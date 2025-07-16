package client.server.converter;

import java.util.LinkedHashMap;
import java.util.Map;

import client.map.GameMap;
import client.map.Position;
import client.map.Terrain;
import client.server.player_state.PlayerActState;
import client.server.player_state.PlayerServerStatus;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

class ServerClientDataConverter {
	private GameState gameState;
	
	static private Terrain convertTerrain(ETerrain terrain) {
        return switch (terrain) {
            case Grass -> Terrain.GRASS;
            case Mountain -> Terrain.MOUNTAIN;
            case Water -> Terrain.WATER;
        };
	}
	
	static private PlayerActState convertPlayerState(EPlayerGameState playerGameState) {
        return switch (playerGameState) {
            case Won -> PlayerActState.WON;
            case Lost -> PlayerActState.LOST;
            case MustAct -> PlayerActState.MUST_ACT;
            default -> PlayerActState.MUST_WAIT;
        };
	}
	
	private PlayerServerStatus getPlayerStatus(String playerID) {
		PlayerState playerState = this.gameState.getPlayers().stream()
                .filter(player -> player.getUniquePlayerID().equals(playerID))
                .findFirst()
                .orElse(null);
		return new PlayerServerStatus(playerState.hasCollectedTreasure(), ServerClientDataConverter.convertPlayerState(playerState.getState()));
	}
	
	private GameMap getGameMap(boolean isMapInit) {
		LinkedHashMap<Position, Terrain> mapNodeLinkedHashMap = new LinkedHashMap<>();
		Position treasurePosition = Position.getUninitializedPosition();

		Position myPlayerPosition = new Position();
		Position myFortPosition = new Position();

		Position enemyplayerPosition = new Position();
		Position enemyFortPosition = Position.getUninitializedPosition();

		Position currentPosition;
		for (FullMapNode mapNode : gameState.getMap().getMapNodes()) {
			currentPosition = new Position(mapNode.getX(), mapNode.getY());
			if(mapNode.getFortState() == EFortState.MyFortPresent) 
				myFortPosition = currentPosition;
			else if(mapNode.getFortState() == EFortState.EnemyFortPresent) 
				enemyFortPosition = currentPosition;
			if(mapNode.getTreasureState() == ETreasureState.MyTreasureIsPresent) 
				treasurePosition = currentPosition;
			if(mapNode.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition) {
				myPlayerPosition = currentPosition;
			}else if(mapNode.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition){
				enemyplayerPosition = currentPosition;
			}else if(mapNode.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition){
				myPlayerPosition = new Position(currentPosition);
				enemyplayerPosition = new Position(currentPosition);
			}
				
			if(!isMapInit) mapNodeLinkedHashMap.put(currentPosition, ServerClientDataConverter.convertTerrain(mapNode.getTerrain()));
        }
		return new GameMap(mapNodeLinkedHashMap, treasurePosition, myPlayerPosition, myFortPosition, enemyplayerPosition, enemyFortPosition);
	}
	
	public Map.Entry<GameMap, PlayerServerStatus> updateClientInfo(GameState gameState, boolean isMapInit, String playerID) {
		this.gameState = gameState;
		GameMap newGameMap = getGameMap(isMapInit);
		PlayerServerStatus newPlayerServerStatus = getPlayerStatus(playerID);
		return Map.entry(newGameMap, newPlayerServerStatus);
	}

}