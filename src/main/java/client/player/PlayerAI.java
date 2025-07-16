package client.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.GameMap;
import client.map.Position;


public class PlayerAI {
	private static final Logger playerLogger = LoggerFactory.getLogger(PlayerAI.class);

	private PlayerStrategyState state;
	private ArrayList<MovesToPosition> strategy;
	private MovesToPosition movesToNextField;
	private final PathBuilder pathBuilder;
	private int moveCounter;
	
	public PlayerAI() {
		strategy = new ArrayList<>();
		pathBuilder = new PathBuilder();
		movesToNextField = new MovesToPosition();
		moveCounter = 0;
	}
	
	private Move moveToNextPosition() {
		if(moveCounter == 8){
			pathBuilder.getPossibleEnemyCastlePositions();
		}
		moveCounter++;
        playerLogger.info("MoveCounter: {}", moveCounter);
		if(movesToNextField.size() == 1){
            playerLogger.info("Next position: {}", movesToNextField.getEndPosition().toString());
			if(state.isExploring()){
				//explores next field before stepping on
				pathBuilder.exploreField(movesToNextField.getEndPosition());
			}
		}
		return movesToNextField.getMove();
	}
	
	private void updateMovesToTarget() {
		movesToNextField = strategy.removeFirst();
        playerLogger.info("Moves successfully updated: {}", movesToNextField.toString());
	}
	
	public void init(GameMap gameMap) {
		state = PlayerStrategyState.EXPLORING_OWN_HALFMAP;
		pathBuilder.initialize(gameMap);
	}

	private void updateStrategy(PlayerStrategyState newState) {
		state = newState;
		strategy = pathBuilder.getPathBasedOnStrategyState(state);
        playerLogger.info("Strategy with player status '{}' successfully updated: {}", state, strategy);
	}

	public void updateStrategyToExploringEnemyHalfMap() {
		this.updateStrategy(PlayerStrategyState.EXPLORING_ENEMY_HALFMAP);
	}

	public void updateStrategyToTreasure() {
		this.updateStrategy(PlayerStrategyState.GOING_TO_TREASURE);
	}

	public void updateStrategyToFort() {
		this.updateStrategy(PlayerStrategyState.GOING_TO_FORT);
	}
	
	public void updateStrategyToEnemyHalfMap() {
		this.updateStrategy(PlayerStrategyState.GOING_TO_ENEMY_HALFMAP);
	}
	
	public Move move() {
		if(movesToNextField.isEmpty()) {
			if(strategy.isEmpty()) {
				playerLogger.info("Strategy is empty -> update!");
				updateStrategy(state);
			}
			updateMovesToTarget();
		}
        playerLogger.info("Remaining moves to next position: {}", movesToNextField.getMoves());
		return moveToNextPosition();
	}

	public Set<Position> getPossibleEnemyCastlePositions() {
		return pathBuilder.getPossibleEnemyCastlePositions();
	}

	public boolean isExploringEnemyHalfMap() {
		return state.isExploringEnemyHalfMap();
	}

	public boolean isExploringOwnHalfMap() {
		return state.isExploringOwnHalfMap();
	}

	public PlayerStrategyState getPlayerStrategyState() {
		return state;
	}

	public ArrayList<String> getStrategy() {
		ArrayList<String> ret = new ArrayList<>();
		strategy.forEach(move -> ret.add(move.toString()));
		return ret;
	}

	public String getMovesToNextField() {
		return movesToNextField.toString();
	}
}
