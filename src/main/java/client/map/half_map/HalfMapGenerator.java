package client.map.half_map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.Position;


class HalfMapGenerator{
	private static final Logger halfMapGeneratorLogger = LoggerFactory.getLogger(HalfMapGenerator.class.getName());
	
	private static final float CHANCE_MOUNTAIN = 0.1f;
	private static final float CHANCE_WATER = 0.14f;
	private static final float CHANCE_GRASS = 0.48f;
	
	private static final int CHANCE_MULTIPLIER = 139;
	private static final int RANGE_GRASS = Math.round(CHANCE_GRASS * CHANCE_MULTIPLIER);
	private static final int RANGE_MOUNTAIN = RANGE_GRASS + Math.round(CHANCE_MOUNTAIN * CHANCE_MULTIPLIER);

	/*
	 * I could find each time empty slots inside my halfMap,
	 * but map generation algorithm should work as fast as possible, so I need
	 * here 2 additional variables, that will store all empty fields, and water fields on edges
	 * what will increase used memory, but reduce complexity and generating time
	 * */
	private final ArrayList<Position> emptyPositionList;
	
	private Position getRandomEmptyPosition() {
		Random random = new Random();
		int index = random.nextInt(this.emptyPositionList.size());
		Position retPos = this.emptyPositionList.get(index);
		this.emptyPositionList.remove(index);
		return retPos;
	}
	
	private void clearHalfMapData(HalfMap halfMap) {
		for(int y = 0; y<HalfMap.HEIGHT; y++) {
			for(int x = 0; x<HalfMap.WIDTH; x++) {
				Position pos = new Position(x, y);
				halfMap.setTerrainEmpty(pos);
				this.emptyPositionList.add(pos);
			}
		}
		halfMapGeneratorLogger.info("All half map data renewed!");
	}

	private void generateMinRequirementOfHalfMap(HalfMap halfMap) {
		//Fill with water fields
		for(int i = 0; i < (int)(CHANCE_WATER * HalfMap.TOTAL); i++) {
			halfMap.setTerrainWater(this.getRandomEmptyPosition());
		}
		//Fill with grass fields
		for(int i = 0; i < (int)(CHANCE_GRASS * HalfMap.TOTAL); i++) {
			halfMap.setTerrainGrass(this.getRandomEmptyPosition());
		}
		//Fill with mountain fields
		for(int i = 0; i < (int)(CHANCE_MOUNTAIN * HalfMap.TOTAL); i++) {
			halfMap.setTerrainMountain(this.getRandomEmptyPosition());
		}
		halfMapGeneratorLogger.info("The first part successfully generated, 14 fields remaining!");
	}
	

	private void generateRestPartOfHalfMap(HalfMap halfMap) {
		Random rnd = new Random();
		int genNumber;
		while(!this.emptyPositionList.isEmpty()){
			genNumber = rnd.nextInt(100);
			
			if(genNumber < RANGE_GRASS) {
				halfMap.setTerrainGrass(this.getRandomEmptyPosition());
			}
			else if(genNumber < RANGE_MOUNTAIN) {
				halfMap.setTerrainMountain(this.getRandomEmptyPosition());
			}
			else {
				halfMap.setTerrainWater(this.getRandomEmptyPosition());
			}
		}
		halfMapGeneratorLogger.info("The second map's part successfully generated!");
	}
	
	
	private void generateFortPosition(HalfMap halfMap) {
		Position ranPos = halfMap.getRandomGrassPosition();
		halfMap.setFort(ranPos);
        halfMapGeneratorLogger.info("Fort successfully generated! Position: {}", ranPos);
	}


	public HalfMapGenerator() {
		this.emptyPositionList = new ArrayList<>();
	}

	public void generateHalfMap(HalfMap halfMap){		
		this.clearHalfMapData(halfMap);
		this.generateMinRequirementOfHalfMap(halfMap);
		/*
		 * Now we have 5 mountain 7 water, and 24 grass fields. But we still have 28% map,
		 * that can be generated as we want. So I will fill remaining 14 fields with random
		 * terrains using this proportion: Water:Mountain:Grass = 10:14:48. I would say that 10x + 14x + 48x = 100 =>
		 * x = 1.39 -> Mountain = 14%, Water = 19%, Grass = 67%;
		 * */
		this.generateRestPartOfHalfMap(halfMap);		
		this.generateFortPosition(halfMap);
        halfMapGeneratorLogger.info("Map is successfully generated!\n{}", halfMap);
	}	
}
