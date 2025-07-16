package client_test.player;

import client.map.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

import client.player.Move;
import client.player.PlayerAI;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

public class PlayerAIPathFinding_Tests_UsingJUnit5 {

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

    public void possibleEnemyFortPositionsSetUp(Position enemyPlayerPosition, Position enemyFortPosition, Position myFortPosition) {
        Position treasurePosition = new Position(8, 4);
        Position myPlayerPosition = new Position(6, 2);
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, myPlayerPosition, myFortPosition, enemyPlayerPosition, enemyFortPosition));
        playerAI.init(gameMap);
    }

    public void closestPathDijkstraSetUp(Position myPlayerPosition, Position myFortPosition) {
        Position treasurePosition = Position.getUninitializedPosition();
        Position enemyPlayerPosition = Position.getUninitializedPosition();
        Position enemyFortPosition = Position.getUninitializedPosition();
        gameMap.update(new GameMap(gameMapNodes, treasurePosition, myPlayerPosition, myFortPosition, enemyPlayerPosition, enemyFortPosition));
        playerAI.init(gameMap);
    }

    @Test
    public void testInitOfGameMap(){
        GameMap gM = new GameMap(gameMapNodes, new Position(8, 4), new Position(6, 2), new Position(3, 2), new Position(3, 6), new Position(5, 6));
        Executable isTerrainGrassTest = () -> gM.isTerrainGrass(new Position(1,1));
        Assertions.assertThrows(RuntimeException.class, isTerrainGrassTest,
                "We expected an exception because game map must be initialized before being used");
    }

    @Test
    public void possibleEnemyFortPositionsTest1(){
        this.possibleEnemyFortPositionsSetUp(new Position(3, 6), new Position(5, 6), new Position(3, 2));

        Set<Position> answer = new HashSet<>(Arrays.asList(
                new Position[]{
                        new Position(0, 5),
                        new Position(1, 5),
                        new Position(0, 6),
                        new Position(1, 6),
                        new Position(2, 6),
                        new Position(1, 7),
                        new Position(1, 8),
                        new Position(4, 5),
                        new Position(4, 7),
                        new Position(4, 8),
                        new Position(5, 5),
                        new Position(5, 6),
                        new Position(6, 6),
                }
        ));

        Set<Position> result = playerAI.getPossibleEnemyCastlePositions();
        assertThat(result, is(equalTo(answer)));
    }

    @Test
    public void possibleEnemyFortPositionsTest2(){
        this.possibleEnemyFortPositionsSetUp(new Position(3, 1), new Position(5, 0), new Position(9, 9));
        Set<Position> answer = new HashSet<>(Arrays.asList(
                new Position[]{
                        new Position(2, 0),
                        new Position(5, 0),
                        new Position(6, 0),
                        new Position(2, 1),
                        new Position(3, 1),
                        new Position(4, 1),
                        new Position(5, 1),
                        new Position(6, 1),
                        new Position(7, 1),
                        new Position(3, 2),
                        new Position(4, 2),
                        new Position(1, 3),
                        new Position(2, 3),
                        new Position(3, 3),
                        new Position(4, 3),
                        new Position(5, 3),
                        new Position(3, 4),
                }
        ));
        Set<Position> result = playerAI.getPossibleEnemyCastlePositions();
        System.out.println(result.toString());
        assertThat(result, is(equalTo(answer)));
    }

    @Test
    public void calculateClosestPathDijkstraClosestMountainTest(){
        closestPathDijkstraSetUp(new Position(0,2), new Position(0,2));
        ArrayList<String> strategyAnswer = new ArrayList<>();
        strategyAnswer.add("(0, 3) -> (0, 4): [DOWN, DOWN];");
        strategyAnswer.add("(0, 4) -> (1, 4): [RIGHT, RIGHT];");
        strategyAnswer.add("(1, 4) -> (2, 4): [RIGHT, RIGHT, RIGHT];"); //must be chosen the closest mount on (2,4)
        String movesToTargetAnswer = "(0, 2) -> (0, 3): [DOWN];"; //He performed an action, so only 1 time DOWN remaining
        Move moveAnswer = Move.DOWN;
        assertThat(playerAI.move(), is(equalTo(moveAnswer)));
        assertThat(playerAI.getStrategy(), is(equalTo(strategyAnswer)));
        assertThat(playerAI.getMovesToNextField(), is(equalTo(movesToTargetAnswer)));
    }

    @Test
    public void calculateClosestPathDijkstraSkippingClosestMountainTest1(){
        //mountains on (1,0) and (0,0) are closest, but should be skipped, because don't open any grass field
        closestPathDijkstraSetUp(new Position(2,1), new Position(2,1));
        ArrayList<String> strategyAnswer = new ArrayList<>();
        strategyAnswer.add("(3, 1) -> (4, 1): [RIGHT, RIGHT];");
        strategyAnswer.add("(4, 1) -> (4, 2): [DOWN, DOWN];");
        strategyAnswer.add("(4, 2) -> (5, 2): [RIGHT, RIGHT, RIGHT];"); //must be chosen the closest mount on (5,2)
        String movesToTargetAnswer = "(2, 1) -> (3, 1): [RIGHT];"; //He performed an action, so only 1 time RIGHT remaining
        Move moveAnswer = Move.RIGHT;
        assertThat(playerAI.move(), is(equalTo(moveAnswer)));
        assertThat(playerAI.getStrategy(), is(equalTo(strategyAnswer)));
        assertThat(playerAI.getMovesToNextField(), is(equalTo(movesToTargetAnswer)));
    }

    @Test
    public void calculateClosestPathDijkstraSkippingClosestMountainTest2(){
        //mountain on (2,8) is the closest, but it should be skipped, because there will be 2 fields to be explored,
        //because after stepping on (1,8) there will be only (1,7) and (1,9) unvisited positions. path from (1,8) to
        //closest (either (1,7) or (1,9) doesn't matter) and then to another one will be 6 moves, which is not >6, so
        //the mountain should be skipped. instead mountain (3,6) should be chosen, because after coming to (2,6)
        //there will be 2 positions (4,5) and (4,7), and path to visit them is much more than 6 (stepping on mount
        //(4,6) doesn't count unfortunately with my algo).
        closestPathDijkstraSetUp(new Position(0,8), new Position(0,8));
        ArrayList<String> strategyAnswer = new ArrayList<>();
        strategyAnswer.add("(1, 8) -> (1, 7): [UP, UP];");
        strategyAnswer.add("(1, 7) -> (1, 6): [UP, UP];");
        strategyAnswer.add("(1, 6) -> (2, 6): [RIGHT, RIGHT];");
        strategyAnswer.add("(2, 6) -> (3, 6): [RIGHT, RIGHT, RIGHT];"); //must be chosen the closest mount on (3,6)
        String movesToTargetAnswer = "(0, 8) -> (1, 8): [RIGHT];"; //He performed an action, so only 1 time RIGHT remaining
        Move moveAnswer = Move.RIGHT;
        Move moveSent = playerAI.move();
        assertThat(moveSent, is(equalTo(moveAnswer)));
        assertThat(playerAI.getStrategy(), is(equalTo(strategyAnswer)));
        assertThat(playerAI.getMovesToNextField(), is(equalTo(movesToTargetAnswer)));
    }

    @Test
    public void calculateClosestPathDijkstraSkippingClosestMountainTest3(){
        //repeat prev test untill we come to (3,6), mount (4,6) should be chosen, because it opens
        //2 new positions (5,5) and (5,6), because path from (3,6) to (5,5) or (5,6) and then to another one is more
        //than 6
        closestPathDijkstraSetUp(new Position(0,8), new Position(0,8));
        String movesToTargetAnswer = "(3, 6) -> (4, 6): [RIGHT, RIGHT, RIGHT];"; //He performed an action, so 4-1=3 RIGHT remaining
        Move moveAnswer = Move.RIGHT;
        //simulate going to (3,6)
        do{
            playerAI.move();
        }while(!playerAI.getStrategy().isEmpty());//now
        do{
            playerAI.move();
        }while(!playerAI.getMovesToNextField().equals("(2, 6) -> (3, 6): [];"));
        //simulate receiving a new map from server
        gameMap.update(new GameMap(gameMapNodes, Position.getUninitializedPosition(), new Position(3, 6), new Position(0,8), Position.getUninitializedPosition(), Position.getUninitializedPosition()));
        assertThat(playerAI.move(), is(equalTo(moveAnswer)));
        assertThat(playerAI.getMovesToNextField(), is(equalTo(movesToTargetAnswer)));
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