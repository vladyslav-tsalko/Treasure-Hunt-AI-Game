package client.map.half_map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.Position;
import client.player.Move;


public class HalfMapValidator{
	private static final Logger halfMapValidatorLogger = LoggerFactory.getLogger(HalfMapValidator.class);
	private static final int HALF_MAP_MAX_WATER_HORIZONTAL_EDGE = HalfMap.WIDTH/2-1;
	private static final int HALF_MAP_MAX_WATER_VERTICAL_EDGE = HalfMap.HEIGHT/2;
	private final Set<Position> visitedPositions;
	
	/*private Boolean isPositionVisited(Position position) {
		return this.visitedPositions.get(position.getListIndex(HalfMap.WIDTH));
	}*/
	
	/*private void setPositionVisited(Position position) {
		this.visitedPositions.set(position.getListIndex(HalfMap.WIDTH), true);
	}*/

	private Position getFirstAccessiblePosition(HalfMap halfMap) {
		Position retPosition = halfMap.getFirstAccessiblePosition();
		if(retPosition != null) return retPosition;
		throw new RuntimeException("\n" + halfMap + "\nThere is no accessible field in this map.");
	}

	private void floodFill(Position startPosition, HalfMap halfMap) {
        if (halfMap.isPositionOutOfMap(startPosition)) return;
        if (visitedPositions.contains(startPosition) || !halfMap.isPositionAccessible(startPosition)) return;

    	visitedPositions.add(startPosition);
        for(Move move: Move.values()) {
        	this.floodFill(startPosition.getNextPosition(move), halfMap);
        }
    }
	
	private void init() {
		this.visitedPositions.clear();
		/*for(int index = 0; index<HalfMap.TOTAL; index++) {
			this.visitedPositions.add(false);
		}*/
		halfMapValidatorLogger.info("Validator initialized!");
	}
	
	public HalfMapValidator() {
		this.visitedPositions = new HashSet<>();
	}

	public Set<Position> riversOnEdges(HalfMap halfMap){
		Map<Edge, Set<Position>> edgeWaterFields = new HashMap<>();
		edgeWaterFields.put(Edge.LEFT, new HashSet<>());
		edgeWaterFields.put(Edge.RIGHT, new HashSet<>());
		edgeWaterFields.put(Edge.TOP, new HashSet<>());
		edgeWaterFields.put(Edge.BOTTOM, new HashSet<>());

		Set<Position> ret = new HashSet<>();
		for(int i = 0; i<HalfMap.WIDTH; i++){
			if(halfMap.isPositionWater(new Position(i, 0)))
				edgeWaterFields.get(Edge.TOP).add((new Position(i, 0)));
			if(halfMap.isPositionWater(new Position(i, HalfMap.HEIGHT-1)))
				edgeWaterFields.get(Edge.BOTTOM).add((new Position(i, HalfMap.HEIGHT-1)));
		}
		for(int i = 0; i<HalfMap.HEIGHT; i++){
			if(halfMap.isPositionWater(new Position(0, i)))
				edgeWaterFields.get(Edge.LEFT).add((new Position(0, i)));
			if(halfMap.isPositionWater(new Position(HalfMap.WIDTH-1, i)))
				edgeWaterFields.get(Edge.RIGHT).add((new Position(HalfMap.WIDTH-1, i)));
		}
		for(Edge edge: Edge.values()) {
			if((edge.equals(Edge.TOP) || edge.equals(Edge.BOTTOM)) && edgeWaterFields.get(edge).size() > HALF_MAP_MAX_WATER_HORIZONTAL_EDGE){
				ret.addAll(edgeWaterFields.get(edge));
			}
			if((edge.equals(Edge.LEFT) || edge.equals(Edge.RIGHT)) && edgeWaterFields.get(edge).size() > HALF_MAP_MAX_WATER_VERTICAL_EDGE){
				ret.addAll(edgeWaterFields.get(edge));
			}
		}
		return ret;
	}

	public Set<Position> validateFloodFill(HalfMap halfMap){
		halfMapValidatorLogger.info("FloodFill started!");
		this.init();
		this.floodFill(this.getFirstAccessiblePosition(halfMap), halfMap);
		Set<Position> notAccessiblePositions = halfMap.getAccessiblePositions();
		notAccessiblePositions.removeAll(this.visitedPositions);
		halfMapValidatorLogger.info(notAccessiblePositions.isEmpty() ? "FloodFill successfully finished!": "FloodFill finished with not accessible positions: " + notAccessiblePositions);
		return notAccessiblePositions;
	}

	public String validateHalfMap(HalfMap halfMap) {
		this.init();
		Set<Position> notAccessiblePositions = validateFloodFill(halfMap);
		Set<Position> riversOnEdgesPositions = riversOnEdges(halfMap);
		if(notAccessiblePositions.isEmpty() && riversOnEdgesPositions.isEmpty()) return "";
		StringBuilder ret = new StringBuilder("Map isn't valid - ");
		if(!notAccessiblePositions.isEmpty()){
			ret.append("not accessible positions: ").append(notAccessiblePositions).append(", ");
		}
		if(!riversOnEdgesPositions.isEmpty()){
			ret.append("rivers on edges positions: ").append(riversOnEdgesPositions);
		}
		return ret.toString();
    }
}
