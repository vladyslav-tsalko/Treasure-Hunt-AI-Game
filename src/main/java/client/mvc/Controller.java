package client.mvc;

import client.map.GameMap;
import client.server.player_state.PlayerServerStatus;

public class Controller {
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void updateModelInfo(GameMap map, PlayerServerStatus treasureCollected) {
        this.model.setData(map, treasureCollected);
    }
}
