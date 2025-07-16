package client.mvc;

import client.map.GameMap;
import client.map.Position;
import client.player.PlayerStrategyState;
import client.server.player_state.PlayerServerStatus;

import java.util.HashSet;
import java.util.Set;

public class Model extends Observable {

    private GameMap gameMap;
    private PlayerServerStatus playerServerStatus;

    public Model() {
        gameMap = new GameMap();
        playerServerStatus = new PlayerServerStatus();
    }

    GameMap getGameMap() {
        return gameMap;
    }

    PlayerServerStatus getPlayerServerStatus() {
        return playerServerStatus;
    }

    void setData(GameMap newGameMap, PlayerServerStatus newPlayerServerStatus) {
        if(!newGameMap.isEmpty() && !gameMap.equals(newGameMap)){
            gameMap = new GameMap(newGameMap);
            playerServerStatus = newPlayerServerStatus;
            GameView.updateData(gameMap, playerServerStatus);
            this.notifyObservers(this);
        }
    }
}
