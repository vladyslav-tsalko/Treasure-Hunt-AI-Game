package client_test.player;

import client.map.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

import client.player.Move;
import client.player.PlayerAI;
import client.player.PlayerStrategyState;
import org.junit.jupiter.api.*;

public class PlayerAI_Tests_UsingJUnit5 {
    private GameMap gameMap = new GameMap();
    private PlayerAI playerAI = new PlayerAI();
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

    @BeforeEach
    public void setUp() {
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, new Position(0,8), new Position(0,8), enemyPlayerPosition, enemyFortPosition));
        playerAI.init(gameMap);
    }

    @Test
    public void playerStrategyTest1(){
        assertThat(playerAI.getPlayerStrategyState(), is(equalTo(PlayerStrategyState.EXPLORING_OWN_HALFMAP)));
        assertThat(playerAI.isExploringOwnHalfMap(), is(equalTo(true)));
        assertThat(playerAI.isExploringEnemyHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isExploring(), is(equalTo(true)));
        assertThat(playerAI.getPlayerStrategyState().isOnOwnHalfMap(), is(equalTo(true)));
        assertThat(playerAI.getPlayerStrategyState().isOnEnemyHalfMap(), is(equalTo(false)));
    }

    @Test
    public void playerStrategyTest2(){
        playerAI.updateStrategyToTreasure();
        assertThat(playerAI.getPlayerStrategyState(), is(equalTo(PlayerStrategyState.GOING_TO_TREASURE)));
        assertThat(playerAI.isExploringOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.isExploringEnemyHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isExploring(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isOnOwnHalfMap(), is(equalTo(true)));
        assertThat(playerAI.getPlayerStrategyState().isOnEnemyHalfMap(), is(equalTo(false)));
    }

    @Test
    public void playerStrategyTest3(){
        playerAI.updateStrategyToEnemyHalfMap();
        assertThat(playerAI.getPlayerStrategyState(), is(equalTo(PlayerStrategyState.GOING_TO_ENEMY_HALFMAP)));
        assertThat(playerAI.isExploringOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.isExploringEnemyHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isExploring(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isOnOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isOnEnemyHalfMap(), is(equalTo(false)));
    }

    @Test
    public void playerStrategyTest4(){
        //update like we are on enemy half
        gameMap.update(new GameMap(gameMapNodes, Position.getUninitializedPosition(), new Position(0,3), new Position(0,8), Position.getUninitializedPosition(), Position.getUninitializedPosition()));
        playerAI.updateStrategyToExploringEnemyHalfMap();
        assertThat(playerAI.getPlayerStrategyState(), is(equalTo(PlayerStrategyState.EXPLORING_ENEMY_HALFMAP)));
        assertThat(playerAI.isExploringOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.isExploringEnemyHalfMap(), is(equalTo(true)));
        assertThat(playerAI.getPlayerStrategyState().isExploring(), is(equalTo(true)));
        assertThat(playerAI.getPlayerStrategyState().isOnOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isOnEnemyHalfMap(), is(equalTo(true)));
    }

    @Test
    public void playerStrategyTest5(){
        gameMap.update(new GameMap(gameMapNodes, Position.getUninitializedPosition(), new Position(0,3), new Position(0,8), Position.getUninitializedPosition(), new Position(0,4)));
        playerAI.updateStrategyToFort();
        assertThat(playerAI.getPlayerStrategyState(), is(equalTo(PlayerStrategyState.GOING_TO_FORT)));
        assertThat(playerAI.isExploringOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.isExploringEnemyHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isExploring(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isOnOwnHalfMap(), is(equalTo(false)));
        assertThat(playerAI.getPlayerStrategyState().isOnEnemyHalfMap(), is(equalTo(true)));
    }


}


/*
 |    0    |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |    9    |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
0|  MOUNT  |  MOUNT  |  GRASS  |  WATER  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
1|  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
2|  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  MOUNT  |  GRASS  |  GRASS  |  MOUNT  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
3|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
4|  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
5|  GRASS  |  GRASS  |  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
6|  GRASS  |  GRASS  |  GRASS  |  MOUNT  |  MOUNT  |  GRASS  |  GRASS  |  WATER  |  MOUNT  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
7|  WATER  |  GRASS  |  WATER  |  WATER  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
8|  GRASS  |  GRASS  |  MOUNT  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  GRASS  |  MOUNT  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
9|  GRASS  |  GRASS  |  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
*/