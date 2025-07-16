package client.server.converter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import client.map.*;
import client.player.Move;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

class ClientServerDataConverter {
	public ETerrain convertTerrain(Terrain terrain) {
        return switch (terrain) {
            case GRASS -> ETerrain.Grass;
            case MOUNTAIN -> ETerrain.Mountain;
            case WATER -> ETerrain.Water;
            case EMPTY -> throw new EmptyTerrainException();
        };
	}
	
	public EMove convertMove(Move move) {
        return switch (move) {
            case UP -> EMove.Up;
            case DOWN -> EMove.Down;
            case LEFT -> EMove.Left;
            case RIGHT -> EMove.Right;
        };
	}
	
	public PlayerHalfMap convertHalfMap(String uniquePlayerID, Position fortPos,  LinkedHashMap<Position, Terrain> generatedHalfMap) {
		HashSet<PlayerHalfMapNode> halfMapNodeSet = new HashSet<>();
		for (Map.Entry<Position, Terrain> entry : generatedHalfMap.entrySet()) {
			Position curPos = entry.getKey();
			halfMapNodeSet.add(new PlayerHalfMapNode(curPos.x(), curPos.y(), curPos.equals(fortPos), convertTerrain(entry.getValue())));
        }
		return new PlayerHalfMap(uniquePlayerID, halfMapNodeSet);
	}
}
