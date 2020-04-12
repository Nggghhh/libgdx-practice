package com.mygdx.game.world;

import java.util.Arrays;
import java.util.logging.FileHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.tools.OrthogonalTiledMapRenderer;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.SimplexNoise;
import com.mygdx.game.world.tiles.CustomTileType;

public class TiledGameMap extends GameMap {

	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	StaticTiledMapTile tile;
	SimplexNoise simp;
	
	float[][] map;
	
	public TiledGameMap () {
		init("Map");
		simp = new SimplexNoise();
		
//		for(int row = 0; row < map.length; row++)
//			for(int col = 0; col < map[row].length; col++)
//				map[row][col] = 0;
//		for(int i = 0; i < 20; i++) {
//			map[RandomNumGen.getRandomNumberInRange(0, 49)][RandomNumGen.getRandomNumberInRange(0, 49)] = 5;
//		}
//		
//		for(int row = 0; row < map.length; row++)
//			for(int col = 0; col < map[row].length; col++) {
//				if(map[row][col] == 5) {
//					if(row+1 > 0 && row+1 < map.length && col > 0 && col < map.length) map[row+1][col] = 4;
//					if(row-1 > 0 && row-1 < map.length && col > 0 && col < map.length) map[row-1][col] = 4;
//					if(row > 0 && row < map.length && col+1 > 0 && col+1 < map.length) map[row][col+1] = 4;
//					if(row > 0 && row < map.length && col-1 > 0 && col-1 < map.length) map[row][col-1] = 4;
//				}
//			}
//		for(int row = 0; row < map.length; row++)
//			for(int col = 0; col < map[row].length; col++)
//				if(map[row][col] == 4 && map[row][col] != 5) {
//					if(row+1 > 0 && row+1 < map.length && col > 0 && col < map.length && map[row+1][col] != 5) map[row+1][col] = 3;
//					if(row-1 > 0 && row-1 < map.length && col > 0 && col < map.length && map[row-1][col] != 5) map[row-1][col] = 3;
//					if(row > 0 && row < map.length && col+1 > 0 && col+1 < map.length && map[row][col+1] != 5) map[row][col+1] = 3;
//					if(row > 0 && row < map.length && col-1 > 0 && col-1 < map.length && map[row][col-1] != 5) map[row][col-1] = 3;
//				}
//		for(int row = 0; row < map.length; row++)
//			for(int col = 0; col < map[row].length; col++)
//				if(map[row][col] == 3 && map[row][col] != 4) {
//					if(row+1 > 0 && row+1 < map.length && col > 0 && col < map.length && map[row+1][col] != 4) map[row+1][col] = 2;
//					if(row-1 > 0 && row-1 < map.length && col > 0 && col < map.length && map[row-1][col] != 4) map[row-1][col] = 2;
//					if(row > 0 && row < map.length && col+1 > 0 && col+1 < map.length && map[row][col+1] != 4) map[row][col+1] = 2;
//					if(row > 0 && row < map.length && col-1 > 0 && col-1 < map.length && map[row][col-1] != 4) map[row][col-1] = 2;
//				}
		
		
		for(int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[row].length; col++) {
				System.out.print((int)map[row][col]+" ");
			}
			System.out.println();
		}
		
	}
	
	public static boolean find(int[][] map, int target) {
		for(int row = 0; row < map.length; row++)
			for(int col = 0; col < map[row].length; col++)
				if(row > 0 && row < map.length && col-1 > 0 && col-1 < map.length)
					return true;
		return false;
	}
	
	@Override
	public void init(String name) {
		tiledMap = new TmxMapLoader().load(name+".tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}
	
	@Override
	public void render(OrthographicCamera camera, SpriteBatch batch) {
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		batch.begin();
		super.render(camera, batch);
		batch.end();
	}

	@Override
	public void update(OrthographicCamera camera, float delta) {
		super.update(camera, delta);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		tiledMapRenderer.dispose();
	}

	@Override
	public CustomTileType getTileTypeByCoordinate(int layer, int col, int row) {

		return null;
	}
	
	@Override
	public TileType setTile(int layer, int col, int row, int id) {
		Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(col, row);
		if(cell != null) {
			cell.setTile(tiledMap.getTileSets().getTile(id));
			System.out.println("epic");
		}
		return null;
	}


	@Override
	public int getWidth() {
		return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
	}

	@Override
	public int getHeight() {
		return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();
	}

	@Override
	public int getLayers() {
		return tiledMap.getLayers().getCount();
	}
}
