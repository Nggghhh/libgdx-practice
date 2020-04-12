package com.mygdx.game.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.SimplexNoise;
import com.mygdx.game.world.tiles.Air;
import com.mygdx.game.world.tiles.CustomTileType;
import com.mygdx.game.world.tiles.GreenGrass;
import com.mygdx.game.world.tiles.Sand;
import com.mygdx.game.world.tiles.ShallowWater;
import com.mygdx.game.world.tiles.TileGridCell;
import com.mygdx.game.world.tiles.TileTextureManager;

public class CustomGameMap extends GameMap {
	StaticTiledMapTile tile;

	String name;
	int id;
	float[][][] map;
	TileGridCell[][][] arrayOfCells;
	private TileTextureManager textures;

	public CustomGameMap() {
		CustomGameMapData data = CustomGameMapLoader.loadMap("bruhlands", 0);
		this.id = data.id;
		this.name = data.name;
		this.map = data.map;
		textures = new TileTextureManager();
		this.arrayOfCells = new TileGridCell[map.length][map[0].length][map[0][0].length];
		for(int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[row].length; col++) {
				System.out.print((int)map[row][col][1]+" ");
			}
			System.out.println();
		}

		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++) {
					arrayOfCells[row][col][layer] = new TileGridCell(this, row, col, layer, (int)map[row][col][layer]);
				}
	}

	@Override
	public void render(OrthographicCamera camera, SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for(int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[row].length; col++) {

				//make tile occupied by player
				if(this.getHero().getRect().collidesWith(arrayOfCells[row][col][0].getRect())) {
					if(!arrayOfCells[row][col][0].occupied) {
						arrayOfCells[row][col][0].occupied = true;
						System.out.println(arrayOfCells[row][col][0].tile.getName());
						//							if(arrayOfCells[row][col][0].tile.isReplacable())
						//								arrayOfCells[row][col][0].tileChange(new Sand("sand", arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y));
					}
				}
				else
					arrayOfCells[row][col][0].occupied = false;

				for(int layer = 0; layer < map[row][col].length; layer++)
					if(arrayOfCells[row][col][layer].tile != null) {
						//if(camera.frustum.pointInFrustum(arrayOfCells[row][col][layer].x, arrayOfCells[row][col][0].y, 0))
						arrayOfCells[row][col][layer].tile.render(batch, textures.getTex(arrayOfCells[row][col][layer].variation, arrayOfCells[row][col][layer].id));
					}
			}
		}
		super.render(camera, batch);
		batch.end();
	}

	@Override
	public void update (OrthographicCamera camera, float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.H)) {
			save();
		}
		super.update(camera, delta);
	}

	public void save() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++)
					map[row][col][layer] = arrayOfCells[row][col][layer].id;

		CustomGameMapLoader.saveMap("bruhlands", 0, map);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public CustomTileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate((int) (x / TileType.TILE_SIZE), (int) (y / TileType.TILE_SIZE), layer);
	}

	@Override
	public CustomTileType getTileTypeByCoordinate(int row, int col, int layer) {
		if (col < 0 || col >= getWidth() || row < 0 || row >= getHeight())
			return null;

		return arrayOfCells[row][col][layer].tile;
	}

	@Override
	public int getWidth() {
		return map.length;
	}

	@Override
	public int getHeight() {
		return map[0].length;
	}

	@Override
	public int getLayers() {
		return map[0][0].length;
	}

	@Override
	public TileType setTile(int layer, int col, int row, int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
