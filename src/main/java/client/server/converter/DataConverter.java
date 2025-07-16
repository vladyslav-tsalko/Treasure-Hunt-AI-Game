package client.server.converter;

import java.util.LinkedHashMap;
import java.util.Map;

import client.map.GameMap;
import client.map.Position;
import client.map.Terrain;
import client.player.Move;
import client.server.player_state.PlayerServerStatus;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromserver.GameState;

public class DataConverter {
	private final ClientServerDataConverter clientServer;
	private final ServerClientDataConverter serverClient;
	
	public DataConverter() {
		this.clientServer = new ClientServerDataConverter();
		this.serverClient = new ServerClientDataConverter();
	}
	
	public PlayerHalfMap getHalfMap(String uniquePlayerID, Position fortPos,  LinkedHashMap<Position, Terrain> generatedHalfMap) {
		return clientServer.convertHalfMap(uniquePlayerID, fortPos, generatedHalfMap);
	}
	
	public EMove getMove(Move move) {
		return clientServer.convertMove(move);
	}
	
	public Map.Entry<GameMap, PlayerServerStatus> getClientInfo(GameState gameState, boolean isMapInit, String playerID) {
		return serverClient.updateClientInfo(gameState, isMapInit, playerID);
	}
}
