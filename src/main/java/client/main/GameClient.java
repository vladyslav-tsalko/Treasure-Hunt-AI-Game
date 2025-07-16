package client.main;

import client.map.*;
import client.map.half_map.HalfMap;
import client.mvc.*;
import client.player.PlayerAI;
import client.server.*;
import client.server.player_state.PlayerServerStatus;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameClient {
	private static final Logger gameClientLogger = LoggerFactory.getLogger(GameClient.class);
	private static final int THREAD_SLEEP_TIME = 500;
	private final ServerConnection serverConnection;
	private final PlayerServerStatus playerServerStatus;
	private final GameMap gameMap;
	private final PlayerAI playerAI;
    private final Controller controller;

	private void updateClientInfo() throws InterruptedException, ServerResponseException {
		Thread.sleep(THREAD_SLEEP_TIME);
		Map.Entry<GameMap, PlayerServerStatus> newClientInfo = serverConnection.updateClientInfo(gameMap.isInit());
		gameMap.update(newClientInfo.getKey());
		playerServerStatus.update(newClientInfo.getValue());
	}

	private void move() throws ServerResponseException {
		if(playerServerStatus.mustWait()) return;
		controller.updateModelInfo(gameMap, playerServerStatus);
		serverConnection.sendMove(playerAI.move());
	}

	public GameClient(String mode, String serverBaseUrl, String gameId) {
		this.serverConnection = new ServerConnection(serverBaseUrl, gameId);
		this.playerAI = new PlayerAI();
		this.gameMap = new GameMap();
		this.playerServerStatus = new PlayerServerStatus();
        Model gameClientModel = new Model();
		this.controller = new Controller(gameClientModel);
		if(mode.equals("GUI")){
			GUIGameView guiGameView = new GUIGameView(gameClientModel, controller);
		}
		Observer consoleGameView = new ConsoleGameView(gameClientModel, controller);
	}
	
	public void registerPlayer(String playerName, String playerSurname, String playerUAcc) throws ServerResponseException {
		serverConnection.registerPlayer(playerName, playerSurname, playerUAcc);
	}

	public void sendMap() throws InterruptedException, ServerResponseException {
		HalfMap myHalfMap = new HalfMap();
		myHalfMap.generate();
		while(playerServerStatus.mustWait()){
			updateClientInfo();
		}
		serverConnection.sendHalfMap(myHalfMap);//check if not empty
		controller.updateModelInfo(new GameMap(myHalfMap), playerServerStatus);
	}
	
	public void retrieveFullMap() throws InterruptedException, ServerResponseException {
		while(!gameMap.isInit()) {
			updateClientInfo();
		}
		gameClientLogger.info(gameMap.toString());
		playerAI.init(gameMap);
		//if(isGUI) swingGUI.initialize(gameMap);
	}

	private void exploring_own_halfmap(){
		if(gameMap.isTreasureFound()) {
			gameClientLogger.info("Treasure is found at position: {}", gameMap.getTreasurePosition());
			playerAI.updateStrategyToTreasure();
		}
		going_to_treasure();
	}

	private void going_to_treasure() {
		if(playerServerStatus.isTreasureCollected()) {
			gameClientLogger.info("Treasure is collected!");
			playerAI.updateStrategyToEnemyHalfMap();
			gameClientLogger.info("Going to enemy's half!");
		}
	}

	private void going_to_enemy_half(){
		if(gameMap.isMyPlayerOnEnemyHalfMap()) {
			gameClientLogger.info("Is on enemy half! {}", gameMap.getMyPlayerPosition());
			playerAI.updateStrategyToExploringEnemyHalfMap();
			gameClientLogger.info("Searching enemy's fort!");
		}
	}

	private void exploring_enemy_halfmap(){
		if(gameMap.isFortFound() && playerAI.isExploringEnemyHalfMap()) {
			gameClientLogger.info("Enemy's fort is found at position: {}", gameMap.getEnemyFortPosition());
			playerAI.updateStrategyToFort();
		}
	}
	
	public void mainGameLoop() throws InterruptedException, ServerResponseException {
		gameClientLogger.info("Searching my treasure!");
		while(true){
			updateClientInfo();
			if(playerServerStatus.isGameOver()){
				controller.updateModelInfo(gameMap, playerServerStatus);
				break;
			}
			switch(playerAI.getPlayerStrategyState()){
				case EXPLORING_OWN_HALFMAP -> exploring_own_halfmap();
				case GOING_TO_TREASURE -> going_to_treasure();
				case GOING_TO_ENEMY_HALFMAP -> going_to_enemy_half();
				case EXPLORING_ENEMY_HALFMAP -> exploring_enemy_halfmap();
			}
			move();
		}

		if(playerServerStatus.isLost()){
			gameClientLogger.info("Game Lost!");
		}
		else{
			if(gameMap.isPlayerOnEnemyFort()){
				gameClientLogger.info("Fort is taken over, the game is won!");
			}else{
				gameClientLogger.info("Enemy is drawn!");
			}
		}
	}

	
	public static void main(String[] args) {
		if(args.length != 3) {
			System.err.println("There must be 3 arguments: 'game_mode', 'server_url', 'game_id'");
			return;
		}
		if(!args[0].equals("TR") && !args[0].equals("GUI")){
			System.err.println("game_mode must be either 'TR' or 'GUI'");
			return;
		}
		try {
			GameClient mainGameClient = new GameClient(args[0], args[1], args[2]);
			mainGameClient.registerPlayer("Name", "Surname", "UAcc");
			mainGameClient.sendMap();
			mainGameClient.retrieveFullMap();
			mainGameClient.mainGameLoop();
		}catch (ServerResponseException | InterruptedException e) {
			gameClientLogger.error(e.getMessage());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
    }
}
