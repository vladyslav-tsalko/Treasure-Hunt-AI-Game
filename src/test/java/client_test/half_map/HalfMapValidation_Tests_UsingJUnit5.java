package client_test.half_map;

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

public class HalfMapValidation_Tests_UsingJUnit5 {
    private static LinkedHashMap<Position, Terrain> initNodesRiverLeft(){
        LinkedHashMap<Position, Terrain> gameMapNodes = new LinkedHashMap<>();
        //y = 0
        gameMapNodes.put(new Position(0, 0), Terrain.WATER);
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
        gameMapNodes.put(new Position(0, 2), Terrain.WATER);
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
        return gameMapNodes;
    }
/*
 |    0    |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |    9    |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
0|  WATER  |  MOUNT  |  GRASS  |  WATER  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
1|  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
2|  WATER  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  MOUNT  |  GRASS  |  GRASS  |  MOUNT  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
3|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
4|  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
*/

    private static LinkedHashMap<Position, Terrain> initNodesRiverUp(){
        LinkedHashMap<Position, Terrain> gameMapNodes = new LinkedHashMap<>();
        //y = 0
        gameMapNodes.put(new Position(0, 0), Terrain.WATER);
        gameMapNodes.put(new Position(1, 0), Terrain.WATER);
        gameMapNodes.put(new Position(2, 0), Terrain.WATER);
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
        return gameMapNodes;
    }

/*
 |    0    |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |    9    |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
0|  WATER  |  WATER  |  WATER  |  WATER  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
1|  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
2|  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  MOUNT  |  GRASS  |  GRASS  |  MOUNT  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
3|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
4|  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
*/

    private static LinkedHashMap<Position, Terrain> initNodesFloodFill1(){
        LinkedHashMap<Position, Terrain> gameMapNodes = new LinkedHashMap<>();
        //y = 0
        gameMapNodes.put(new Position(0, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(3, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(5, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 0), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(8, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(9, 0), Terrain.WATER);

        // y = 1
        gameMapNodes.put(new Position(0, 1), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 1), Terrain.GRASS);
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
        gameMapNodes.put(new Position(8, 3), Terrain.WATER);
        gameMapNodes.put(new Position(9, 3), Terrain.WATER);

        // y = 4
        gameMapNodes.put(new Position(0, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 4), Terrain.MOUNTAIN);
        gameMapNodes.put(new Position(3, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 4), Terrain.WATER);
        gameMapNodes.put(new Position(5, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(6, 4), Terrain.GRASS);
        gameMapNodes.put(new Position(7, 4), Terrain.WATER);
        gameMapNodes.put(new Position(8, 4), Terrain.WATER);
        gameMapNodes.put(new Position(9, 4), Terrain.GRASS);
        return gameMapNodes;
    }

/*
 |    0    |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |    9    |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
0|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
1|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
2|  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  MOUNT  |  GRASS  |  GRASS  |  MOUNT  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
3|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |  WATER  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
4|  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  WATER  |  WATER  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
*/

    private static LinkedHashMap<Position, Terrain> initNodesFloodFill2(){
        LinkedHashMap<Position, Terrain> gameMapNodes = new LinkedHashMap<>();
        //y = 0
        gameMapNodes.put(new Position(0, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(1, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(2, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(3, 0), Terrain.GRASS);
        gameMapNodes.put(new Position(4, 0), Terrain.GRASS);
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
        gameMapNodes.put(new Position(0, 3), Terrain.WATER);
        gameMapNodes.put(new Position(1, 3), Terrain.WATER);
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
        return gameMapNodes;
    }

/*
 |    0    |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |    9    |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
0|  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
1|  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
2|  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  MOUNT  |  MOUNT  |  GRASS  |  GRASS  |  MOUNT  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
3|  WATER  |  WATER  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
4|  GRASS  |  GRASS  |  MOUNT  |  GRASS  |  WATER  |  GRASS  |  GRASS  |  WATER  |  GRASS  |  GRASS  |
-|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|
*/

    private HalfMap halfMap;
    private HalfMapValidator halfMapValidator = new HalfMapValidator();

    @Test
    public void riverOnLeftEdgeTest(){
        halfMap = new HalfMap(HalfMapValidation_Tests_UsingJUnit5.initNodesRiverLeft());
        Set<Position> riverPositionsAnswer = new HashSet<>(Arrays.asList(
                new Position[]{
                        new Position(0, 0),
                        new Position(0, 1),
                        new Position(0, 2)
                }));
        Set<Position> riverPositionsResult = halfMapValidator.riversOnEdges(halfMap);
        assertThat(riverPositionsResult, is(equalTo(riverPositionsAnswer)));
    }

    @Test
    public void riverOnUpperEdgeTest(){
        halfMap = new HalfMap(HalfMapValidation_Tests_UsingJUnit5.initNodesRiverUp());
        Set<Position> riverPositionsAnswer = new HashSet<>(Arrays.asList(
                new Position[]{
                        new Position(0, 0),
                        new Position(1, 0),
                        new Position(2, 0),
                        new Position(3, 0),
                        new Position(4, 0),
                        new Position(9, 0),
                }));
        Set<Position> riverPositionsResult = halfMapValidator.riversOnEdges(halfMap);
        assertThat(riverPositionsResult, is(equalTo(riverPositionsAnswer)));
    }

    @Test
    public void floodFillTest1(){
        halfMap = new HalfMap(HalfMapValidation_Tests_UsingJUnit5.initNodesFloodFill1());
        Set<Position> notAccessiblePositionsAnswer = new HashSet<>(Arrays.asList(
                new Position[]{
                        new Position(9, 4)
                }));
        Set<Position> notAccessiblePositions = halfMapValidator.validateFloodFill(halfMap);
        assertThat(notAccessiblePositions, is(equalTo(notAccessiblePositionsAnswer)));
    }

    @Test
    public void floodFillTest2(){
        halfMap = new HalfMap(HalfMapValidation_Tests_UsingJUnit5.initNodesFloodFill2());
        Set<Position> notAccessiblePositionsAnswer = new HashSet<>(Arrays.asList(
                new Position[]{
                        new Position(0, 2),
                        new Position(1, 2)
                }));
        Set<Position> notAccessiblePositions = halfMapValidator.validateFloodFill(halfMap);
        assertThat(notAccessiblePositions, is(equalTo(notAccessiblePositionsAnswer)));
    }




}


