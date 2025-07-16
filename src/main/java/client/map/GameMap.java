package client.map;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import client.map.half_map.HalfMap;
import client.player.PlayerStrategyState;

public class GameMap {
	private record MapBorders(Position start, Position end) {
		public MapBorders() { this(new Position(), new Position()); }
		boolean isPositionOutOfMap(Position pos) {
			return(
				pos.x() < start.x() ||
				pos.x() > end.x() ||
				pos.y() < start.y() ||
				pos.y() > end.y()
			);
		}
	}

	private LinkedHashMap<Position, Terrain> gameMapNodes;

	private Position treasurePosition;
	private boolean init;

	private MapBorders myHalfMapBorders;
	private MapBorders enemyHalfMapBorders;
	private MapBorders mapBorders;

	private Position myPlayerPosition;
	private Position myFortPosition;

	private Position enemyPlayerPosition;
	private Position enemyFortPosition;

	private void checkInit(){
		if(!init){
			throw new RuntimeException("Map is not initialized");
		}
	}

	private boolean isMyFortInLeftTopPart(){
		return (myFortPosition.x() < HalfMap.WIDTH && myFortPosition.y() < HalfMap.HEIGHT);
	}

	private MapBorders getUpperLeftHalfMapBorder(){
		return new MapBorders(new Position(0,0), new Position(HalfMap.WIDTH - 1, HalfMap.HEIGHT - 1));
	}

	private MapBorders getLowerOrRightHalfMapBorder(){
		return mapBorders.end().x() == mapBorders.end().y()?
			new MapBorders(new Position(0, HalfMap.HEIGHT), new Position(mapBorders.end().x(), mapBorders.end().y())) :
				new MapBorders(new Position(HalfMap.WIDTH, 0), new Position(mapBorders.end().x(), mapBorders.end().y()));

	}

	private void setBorders() {
		if(isMyFortInLeftTopPart()) {
			myHalfMapBorders = getUpperLeftHalfMapBorder();
			enemyHalfMapBorders = getLowerOrRightHalfMapBorder();
		}else{
			myHalfMapBorders = getLowerOrRightHalfMapBorder();
			enemyHalfMapBorders = getUpperLeftHalfMapBorder();
		}
	}

	private void initialize(GameMap newGameMap) {
		if(newGameMap.gameMapNodes.size() != 100 || init) return;
		this.init = true;
		Comparator<Position> positionComparator = Comparator
			    .comparingInt(Position::y) // primary sorting by y
			    .thenComparingInt(Position::x); // secondary sorting by x
		this.gameMapNodes = newGameMap.gameMapNodes.entrySet().stream()
			    .sorted(Map.Entry.comparingByKey(positionComparator))
			    .collect(Collectors.toMap(
			        Map.Entry::getKey,
			        Map.Entry::getValue,
			        (e1, e2) -> e1, // handle duplicates if any
			        LinkedHashMap::new // maintain order
			    ));
		Position maxPos = newGameMap.gameMapNodes.entrySet().stream()
			    .max(Map.Entry.comparingByKey(positionComparator))
			    .orElse(null).getKey();
		this.mapBorders = new MapBorders(new Position(0,0), maxPos);
		this.myFortPosition = newGameMap.myFortPosition;

		this.setBorders();
	}

	private MapBorders getCurrentHalfMapBorders(PlayerStrategyState state) {
		if(state.isOnOwnHalfMap()) {
			return myHalfMapBorders;
		}else if(state.isOnEnemyHalfMap()) {
			return enemyHalfMapBorders;
		}else {
			return mapBorders;
		}
	}
	
	private Set<Position> getCurrentTypePositions(PlayerStrategyState state, Terrain terrain){
		this.checkInit();
		Set<Position> retSet = new HashSet<>();
		Position curPosition;
		MapBorders currentHalfMapBorders = getCurrentHalfMapBorders(state);
		
		for(int y = currentHalfMapBorders.start().y(); y <= currentHalfMapBorders.end().y(); y++) {
			for(int x = currentHalfMapBorders.start().x(); x <= currentHalfMapBorders.end().x(); x++) {
				curPosition = new Position(x, y);
				if(gameMapNodes.get(curPosition) == terrain) {
					retSet.add(curPosition);
				}
			}
		}
		return retSet;
	}

	public GameMap() {
		gameMapNodes = new LinkedHashMap<>();
		init = false;
		treasurePosition = new Position();

		myHalfMapBorders = new MapBorders();
		enemyHalfMapBorders = new MapBorders();
		mapBorders = new MapBorders();

		myPlayerPosition = new Position();
		myFortPosition = new Position();

		enemyPlayerPosition = new Position();
		enemyFortPosition = new Position();
	}

	public GameMap(HalfMap halfMap) {
		this.gameMapNodes = halfMap.getHalfMapNodes();
		this.treasurePosition = Position.getUninitializedPosition();

		this.myPlayerPosition = halfMap.getFortPosition();
		this.myFortPosition = halfMap.getFortPosition();

		this.enemyPlayerPosition = Position.getUninitializedPosition();
		this.enemyFortPosition = Position.getUninitializedPosition();
		this.mapBorders = new MapBorders(new Position(), new Position(9, 4));
	}
	
	public GameMap(LinkedHashMap<Position, Terrain> gameMapNodes, Position treasurePosition, Position myPlayerPosition, Position myFortPosition, Position enemyPlayerPosition, Position enemyFortPosition) {
		this.gameMapNodes = gameMapNodes;
		this.treasurePosition = treasurePosition;

		this.myPlayerPosition = myPlayerPosition;
		this.myFortPosition = myFortPosition;

		this.enemyPlayerPosition = enemyPlayerPosition;
		this.enemyFortPosition = enemyFortPosition;
	}

	public GameMap(GameMap newGameMap) {
		init = newGameMap.init;
		this.gameMapNodes = newGameMap.gameMapNodes;
		this.treasurePosition = newGameMap.treasurePosition;

		this.myPlayerPosition = newGameMap.myPlayerPosition;
		this.myFortPosition = newGameMap.myFortPosition;

		this.enemyPlayerPosition = newGameMap.enemyPlayerPosition;
		this.enemyFortPosition = newGameMap.enemyFortPosition;
		this.myHalfMapBorders = newGameMap.myHalfMapBorders;
		this.mapBorders = newGameMap.mapBorders;
		this.enemyHalfMapBorders = newGameMap.enemyHalfMapBorders;
	}
	
	public void update(GameMap newGameMap) {
		initialize(newGameMap);
		this.treasurePosition = newGameMap.treasurePosition;
		this.myPlayerPosition = newGameMap.myPlayerPosition;
		this.enemyPlayerPosition = newGameMap.enemyPlayerPosition;
		this.enemyFortPosition = newGameMap.enemyFortPosition;
	}
	
	public boolean isPositionOutOfMap(Position pos, PlayerStrategyState state) {
		this.checkInit();
		if(state.isOnOwnHalfMap()) {
			return myHalfMapBorders.isPositionOutOfMap(pos);
		}else if(state.isOnEnemyHalfMap()) {
			return enemyHalfMapBorders.isPositionOutOfMap(pos);
		}else {
			return mapBorders.isPositionOutOfMap(pos);
		}
	}
	
	public Set<Position> getGrassFields(PlayerStrategyState state){
		return getCurrentTypePositions(state, Terrain.GRASS);
	}
	
	public Set<Position> getMountainFields(PlayerStrategyState state){
		return getCurrentTypePositions(state, Terrain.MOUNTAIN);
	}
	
	public Set<Position> getAccessiblePositions(PlayerStrategyState state){
		this.checkInit();
		Set<Position> retSet = new HashSet<>();
		Position curPosition;
		MapBorders currentHalfMapBorders = getCurrentHalfMapBorders(state);

		for(int y = currentHalfMapBorders.start().y(); y <= currentHalfMapBorders.end().y(); y++) {
			for(int x = currentHalfMapBorders.start().x(); x <= currentHalfMapBorders.end().x(); x++) {
				curPosition = new Position(x, y);
				if(gameMapNodes.get(curPosition).isAccessible()) {
					retSet.add(curPosition);
				}
			}
		}
		return retSet;
	}
	
	public Set<Position> getGrassFieldsAround(Position pos, PlayerStrategyState state){
		this.checkInit();
		Set<Position> adjacentPositions = pos.getAdjacentPositions();
		adjacentPositions.removeIf(position -> (isPositionOutOfMap(position, state) || !isTerrainGrass(position)));
		return adjacentPositions;
	}
	
	public boolean isTerrainGrass(Position pos) {
		this.checkInit();
		return (gameMapNodes.get(pos) == Terrain.GRASS);
	}
	
	public boolean isTerrainMountain(Position pos) {
		this.checkInit();
		return (gameMapNodes.get(pos) == Terrain.MOUNTAIN);
	}
	
	public boolean isInit() {
		return this.init;
	}
	
	public boolean isFortFound() {
		this.checkInit();
		return enemyFortPosition.isInit();
	}
	
	public boolean isTreasureFound() {
		this.checkInit();
		return treasurePosition.isInit();
	}
	
	public Position getTreasurePosition() {
		return treasurePosition;
	}
	
	public Position getMyPlayerPosition() {
		return myPlayerPosition;
	}
	
	public Position getFortPosition() {
		return myFortPosition;
	}

	public Position getEnemyPlayerPosition() {
		return enemyPlayerPosition;
	}
	
	public Position getEnemyFortPosition(){
		return enemyFortPosition;
	}
	
	/*public boolean isPlayerOnTreasure() {
		return treasurePosition.equals(myPlayerPosition);
	}*/
	
	public Set<Position> getEnemyMapEdgePositions(){
		this.checkInit();
		Set<Position> set = new HashSet<>();
		Position enemyPlayerHalfMapStartPosition = enemyHalfMapBorders.start();
		Position enemyPlayerHalfMapEndPosition = enemyHalfMapBorders.end();
		Position curPosition;
		for(int x = enemyPlayerHalfMapStartPosition.x(); x <= enemyPlayerHalfMapEndPosition.x(); x++) {//upper x
			curPosition = new Position(x, enemyPlayerHalfMapStartPosition.y());
			if(gameMapNodes.get(curPosition).isAccessible()) set.add(curPosition);
		}
		for(int x = enemyPlayerHalfMapStartPosition.x(); x <= enemyPlayerHalfMapEndPosition.x(); x++) { //lower x
			curPosition = new Position(x, enemyPlayerHalfMapEndPosition.y());
			if(gameMapNodes.get(curPosition).isAccessible()) set.add(curPosition);
		}
		
		for(int y = enemyPlayerHalfMapStartPosition.y() + 1; y < enemyPlayerHalfMapEndPosition.y(); y++) { //left y
			curPosition = new Position(enemyPlayerHalfMapStartPosition.x(), y);
			if(gameMapNodes.get(curPosition).isAccessible()) set.add(curPosition);
		}
		for(int y = enemyPlayerHalfMapStartPosition.y() + 1; y < enemyPlayerHalfMapEndPosition.y(); y++) { //left y
			curPosition = new Position(enemyPlayerHalfMapEndPosition.x(), y);
			if(gameMapNodes.get(curPosition).isAccessible()) set.add(curPosition);
		}
		return set;
	}

	public boolean isPlayerOnEnemyFort(){
		return enemyFortPosition.equals(myPlayerPosition);
	}
	
	public boolean isMyPlayerOnEnemyHalfMap() {
		this.checkInit();
		return !enemyHalfMapBorders.isPositionOutOfMap(myPlayerPosition);
	}

	public boolean isEnemyPlayerOnMyHalfMap() {
		this.checkInit();
		return !myHalfMapBorders.isPositionOutOfMap(enemyPlayerPosition);
	}

	public Map<Position, Terrain> getGameMapNodes() {
		return Collections.unmodifiableMap(gameMapNodes);
	}

	public Position getDimensions() {
		return mapBorders.end();
	}

	public boolean isEmpty(){
		return gameMapNodes.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		GameMap gameMap = (GameMap) o;
		return  Objects.equals(gameMapNodes, gameMap.gameMapNodes) && Objects.equals(treasurePosition, gameMap.treasurePosition) && Objects.equals(myPlayerPosition, gameMap.myPlayerPosition) && Objects.equals(myFortPosition, gameMap.myFortPosition) && Objects.equals(enemyPlayerPosition, gameMap.enemyPlayerPosition) && Objects.equals(enemyFortPosition, gameMap.enemyFortPosition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameMapNodes, treasurePosition, init, myHalfMapBorders, enemyHalfMapBorders, mapBorders, myPlayerPosition, myFortPosition, enemyPlayerPosition, enemyFortPosition);
	}

	@Override
	public String toString() {
		StringBuilder retString = new StringBuilder("\n");
		String separatorString = ("\n-|" + ("-".repeat(9) + "|").repeat(mapBorders.end.x() + 1) + "\n");
		for(int x = 0; x <= mapBorders.end.x(); x++) {
			if(x == 0) retString.append(" |");
			retString.append(" ".repeat(4)).append(String.format("%-5d|", x));
		}
		retString.append(separatorString);
		this.gameMapNodes.forEach(((position, terrain) -> {
			if(position.x() == 0) retString.append(String.format("%d|", position.y()));
			retString.append(String.format("%14s|", terrain));
			if(position.x() == mapBorders.end.x()) retString.append(separatorString);
		}));
		retString.append(String.format("My fort: %s;\nMy player: %s", this.myFortPosition, this.myPlayerPosition));
        return retString.toString();
	}
}
