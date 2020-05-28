package com.mygdx.game.world;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.SimplexNoise;

public class CustomGameMapLoader {
	private static Json json;
	public static final int SIZE = 100;
	public static final int LAYERS = 5;
	private static final FileHandle PATH_TO_CELLS = Gdx.files.local("world/maps/");
	private static final FileHandle PATH_TO_ENTITIES = Gdx.files.local("world/maps/entities");
	private static final FileHandle PATH_TO_MAP = Gdx.files.local("world/global/");
	private static SimplexNoise mapGen = new SimplexNoise();
	
	public static CustomGameMapData loadMap(String name, int id) {
		json = new Json();
		if(!PATH_TO_CELLS.exists()) {
			PATH_TO_CELLS.file().mkdirs();
		}
		if(!PATH_TO_ENTITIES.exists()) {
			PATH_TO_ENTITIES.file().mkdirs();
		}
		FileHandle cell = Gdx.files.internal("world/maps/"+id+".map");
		FileHandle entityList = Gdx.files.internal("world/maps/entities/"+id+".json");
		if(cell.exists() && entityList.exists()) {
			CustomGameMapData data = json.fromJson(CustomGameMapData.class, cell.readString());
			EntitySnapshot[] entities = json.fromJson(EntitySnapshot[].class, entityList.readString());
			data.entities = entities;
			return data;
		}
		else if(cell.exists() && !entityList.exists()) {
			CustomGameMapData data = json.fromJson(CustomGameMapData.class, cell.readString());
			EntitySnapshot[] entities = new EntitySnapshot[1];
			data.entities = entities;
			return data;
		}
		else {
			CustomGameMapData data = generateMap(name, id);
			saveMap(data.name, data.id, data.map);
			System.out.println("not exists");
			return data;
		}
	}
	
	public static void saveMap(String name, int id, float[][][] map) {
		json = new Json();
		CustomGameMapData data = new CustomGameMapData();
		FileHandle cell = Gdx.files.local("world/maps/"+id+".map");
		data.id = id;
		data.name = name;
		data.map = map;
		
		if(!PATH_TO_CELLS.exists()) {
			PATH_TO_CELLS.file().mkdirs();
		}
		
		cell.writeString(json.prettyPrint(data), false);
		cell = null;
		data = null;
	}
	
	public static CustomGameMapData generateMap(String name, int id) {
		CustomGameMapData mapData = new CustomGameMapData();
		mapGen = new SimplexNoise();
		
		int seed = 1000;
		int octave = 3;
		if(seed < 0)
			seed = RandomNumGen.getRandomNumberInRange(0, 1000);
		
		mapData.id = id;
		mapData.name = name;
		mapData.map = mapGen.generateBaseTerrain(mapGen.CreateRoundedArray(SIZE, SIZE, octave, 5, seed));
		
		return mapData;
	}
}
