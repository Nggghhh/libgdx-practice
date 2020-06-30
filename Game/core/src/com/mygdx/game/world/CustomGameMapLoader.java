package com.mygdx.game.world;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.Player;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.SimplexNoise;
import com.mygdx.game.world.tiles.TileGridCell;

public class CustomGameMapLoader {
	private static Json json;
	public static final int SIZE = 100;
	public static final int LAYERS = 5;
	private static final FileHandle PATH_TO_CELLS = Gdx.files.local("world/maps/");
	private static final FileHandle PATH_TO_ENTITIES = Gdx.files.local("world/maps/entities");
	private static final FileHandle PATH_TO_GLOBAL_DATA = Gdx.files.local("world/global/");
	private static final FileHandle PATH_TO_PLAYER= Gdx.files.local("world/player/");
	private static SimplexNoise mapGen = new SimplexNoise();
	
	static {
		if(!PATH_TO_CELLS.exists()) {
			PATH_TO_CELLS.file().mkdirs();
		}
		if(!PATH_TO_ENTITIES.exists()) {
			PATH_TO_ENTITIES.file().mkdirs();
		}
		if(!PATH_TO_PLAYER.exists()) {
			PATH_TO_PLAYER.file().mkdirs();
		}
		if(!PATH_TO_GLOBAL_DATA.exists()) {
			PATH_TO_GLOBAL_DATA.file().mkdir();
		}
	}
	public static GlobalDataSnapshot loadGlobalData() {
		json = new Json();
		FileHandle data = Gdx.files.local("world/global/data.json");
		if(data.exists()) {
			GlobalDataSnapshot dataSnapshot = json.fromJson(GlobalDataSnapshot.class, data.readString());
			return dataSnapshot;
		}
		else {
			GlobalDataSnapshot dataSnapshot = new GlobalDataSnapshot();
			return dataSnapshot;
		}
			
	}
	
	public static CustomGameMapData loadMap(String name, String id, GlobalDataSnapshot dataSnapshot) {
		json = new Json();
		FileHandle cell = Gdx.files.local("world/maps/"+id+".map");
		FileHandle entityList = Gdx.files.local("world/maps/entities/"+id+".json");
		if(cell.exists() && entityList.exists()) {
			CustomGameMapData data = json.fromJson(CustomGameMapData.class, cell.readString());
			EntitySnapshot[] entities = json.fromJson(EntitySnapshot[].class, entityList.readString());
			data.entities = entities;
			return data;
		}
		else if(cell.exists() && !entityList.exists()) {
			CustomGameMapData data = json.fromJson(CustomGameMapData.class, cell.readString());
			EntitySnapshot[] entities = new EntitySnapshot[1];
			System.out.println("entities are not exists");
			data.entities = entities;
			return data;
		}
		else {
			CustomGameMapData data = generateMap(name, id);
			saveMap(data.name, data.id, data.map, new ArrayList<Entity>(), dataSnapshot);
			System.out.println("not exists");
			return data;
		}
	}
	
	public static EntitySnapshot loadPlayer() {	
		FileHandle player = Gdx.files.local("world/player/player.json");
		if(player.exists()) {	
			EntitySnapshot playerSnapshot = json.fromJson(EntitySnapshot.class, player.readString());
			return playerSnapshot;
		}
		else {
			EntitySnapshot playerSnapshot = new EntitySnapshot();
			playerSnapshot.x = 740;
			playerSnapshot.y = 740;
			playerSnapshot.HEALTH = 6;
			playerSnapshot.type = "PLAYER";
			playerSnapshot.layer = 0;
			return playerSnapshot;
		}
	}
	
	public static void saveMap(String name, String id, float[][][] map, ArrayList<Entity> entities, GlobalDataSnapshot snapshot) {
		json = new Json();
		CustomGameMapData data = new CustomGameMapData();
		FileHandle cell = Gdx.files.local("world/maps/"+id+".map");
		FileHandle entityList = Gdx.files.local("world/maps/entities/"+id+".json");
		FileHandle player = Gdx.files.local("world/player/player.json");
		FileHandle globalData = Gdx.files.local("world/global/data.json");
		data.id = id;
		data.name = name;
		data.map = map;
		
		ArrayList<EntitySnapshot> snapshots = new ArrayList<EntitySnapshot>();
		for(Entity entity : entities) {
			if(entity.getType() == EntityType.PLAYER) {
				EntitySnapshot playerSnapshot = entity.getSaveSnapshot();
				player.writeString(json.prettyPrint(playerSnapshot), false);
			}
			else {
				snapshots.add(entity.getSaveSnapshot());
			}
		}
		globalData.writeString(json.prettyPrint(snapshot), false);
		entityList.writeString(json.prettyPrint(snapshots), false);
		cell.writeString(json.prettyPrint(data), false);
	}
	
	public static CustomGameMapData generateMap(String name, String id) {
		CustomGameMapData mapData = new CustomGameMapData();
		mapGen = new SimplexNoise();
		
		int seed = -1;
		int octave = 3;
		if(seed < 0)
			seed = RandomNumGen.getRandomNumberInRange(1000, 9999);
		
		mapData.id = id;
		mapData.name = name;
		mapData.map = mapGen.generateBaseTerrain(mapGen.CreateRoundedArray(SIZE, SIZE, octave, 5, seed));
		mapData.map = crop(mapData.map);
		mapData.map = generateBridges(mapData.map);
		EntitySnapshot[] entityList = generateForest(mapData);
//		mapData.map = generateDiversity(mapData.map, seed, octave);
		mapData.entities = entityList;
		
		return mapData;
	}
	
	public static EntitySnapshot[] generateForest(CustomGameMapData mapData) {
		EntitySnapshot[] entities = new EntitySnapshot[500];
		float[][][] map = mapData.map;
		int index = 0;
		
		for(int row = 0; row<map.length; row++) {
			for(int col = 0; col<map[0].length; col++) {
				int r = RandomNumGen.getRandomNumberInRange(0, 40);
				int x = (row*16)+8;
				int y = (col*16)+8;
				float cell1 = map[row][col][0];
				float cell2 = map[row][col][1];
				
				if(index == 499) {
					return entities;
				}
				if(r > 37) {
					if(cell1 == 4 && cell2 == 0) {
						entities[index] = new EntitySnapshot();
						if(r == 38)
							entities[index].type = "BUSH";
						else if(r == 39)
							entities[index].type = "BIG_TREE";
						else
							entities[index].type = "SMALL_BUSH";
						entities[index].x = x;
						entities[index].y = y;
						entities[index].HEALTH = 6;
						entities[index].layer = 0;
						index++;
					}
				}
			}
		}
		return entities;
	}
	
	
	public static float[][][] generateDiversity(float[][][] map, int seed, int octave) {
		int newSeed;
		try {
			String transform = Integer.toString(seed);
			transform = transform+transform.charAt(3);
			newSeed = Integer.parseInt(transform);
		}
		catch(Exception e) {
			newSeed = RandomNumGen.getRandomNumberInRange(0, 1000);
		}
		float [][][] overlapMap = mapGen.generateBaseTerrain(mapGen.CreateRoundedArray(SIZE, SIZE, octave, 5, newSeed));
		
		for(int row = 0; row<map.length; row++) {
			for(int col = 0; col<map[0].length; col++) {
				if(map[row][col][0] == 4 && overlapMap[row][col][0] == 4) {
					map[row][col][0] = 10;
				}
				if(map[row][col][1] == 5 && overlapMap[row][col][1] == 5) {
					map[row][col][1] = 11;
				}
			}
		}
		return map;
	}
	
	public static float[][][] crop(float[][][] map) {
		for(int row = 0; row<6; row++) {
			for(int col = 0; col<map[0].length; col++) {
				map[row][col][0] = 1;
				map[col][row][0] = 1;
				map[row][col][1] = 0;
				map[col][row][1] = 0;
			}
		}
		for(int row = map.length-1; row>map.length-7; row--) {
			for(int col = map[0].length-1; col>0; col--) {
				map[row][col][0] = 1;
				map[col][row][0] = 1;
				map[row][col][1] = 0;
				map[col][row][1] = 0;
			}
		}
		return map;
	}
	
	public static float[][][] generateBridges(float[][][] map) {
		for(int row = 5; row<6; row++) {
			for(int col = 5; col<map[0].length-6; col++) {
				map[row][col][0] = 12;
				map[col][row][0] = 12;
			}
		}
		for(int row = map.length-6; row>map.length-7; row--) {
			for(int col = map[0].length-6; col>4; col--) {
				map[row][col][0] = 12;
				map[col][row][0] = 12;
			}
		}
		for(int col = 5; col>-1; col--) {
			map[map.length/2][col][0] = 12;
			map[(map.length/2)-1][col][0] = 12;
			map[(map.length/2)+1][col][0] = 12;
			
			map[col][map.length/2][0] = 12;
			map[col][(map.length/2)-1][0] = 12;
			map[col][(map.length/2)+1][0] = 12;
		}
		
		for(int col = map.length-5; col<map.length; col++) {
			map[map.length/2][col][0] = 12;
			map[(map.length/2)-1][col][0] = 12;
			map[(map.length/2)+1][col][0] = 12;
			
			map[col][map.length/2][0] = 12;
			map[col][(map.length/2)-1][0] = 12;
			map[col][(map.length/2)+1][0] = 12;
		}
		return map;
	}
}
