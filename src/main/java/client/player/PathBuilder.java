package client.player;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.GameMap;
import client.map.Position;

class PathBuilder {
	private static final Logger pathBuilderLogger = LoggerFactory.getLogger(PathBuilder.class);

	private record State(Position vertex, int distance, Set<Position> notVisitedGrassPositions) implements Comparable<State> {
		@Override
		public int compareTo(State other) {
			int compareDistance = Integer.compare(this.distance, other.distance);
			return compareDistance == 0 ? Integer.compare(other.notVisitedGrassPositions.size(), this.notVisitedGrassPositions.size()): compareDistance;
		}
	}


	private GameMap gameMap;
	private boolean init;
	private boolean isEnemyCastlePossiblePositionsApplied;

	private Set<Position> enemyCastlePossiblePositions;
	private Set<Position> remainingMountainFields;
	private Set<Position> remainingGrassFields;
	private PlayerStrategyState currentPlayerStrategyState;

	private void checkInit(){
		if(!init) throw new RuntimeException("Path Finder must be initialized!");
	}
	
	public void initialize(GameMap gameMap) {
		this.gameMap = gameMap;
		remainingMountainFields = new HashSet<>();
		remainingGrassFields = new HashSet<>();
		init = true;
	}

    private ArrayList<MovesToPosition> calculateClosestPathDijkstra(Position source, Position target) {
    	Map<Position, Integer> distanceMap = new HashMap<>();
    	for(Position pos: gameMap.getAccessiblePositions(currentPlayerStrategyState)) {
    		distanceMap.put(pos, Integer.MAX_VALUE);
    	}
    	distanceMap.put(source, 0);

        PriorityQueue<State> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new State(source, 0, remainingGrassFields));

        Map<Position, MovesToPosition> prevMap = new LinkedHashMap<>();

        while (!priorityQueue.isEmpty()) {
            State current = priorityQueue.poll();
            Position currentPosition = current.vertex;
            if (currentPosition.equals(target)) break;

            for (Move move : Move.values()) {
				Set<Position> nextNotVisitedGrassPositions = new HashSet<>(current.notVisitedGrassPositions);
            	Position nextPosition = currentPosition.getNextPosition(move);
            	if(gameMap.isPositionOutOfMap(nextPosition, currentPlayerStrategyState)) continue;
            	
            	int weight;
            	if(gameMap.isTerrainGrass(nextPosition)) {
        			weight = gameMap.isTerrainGrass(currentPosition) ? 2: 3;
					nextNotVisitedGrassPositions.remove(nextPosition);
        		}else if(gameMap.isTerrainMountain(nextPosition)) {
        			weight = gameMap.isTerrainGrass(currentPosition) ? 3: 4;
					nextNotVisitedGrassPositions.removeAll(nextPosition.getAdjacentPositions());
        		}else continue;

                int newWeight = distanceMap.get(currentPosition) + weight;
                if (newWeight < distanceMap.get(nextPosition) || (newWeight == distanceMap.get(nextPosition) && nextNotVisitedGrassPositions.size() < current.notVisitedGrassPositions.size())) {
                	distanceMap.put(nextPosition, newWeight);
                	prevMap.put(nextPosition, new MovesToPosition(currentPosition, move, weight));
                    priorityQueue.add(new State(nextPosition, newWeight, nextNotVisitedGrassPositions)); // Add/Update priority queue
                }
            }
        }
        ArrayList<MovesToPosition> path = new ArrayList<>();
        if (prevMap.containsKey(target)) {
        	Position at = target;
        	while(true) {
        		MovesToPosition newPosition = prevMap.get(at);
            	if(newPosition == null) break;
                path.add(newPosition);
            	at = newPosition.getStartPosition();
        	}
            Collections.reverse(path); // Reverse the path to get the correct order
        }
        return path;
    }
    
    private ArrayList<MovesToPosition> getPathToClosestPositionFromSet(Set<Position> fields){
    	int minLength = Integer.MAX_VALUE;
    	ArrayList<MovesToPosition> tempList = new ArrayList<>();
    	for(Position field: fields) {
    		ArrayList<MovesToPosition> pathArrayList = calculateClosestPathDijkstra(gameMap.getMyPlayerPosition(), field);
    		int currentLength = pathArrayList.stream().mapToInt(MovesToPosition::size).sum();
    		if(minLength > currentLength) {
    			minLength = currentLength;
    			tempList = pathArrayList;
    		}
    	}
    	return tempList;
    }

    private void updateRemainingFields() {
		if(enemyCastlePossiblePositions == null){
			this.remainingGrassFields = gameMap.getGrassFields(currentPlayerStrategyState);
		}else{
			this.remainingGrassFields = enemyCastlePossiblePositions;
			isEnemyCastlePossiblePositionsApplied = true;
		}
		this.remainingMountainFields = gameMap.getMountainFields(currentPlayerStrategyState);

		//explores current field after changing strategy to exploring current half map
    	exploreField(gameMap.getMyPlayerPosition());
    }

	private boolean isMountainWorthVisiting(ArrayList<MovesToPosition> movesToMountainPosition) {
		MovesToPosition moveToMountainPosition = movesToMountainPosition.getLast();

		if(gameMap.isTerrainGrass(moveToMountainPosition.getEndPosition())) {
			pathBuilderLogger.warn("It's not a mountain, it's a grass!!!");
			return true;
		}
		//all positions that will be visited while going to mountain
		//we want to remove positions, that will be visited, when myPlayer will be near mountain,
		//so I want to remove all positions, that are near mountain and will be visited before stepping on mountain.
		//so it's at list the last position before stepping into mountain field, or + 1 corner position
		//I want this to be general, so I will save all positions
		Set<Position> allVisitedPositions = new HashSet<>();
		movesToMountainPosition.forEach(movesToPosition -> {
			allVisitedPositions.add(movesToPosition.getStartPosition());
			allVisitedPositions.add(movesToPosition.getEndPosition());
		});
		Set<Position> grassPositionsToBeVisited = new HashSet<>(gameMap.getGrassFieldsAround(moveToMountainPosition.getEndPosition(), currentPlayerStrategyState));

		//Now we will have all positions, that will be 'visited' after stepping into mountain field.
		grassPositionsToBeVisited.removeAll(allVisitedPositions);
		//Get positions that don't have to be visited
		Set<Position> positionsNoNeedToBeVisited = new HashSet<>();
		grassPositionsToBeVisited.forEach(grassPositionToBeVisited -> {
			if(!remainingGrassFields.contains(grassPositionToBeVisited)) positionsNoNeedToBeVisited.add(grassPositionToBeVisited);
		});
		//Now we will have only positions, that have to be visited
		grassPositionsToBeVisited.removeAll(positionsNoNeedToBeVisited);
        pathBuilderLogger.info("New grass positions to be visited: {}", grassPositionsToBeVisited);
		//After all we have positions, that will be visited only after stepping into mountain field, and have to be visited
		//If there are more than 2 positions, we will need more than 6 moves to visit all of them, so it's much better to visit mountain
		if(grassPositionsToBeVisited.size() > 2) {
            pathBuilderLogger.info("Size is {}, worth visiting!", grassPositionsToBeVisited.size());
			return true;
		}
		//If there are less then 2 positions, we have to check, what will be better, to make 6 moves (3 grass->mountain
		// and 3 mountain->grass), or to make less moves, just visiting only these 2 grass fields.
		Position currentPosition = moveToMountainPosition.getStartPosition();
		int pathSize = 0;
		for(Position grassField: grassPositionsToBeVisited){
			pathSize += calculateClosestPathDijkstra(currentPosition, grassField)
					.stream()
					.mapToInt(MovesToPosition::size).sum();
			currentPosition = grassField;
		}
        pathBuilderLogger.info("Size is {}, number of moves to visit positions: {}", grassPositionsToBeVisited.size(), pathSize);
		return (pathSize > 6);
	}

	private ArrayList<MovesToPosition> getClosestMountain(){
		if(remainingMountainFields.isEmpty()) return null;
		pathBuilderLogger.info("Getting closest mountain");
		ArrayList<MovesToPosition> pathToClosestMountain = new ArrayList<>();
		final int remainingMountainFieldsCount = remainingMountainFields.size();
		for(int i = 0; i < remainingMountainFieldsCount; i++){
			//find path to closest
			pathToClosestMountain = getPathToClosestPositionFromSet(remainingMountainFields);
            pathBuilderLogger.info("Check closest mountain on position: {}", pathToClosestMountain.getLast().getEndPosition());
			if(isMountainWorthVisiting(pathToClosestMountain)){
                pathBuilderLogger.info("Position {} is worth visiting!", pathToClosestMountain.getLast().getEndPosition());
				return pathToClosestMountain;
			}
            pathBuilderLogger.info("Position {} is not worth visiting!", pathToClosestMountain.getLast().getEndPosition());
			//remove this from set and find another closest
			remainingMountainFields.remove(pathToClosestMountain.getLast().getEndPosition());
			pathToClosestMountain.clear();
			pathBuilderLogger.info("Remaining mountains: {}", remainingMountainFields);
		}
		return pathToClosestMountain;
	}
	private ArrayList<MovesToPosition> getClosestGrass(){
		return remainingGrassFields.isEmpty() ? null: getPathToClosestPositionFromSet(remainingGrassFields);
	}
	private ArrayList<MovesToPosition> getClosestAccessibleField(){
		//should be checked 2 times. After game started and after coming to enemy's half
		if((currentPlayerStrategyState.isExploring() && remainingMountainFields.isEmpty() && remainingGrassFields.isEmpty())){
            pathBuilderLogger.info("Mountain and grass fields are empty, update fields, strategyState: {}", currentPlayerStrategyState);
			updateRemainingFields();
		}
		ArrayList<MovesToPosition> pathToPosition = getClosestMountain();
		if(pathToPosition == null || pathToPosition.isEmpty()){
			pathToPosition = getClosestGrass();
			if (pathToPosition == null || pathToPosition.isEmpty()){
				//should never be executed
				pathBuilderLogger.error("There is no more field that can be explored");
				throw new RuntimeException("There is no more field that can be explored");
			}
		}
		return pathToPosition;
	}


	private ArrayList<MovesToPosition> getPathToTreasure(){
		return calculateClosestPathDijkstra(gameMap.getMyPlayerPosition(), gameMap.getTreasurePosition());
	}
	private ArrayList<MovesToPosition> getPathToEnemyHalfMap(){
		ArrayList<MovesToPosition> ret = getPathToClosestPositionFromSet(gameMap.getEnemyMapEdgePositions());
		//we don't need them until we get to enemy half
		remainingMountainFields.clear();
		remainingGrassFields.clear();
		return ret;
	}
	private ArrayList<MovesToPosition> getPathToFort(){
		return calculateClosestPathDijkstra(gameMap.getMyPlayerPosition(), gameMap.getEnemyFortPosition());
	}

	public ArrayList<MovesToPosition> getPathBasedOnStrategyState(PlayerStrategyState newStrategyState){
		this.checkInit();
		currentPlayerStrategyState = newStrategyState;
		return switch (currentPlayerStrategyState) {
			case GOING_TO_TREASURE -> getPathToTreasure();
			case GOING_TO_ENEMY_HALFMAP -> getPathToEnemyHalfMap();
			case GOING_TO_FORT -> getPathToFort();
			default -> getClosestAccessibleField();
		};
	}

    public void exploreField(Position position) {
		this.checkInit();
    	if(gameMap.isTerrainGrass(position)) {
    		remainingGrassFields.remove(position);
    	}else {
    		remainingMountainFields.remove(position);
    		remainingGrassFields.removeAll(gameMap.getGrassFieldsAround(position, currentPlayerStrategyState));
    	}
		if(currentPlayerStrategyState.isExploring()){
            pathBuilderLogger.info("Remaining unexplored mountain fields: {}", remainingMountainFields);
            pathBuilderLogger.info("Remaining unexplored grass fields: {}", remainingGrassFields);
			if(currentPlayerStrategyState.isExploringEnemyHalfMap() && !isEnemyCastlePossiblePositionsApplied && enemyCastlePossiblePositions != null){
				Set<Position> leftGrassFields = gameMap.getGrassFields(currentPlayerStrategyState);
				leftGrassFields.removeAll(remainingGrassFields);
				updateRemainingFields();
				remainingGrassFields.removeAll(leftGrassFields);
			}
		}
    }

	public Set<Position> getPossibleEnemyCastlePositions(){
		this.checkInit();

		PlayerStrategyState tempState = currentPlayerStrategyState;
		if(gameMap.isEnemyPlayerOnMyHalfMap()) {
			currentPlayerStrategyState = PlayerStrategyState.GOING_TO_ENEMY_HALFMAP;
		}else{
			currentPlayerStrategyState = PlayerStrategyState.EXPLORING_ENEMY_HALFMAP;
		}
		Set<Position> ret = new HashSet<>();
		gameMap.getGrassFields(currentPlayerStrategyState).forEach(grassField -> {
			//it must be <= 9, because depending on who started the first, after my 8th move the enemy will make either 8th or 9th move
			if (calculateClosestPathDijkstra(gameMap.getEnemyPlayerPosition(), grassField).stream().mapToInt(MovesToPosition::size).sum() <= 9) {
				ret.add(grassField);
			}
		});
		if(gameMap.isEnemyPlayerOnMyHalfMap()){
			ret.removeAll(gameMap.getGrassFields(PlayerStrategyState.EXPLORING_OWN_HALFMAP));
		}
		currentPlayerStrategyState = tempState;
		enemyCastlePossiblePositions = ret;
        pathBuilderLogger.info("Enemy Position: {}", gameMap.getEnemyPlayerPosition());
        pathBuilderLogger.info("Enemy Possible Fort Positions: {}", enemyCastlePossiblePositions.toString());
		return enemyCastlePossiblePositions;
	}
}
