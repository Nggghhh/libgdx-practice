package com.mygdx.game.world;

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

public class TiledGameMap extends GameMap {

	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	StaticTiledMapTile tile;
	
	public TiledGameMap () {
		init("Map");
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
	public TileType getTileTypeByCoordinate(int layer, int col, int row) {
		Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(col, row);
		if(cell != null) {
			TiledMapTile tile = cell.getTile();
			
			if(tile != null) {
				int id = tile.getId();
				return TileType.getTileTypeById(id);
			}
		}
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
