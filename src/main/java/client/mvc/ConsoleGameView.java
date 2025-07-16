package client.mvc;

import client.map.EmptyTerrainException;
import client.map.GameMap;
import client.map.Position;
import client.map.Terrain;
import client.player.PlayerStrategyState;
import client.server.player_state.PlayerServerStatus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConsoleGameView extends GameView {
    //private Position treasurePosition;
    //private boolean isTreasureFound;
    //private final Set<Position> visitedPositions;

    public ConsoleGameView(Model model, Controller controller) {
        super(model, controller);
        treasurePosition = Position.getUninitializedPosition();
        isTreasureFound = false;
        //visitedPositions = new HashSet<>();
    }

    private String[] create3x3Block(Terrain terrain, String addEmoji) {
        String emoji = "";
        switch (terrain) {
            case GRASS -> emoji = " 🟩";
            case WATER -> {
                emoji = " 🟦";
                addEmoji = "🟦";
            }
            case MOUNTAIN -> emoji = " 🟫";
            case EMPTY -> throw new EmptyTerrainException();
        }
        String upAndDown = emoji.repeat(3) + " ";
        String middle = emoji + " " + addEmoji + emoji + " ";
        return new String[] {
                upAndDown,
                middle,
                upAndDown
        };
    }

    private String getMapOutput(GameMap map, PlayerServerStatus playerServerStatus) {
        Map<Position, Terrain> getGameMapNodes = map.getGameMapNodes();
        Position dimensions = map.getDimensions();

        StringBuilder output = new StringBuilder("\n");
        String separatorString = "\n" + "-".repeat(dimensions.x() == 9 ? 117: 234) + "\n";

        output.append(separatorString);

        Position myPlayerPosition = map.getMyPlayerPosition();
        if(getGameMapNodes.size() == 100){
            visitedPositions.add(myPlayerPosition);
            if(map.isTerrainMountain(myPlayerPosition)) visitedPositions.addAll(map.getGrassFieldsAround(myPlayerPosition, PlayerStrategyState.GOING_TO_ENEMY_HALFMAP));
        }

        Position myFortPosition = map.getFortPosition();
        Position enemyPlayerPosition = map.getEnemyPlayerPosition();
        Position enemyFortPosition = map.getEnemyFortPosition();
        //Position treasurePosition = map.getTreasurePosition();
        if(map.getTreasurePosition().isInit() && !isTreasureFound) {
            treasurePosition = map.getTreasurePosition();
            isTreasureFound = true;
        }
        else if(playerServerStatus.isTreasureCollected() && !isTreasureFound){
            treasurePosition = myPlayerPosition;
            isTreasureFound = true;
        }

        for (int y = 0; y <= dimensions.y(); y++) {
            StringBuilder topRow = new StringBuilder(" |");
            StringBuilder midRow = new StringBuilder(" |");
            StringBuilder botRow = new StringBuilder(" |");

            for (int x = 0; x <= dimensions.x(); x++) {
                Position currentPosition = new Position(x, y);
                Terrain terrain = getGameMapNodes.get(currentPosition);
                String[] block;
                if(currentPosition.equals(myPlayerPosition)){
                    if(currentPosition.equals(myFortPosition)) block = create3x3Block(terrain, myFortEmoji);
                    else if(currentPosition.equals(enemyPlayerPosition)) block = create3x3Block(terrain, playersFightEmoji);
                    else if(currentPosition.equals(treasurePosition)) block = create3x3Block(terrain, playerOnTreasure);
                    else{
                        if(playerServerStatus.isWon()) block = create3x3Block(terrain, gameWonMyPlayerEmoji);
                        else if(playerServerStatus.isLost()) block = create3x3Block(terrain, gameLostMyPlayerEmoji);
                        else{
                            if(isTreasureFound && !playerServerStatus.isTreasureCollected()) block = create3x3Block(terrain, myPlayerFoundTreasure);
                            else if(isTreasureFound && playerServerStatus.isTreasureCollected()) block = create3x3Block(terrain, myPlayerCollectedTreasure);
                            else block = create3x3Block(terrain, myPlayerEmoji);
                        }
                    }
                }else if(currentPosition.equals(enemyPlayerPosition)){
                    if(playerServerStatus.isWon()) block = create3x3Block(terrain, gameWonEnemyPlayerEmoji);
                    else if(playerServerStatus.isLost()) block = create3x3Block(terrain, gameLostEnemyPlayerEmoji);
                    else block = create3x3Block(terrain, enemyPlayerEmoji);
                } else if(currentPosition.equals(myFortPosition)) block = create3x3Block(terrain, myFortEmoji);
                else if(currentPosition.equals(enemyFortPosition)) block = create3x3Block(terrain, enemyFortEmoji);
                else if(currentPosition.equals(treasurePosition)) block = create3x3Block(terrain, playerServerStatus.isTreasureCollected() ? collectedTreasureEmoji: foundTreasureEmoji);
                else block = create3x3Block(terrain, visitedPositions.contains(currentPosition) ? visitedPositionEmoji: unvisitedPositionEmoji);

                topRow.append(block[0]).append("|");
                midRow.append(block[1]).append("|");
                botRow.append(block[2]).append("|");
            }

            output.append(topRow).append("\n").append(midRow).append("\n").append(botRow).append(separatorString);
        }
        return output.toString();
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof Model model) {
            String output = getMapOutput(model.getGameMap(), model.getPlayerServerStatus());
            System.out.println(output);
        }
    }
}
