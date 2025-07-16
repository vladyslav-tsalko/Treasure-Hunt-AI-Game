package client.mvc;

import client.map.GameMap;
import client.map.Position;
import client.player.PlayerStrategyState;
import client.server.player_state.PlayerServerStatus;

import java.util.HashSet;
import java.util.Set;

abstract class GameView implements Observer{
    static protected final String myPlayerEmoji = "🙂";
    static protected final String myPlayerFoundTreasure = "😄";
    static protected final String myPlayerCollectedTreasure = "😁";
    static protected final String enemyPlayerEmoji = "😈";
    static protected final String myFortEmoji = "🏰";
    static protected final String enemyFortEmoji = "🏯";
    static protected final String foundTreasureEmoji = "💰";
    static protected final String collectedTreasureEmoji = "✨";
    static protected final String playerOnTreasure = "🤩";
    static protected final String playersFightEmoji = "🔪";
    static protected final String visitedPositionEmoji = "▫️";
    static protected final String unvisitedPositionEmoji = "▪️";
    static protected final String gameWonMyPlayerEmoji = "🥳";
    static protected final String gameWonEnemyPlayerEmoji = "🤬";
    static protected final String gameLostMyPlayerEmoji = "😭";
    static protected final String gameLostEnemyPlayerEmoji = "👹";

    Controller controller;

    static protected Position treasurePosition = Position.getUninitializedPosition();
    static protected boolean isTreasureFound = false;
    static protected final Set<Position> visitedPositions = new HashSet<>();

    static protected void updateData(GameMap gameMap, PlayerServerStatus playerServerStatus) {
        Position myPlayerPosition = gameMap.getMyPlayerPosition();
        if(gameMap.getGameMapNodes().size() == 100){
            GameView.visitedPositions.add(myPlayerPosition);
            if(gameMap.isTerrainMountain(myPlayerPosition)) GameView.visitedPositions.addAll(gameMap.getGrassFieldsAround(myPlayerPosition, PlayerStrategyState.GOING_TO_ENEMY_HALFMAP));
        }
        if(gameMap.getTreasurePosition().isInit() && !GameView.isTreasureFound) {
            GameView.treasurePosition = gameMap.getTreasurePosition();
            GameView.isTreasureFound = true;
        }
        else if(playerServerStatus.isTreasureCollected() && !GameView.isTreasureFound){
            GameView.treasurePosition = myPlayerPosition;
            GameView.isTreasureFound = true;
        }
    }

    GameView(Model model, Controller controller) {
        this.controller = controller;
        model.addObserver(this);
    }
}
