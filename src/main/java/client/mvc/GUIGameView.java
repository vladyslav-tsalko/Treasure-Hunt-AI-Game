package client.mvc;

import client.map.EmptyTerrainException;
import client.map.GameMap;
import client.map.Position;
import client.map.Terrain;
import client.player.PlayerStrategyState;
import client.server.player_state.PlayerServerStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GUIGameView extends GameView {
    private final JFrame frame;
    private final JPanel panel;
    private final ArrayList<JLabel> labels;
    //private GameMap gameMap;
    private boolean init;
    //private Position treasurePosition;
    //private boolean isTreasureFound;
    //private final Set<Position> visitedPositions;

    private void checkInit(){
        if(!init) throw new RuntimeException("SwingGUI must be initialized!");
    }

    private void updateLabelText(GameMap gameMap, PlayerServerStatus playerServerStatus) {
        checkInit();
        //gameMap = map;
        Position myPlayerPosition = gameMap.getMyPlayerPosition();
        visitedPositions.add(myPlayerPosition);
        if(gameMap.isTerrainMountain(myPlayerPosition)) visitedPositions.addAll(gameMap.getGrassFieldsAround(myPlayerPosition, PlayerStrategyState.GOING_TO_ENEMY_HALFMAP));

        if(gameMap.getTreasurePosition().isInit() && !isTreasureFound) {
            treasurePosition = gameMap.getTreasurePosition();
            isTreasureFound = true;
        }
        else if(playerServerStatus.isTreasureCollected() && !isTreasureFound){
            treasurePosition = myPlayerPosition;
            isTreasureFound = true;
        }
        //Position position = new Position();
        //JLabel currentLabel = labels.get(position.getListIndex(gameMap.getDimensions().x()));

        gameMap.getGameMapNodes().forEach((position, terrain) -> {
            String newEmoji = "";
            if(position.equals(gameMap.getMyPlayerPosition())){
                if(position.equals(gameMap.getFortPosition())) newEmoji = myFortEmoji;
                else if(position.equals(gameMap.getEnemyPlayerPosition())) newEmoji = playersFightEmoji;
                else if(position.equals(treasurePosition)) newEmoji = playerOnTreasure;
                else{
                    if(playerServerStatus.isWon()) newEmoji = gameWonMyPlayerEmoji;
                    else if(playerServerStatus.isLost()) newEmoji = gameLostMyPlayerEmoji;
                    else{
                        if(isTreasureFound && !playerServerStatus.isTreasureCollected()) newEmoji = myPlayerFoundTreasure;
                        else if(isTreasureFound && playerServerStatus.isTreasureCollected()) newEmoji = myPlayerCollectedTreasure;
                        else newEmoji = myPlayerEmoji;
                    }
                }
            }else if(position.equals(gameMap.getEnemyPlayerPosition())){
                if(playerServerStatus.isWon()) newEmoji = gameWonEnemyPlayerEmoji;
                else if(playerServerStatus.isLost()) newEmoji = gameLostEnemyPlayerEmoji;
                else newEmoji = enemyPlayerEmoji;
            } else if(position.equals(gameMap.getFortPosition())) newEmoji = myFortEmoji;
            else if(position.equals(gameMap.getEnemyFortPosition())) newEmoji = enemyFortEmoji;
            else if(position.equals(treasurePosition)) newEmoji = playerServerStatus.isTreasureCollected() ? collectedTreasureEmoji: foundTreasureEmoji;
            else newEmoji = visitedPositions.contains(position) ? "👣": "";
            JLabel currentLabel = labels.get(position.getListIndex(gameMap.getDimensions().x()+1));
            currentLabel.setText(newEmoji);
            currentLabel.repaint();
        });
        //currentLabel.setText("Updated Text for Label 2");
    }

    private void setPanel(GameMap gameMap) {
        Position dimensions = gameMap.getDimensions();
        panel.setLayout(new GridLayout(dimensions.y() + 1, dimensions.x() + 1, 5, 5));
        gameMap.getGameMapNodes().forEach((position, terrain) -> {
            JLabel label = new JLabel();

            switch (terrain) {
                case GRASS -> label.setBackground(new Color(124, 252, 0));
                case WATER -> label.setBackground(new Color(30, 144, 255));
                case MOUNTAIN -> label.setBackground(new Color(169, 169, 169));
                case EMPTY -> throw new EmptyTerrainException();
            }
            if(position.equals(gameMap.getMyPlayerPosition())){
                if(position.equals(gameMap.getFortPosition())) label.setText(myFortEmoji);
                else label.setText(myPlayerEmoji);
            }else if(position.equals(gameMap.getEnemyPlayerPosition())){
                if(position.equals(gameMap.getEnemyFortPosition())) label.setText(enemyFortEmoji);
                else label.setText(enemyPlayerEmoji);
            } else if(position.equals(gameMap.getFortPosition())) label.setText(myFortEmoji);
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setFont(new Font("Arial", Font.PLAIN, 30));
            labels.add(label);
            panel.add(label);
        });
    }

    public GUIGameView(Model model, Controller controller) {
        super(model, controller);
        frame = new JFrame("2D Terrain Map");
        panel = new JPanel();
        labels = new ArrayList<>();
        init = false;
        //visitedPositions = new HashSet<>();
    }

    public void initialize(GameMap map) {
        if(map.getGameMapNodes().size() != 100) return;
        //gameMap = map;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(map.getDimensions().x() == 10) frame.setSize(800, 600);
        else frame.setSize(1600, 600);
        this.setPanel(map);
        frame.add(panel);
        frame.setVisible(true);
        init = true;
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof Model) {
            Model model = (Model) observable;
            if(!init) initialize(model.getGameMap());
            if(init) updateLabelText(model.getGameMap(), model.getPlayerServerStatus());
        }
    }
}
