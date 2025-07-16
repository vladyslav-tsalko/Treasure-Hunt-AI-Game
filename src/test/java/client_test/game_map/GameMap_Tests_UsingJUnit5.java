package client_test.game_map;

import client.map.GameMap;
import client.map.Position;
import client.map.Terrain;
import client.map.half_map.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

import client.player.PlayerAI;
import client.player.PlayerStrategyState;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

public class GameMap_Tests_UsingJUnit5 {
    private static final String ANSI_BROWN = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";
    private GameMap gameMap = new GameMap();
    private static LinkedHashMap<Position, Terrain> gameMapNodes;

    @BeforeAll
    public static void setUpGameMapNodes() {
        gameMapNodes = new LinkedHashMap<>();
        //y = 0
        gameMapNodes.put(new Position(0, 0), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(1, 0), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(2, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(3, 0), Terrain.WATER);
        gameMapNodes.put(new Position(4, 0), Terrain.WATER);
        gameMapNodes.put(new Position(5, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 0), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(8, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 0), Terrain.WATER);

        // y = 1
        gameMapNodes.put(new Position(0, 1), Terrain.WATER);
        gameMapNodes.put(new Position(1, 1), Terrain.WATER);
        gameMapNodes.put(new Position(2, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(3, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(8, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 1), Terrain.WATER);

        // y = 2
        gameMapNodes.put(new Position(0, 2), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 2), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 2), Terrain.WATER);
        gameMapNodes.put(new Position(3, 2), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 2), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 2), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(6, 2), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(7, 2), Terrain.GRASS);
        gameMapNodes.put(new Position(8, 2), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 2), Terrain.MOUNTAIN);

        // y = 3
        gameMapNodes.put(new Position(0, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(3, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 3), Terrain.WATER);
        gameMapNodes.put(new Position(8, 3), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 3), Terrain.GRASS);

        // y = 4
        gameMapNodes.put(new Position(0, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 4), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(3, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 4), Terrain.WATER);
        gameMapNodes.put(new Position(5, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 4), Terrain.WATER);
        gameMapNodes.put(new Position(8, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 4), Terrain.GRASS);

        // y = 5
        gameMapNodes.put(new Position(0, 5), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 5), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 5), Terrain.WATER);
        gameMapNodes.put(new Position(3, 5), Terrain.WATER);
        gameMapNodes.put(new Position(4, 5), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 5), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 5), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 5), Terrain.GRASS);
        gameMapNodes.put(new Position(8, 5), Terrain.WATER);
        gameMapNodes.put(new Position(9, 5), Terrain.GRASS);

        // y = 6
        gameMapNodes.put(new Position(0, 6), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 6), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 6), Terrain.GRASS);
        gameMapNodes.put(new Position(3, 6), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(4, 6), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(5, 6), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 6), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 6), Terrain.WATER);
        gameMapNodes.put(new Position(8, 6), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(9, 6), Terrain.GRASS);

        // y = 7
        gameMapNodes.put(new Position(0, 7), Terrain.WATER);
        gameMapNodes.put(new Position(1, 7), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 7), Terrain.WATER);
        gameMapNodes.put(new Position(3, 7), Terrain.WATER);
        gameMapNodes.put(new Position(4, 7), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 7), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(6, 7), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 7), Terrain.WATER);
        gameMapNodes.put(new Position(8, 7), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 7), Terrain.GRASS);

        // y = 8
        gameMapNodes.put(new Position(0, 8), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 8), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 8), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(3, 8), Terrain.WATER);
        gameMapNodes.put(new Position(4, 8), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 8), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 8), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(7, 8), Terrain.GRASS);
        gameMapNodes.put(new Position(8, 8), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 8), Terrain.MOUNTAIN);

        // y = 9
        gameMapNodes.put(new Position(0, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 9), Terrain.WATER);
        gameMapNodes.put(new Position(3, 9), Terrain.WATER);
        gameMapNodes.put(new Position(4, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(8, 9), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 9), Terrain.GRASS);
    }

    @Test
    public void gameMapIsEmptyTestFailed(){
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assert !gameMap.isEmpty();
    }

    @Test
    public void gameMapIsEmptyTestSuccess(){
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(new LinkedHashMap<>(), treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assert gameMap.isEmpty();
    }

    @Test
    public void gameMapCheckDimensions(){
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assertThat(gameMap.getDimensions(), is(equalTo(new Position(9,9))));
    }

    @Test
    public void gameMapCheckIfPlayerOnEnemyHalfMap(){
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assertThat(gameMap.isMyPlayerOnEnemyHalfMap(), is(equalTo(false)));
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, new Position(0,2), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assertThat(gameMap.isMyPlayerOnEnemyHalfMap(), is(equalTo(true)));
    }

    @Test
    public void gameMapEqualsTest(){
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assertThat(gameMap.isMyPlayerOnEnemyHalfMap(), is(equalTo(false)));
        GameMap testGameMap = new GameMap( new GameMap(gameMapNodes, treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        assert gameMap.equals(testGameMap);
    }
}
