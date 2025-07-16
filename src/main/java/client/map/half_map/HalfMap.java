package client.map.half_map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.Position;
import client.map.Terrain;

public class HalfMap {
	private static final Logger halfMapLogger = LoggerFactory.getLogger(HalfMap.class);
	public static final int HEIGHT = 5;
	public static final int WIDTH = 10;
	public static final int TOTAL = HEIGHT * WIDTH;
	private final Notification notification;
	
	private final LinkedHashMap<Position, Terrain> terrains;
	private Position fortPosition;
	private boolean isGenerated = false;
	
	private void setTerrain(Position pos, Terrain terrain) {
		this.terrains.put(pos, terrain);
	}

	private Set<Position> getGrassFields(){
		Set<Position> retSet = new HashSet<>();
		terrains.forEach(((position, terrain) -> {
			if(terrain == Terrain.GRASS) retSet.add(position);
		}));
		return retSet;
	}
	
	void setTerrainEmpty(Position pos) {
		this.setTerrain(pos, Terrain.EMPTY);
	}
	
	void setTerrainWater(Position pos) {
		this.setTerrain(pos, Terrain.WATER);
	}
	
	void setTerrainGrass(Position pos) {
		this.setTerrain(pos, Terrain.GRASS);
	}
	
	void setTerrainMountain(Position pos) {
		this.setTerrain(pos, Terrain.MOUNTAIN);
	}
	
	void setFort(Position pos) {
		this.fortPosition = pos;
	}

	public boolean isPositionWater(Position pos) {
		return terrains.get(pos) == Terrain.WATER;
	}
	
	boolean isPositionOutOfMap(Position pos) {
		return (pos.x() < 0 || pos.x() >= WIDTH || pos.y() < 0 || pos.y() >= HEIGHT);
	}
	
	boolean isPositionAccessible(Position pos) {
		return terrains.get(pos).isAccessible();
	}

	Position getRandomGrassPosition() {
		List<Position> list = new ArrayList<>(getGrassFields());
		Random random = new Random();
		int randomIndex = random.nextInt(list.size());
		return list.get(randomIndex);
	}

	Set<Position> getAccessiblePositions() {
		Set<Position> retSet = new HashSet<>();
		terrains.forEach((pos, terrain) -> {
			if(terrain.isAccessible()) retSet.add(pos);
		});
		return retSet;
	}

	Position getFirstAccessiblePosition() {
		for(Entry<Position, Terrain> entry : terrains.entrySet()) {
			if(entry.getValue().isAccessible()) return entry.getKey();
		}
		return null;
	}

	public HalfMap() {
		this.terrains = new LinkedHashMap<>();
		this.fortPosition = new Position();
		notification = new Notification();
	}

	public HalfMap(LinkedHashMap<Position, Terrain> terrains) {
		this.terrains = terrains;
		this.fortPosition = new Position();
		isGenerated = true;
		notification = new Notification();
	}
	
	public void generate() {
		if(isGenerated) return;
		HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
		HalfMapValidator halfMapValidator = new HalfMapValidator();
		while(true){
			halfMapGenerator.generateHalfMap(this);
			String errorMessage = halfMapValidator.validateHalfMap(this);
			if(errorMessage.isEmpty()){
				if(notification.hasError()) System.out.println(notification.errorInformation());
				break;
			}
			else {
				halfMapLogger.warn(errorMessage + this);
				notification.addError(errorMessage);
			}
		}
		isGenerated = true;
		halfMapLogger.info("Map is valid!" + this);
		System.out.println("Successfully generated valid halfmap!");

	}
	
	public boolean isEmpty() {
		return terrains.isEmpty();
	}
	
	public Position getFortPosition() {
		return fortPosition;
	}
	
	public LinkedHashMap<Position, Terrain> getHalfMapNodes() {
		return this.terrains;
	}
	
	@Override
    public String toString() {
		StringBuilder retString = new StringBuilder("\n");
		String separatorString = ("\n-|" + ("-".repeat(9) + "|").repeat(WIDTH) + "\n");
		for(int x = 0; x < WIDTH; x++) {
			if(x == 0) retString.append(" |");
			retString.append(" ".repeat(4)).append(String.format("%-5d|", x));
		}
		retString.append(separatorString);
		this.terrains.forEach(((position, terrain) -> {
			if(position.x() == 0) retString.append(String.format("%d|", position.y()));
			retString.append(String.format("%14s|", terrain));
			if(position.x() == WIDTH - 1) retString.append(separatorString);
		}));
		retString.append(String.format("Fort Position: %s\n", fortPosition));
        return retString.toString();
    }
}
