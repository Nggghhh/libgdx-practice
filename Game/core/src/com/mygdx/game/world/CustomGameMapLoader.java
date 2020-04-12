package com.mygdx.game.world;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.tools.SimplexNoise;

public class CustomGameMapLoader {
	private static Json json = new Json();
	public static final int SIZE = 100;
	public static final int LAYERS = 5;
	private static final FileHandle PATH_TO_CELLS = Gdx.files.local("world/maps/");
	private static final FileHandle PATH_TO_MAP = Gdx.files.local("world/global/");
	private static SimplexNoise mapGen = new SimplexNoise();
	
	public static CustomGameMapData loadMap(String name, int id) {
		if(!PATH_TO_CELLS.exists()) {
			PATH_TO_CELLS.file().mkdirs();
		}
		FileHandle cell = Gdx.files.internal("world/maps/"+id+".map");
		if(cell.exists()) {
			CustomGameMapData data = json.fromJson(CustomGameMapData.class, cell.readString());
			return data;
		}
		else {
			CustomGameMapData data = generateMap(name, id);
			saveMap(data.name, data.id, data.map);
			return data;
		}
	}
	
	public static void saveMap(String name, int id, float[][][] map) {
		CustomGameMapData data = new CustomGameMapData();
		FileHandle cell = Gdx.files.local("world/maps/"+id+".map");
		data.id = id;
		data.name = name;
		data.map = map;
		
		if(!PATH_TO_CELLS.exists()) {
			PATH_TO_CELLS.file().mkdirs();
		}
		
		cell.writeString(json.prettyPrint(data), false);
	}
	
	public static CustomGameMapData generateMap(String name, int id) {
		CustomGameMapData mapData = new CustomGameMapData();
		mapGen = new SimplexNoise();
		float[][] mapToLoad;
		
		int seed = 1000;
		int octave = 3;
//		Random seed = new Random();
		
		mapData.id = id;
		mapData.name = name;
		mapData.map = mapGen.generateBaseTerrain(mapGen.CreateRoundedArray(SIZE, SIZE, octave, 1, seed));
		
		return mapData;
	}
}
