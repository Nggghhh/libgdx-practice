package com.mygdx.game.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.Camera;
import com.mygdx.game.HUD;
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
	String name;
	int id;
	float[][][] map;
	TileGridCell[][][] arrayOfCells;
	private TileTextureManager textures;
	private HUD hud;

	public CustomGameMap() {
		CustomGameMapData data = CustomGameMapLoader.loadMap("bruhlands", 0);
		this.id = data.id;
		this.name = data.name;
		this.map = data.map;
		textures = new TileTextureManager();
		hud = new HUD();
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
		updateTiles();
		plants();
	}

	@Override
	public void render(Camera camera, SpriteBatch batch) {
		batch.setProjectionMatrix(camera.getCamera().combined);
		batch.begin();

		for(int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[row].length; col++) {

				//make tile occupied by player
				if(this.getHero().getRect().collidesWith(arrayOfCells[row][col][0].getRect())) {
					if(!arrayOfCells[row][col][0].occupied) {
						arrayOfCells[row][col][0].occupied = true;
						System.out.println(arrayOfCells[row][col][0].tile.getName());
//						if(arrayOfCells[row][col][0].tile.isReplacable())
//							arrayOfCells[row][col][0].tileChange(new ShallowWater("shallowwater", arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y), this);
					}
				}
				else
					arrayOfCells[row][col][0].occupied = false;

				for(int layer = 0; layer < map[row][col].length; layer++)
					if(arrayOfCells[row][col][layer].tile != null) {
						if(camera.getRightB()>arrayOfCells[row][col][layer].x && camera.getTopB()>arrayOfCells[row][col][0].y && camera.getLeftB()-16<arrayOfCells[row][col][layer].x && camera.getBottomB()-16<arrayOfCells[row][col][0].y) {
							arrayOfCells[row][col][layer].tile.render(batch);
						}
					}
			}
		}
		super.render(camera, batch);
		hud.render(this.getHero().getHealth(), batch, camera.getCamera());
		
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
	
	public void updateTiles() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++) {
					updateTile(row, col, layer);
				}

	}
	
	public void updateTile(int row, int col, int layer) {
		try {
			TileGridCell midCell = arrayOfCells[row][col][layer];
			TileGridCell upCell = arrayOfCells[row][col+1][layer];
			TileGridCell downCell = arrayOfCells[row][col-1][layer];
			TileGridCell leftCell = arrayOfCells[row-1][col][layer];
			TileGridCell rightCell = arrayOfCells[row+1][col][layer];
			if(midCell.tile.isConnectable())
				if(upCell.tile.getGroup() == midCell.tile.getGroup() && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(1);
				else if(midCell.tile.isConnectable() && upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(2);
				else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(3);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(4);
				else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(5);
				else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(6);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(7);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(8);
				else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(9);
				else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(10);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(11);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(12);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(13);
				else if(upCell.tile.getGroup() != midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
					midCell.tile.setVar(14);
				else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() != midCell.tile.getGroup() && rightCell.tile.getGroup() != midCell.tile.getGroup())
					midCell.tile.setVar(15);
				else
					midCell.tile.setVar(0);
				
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("out of bounds");
		}
	}
	
	public void plants() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++) {
					//flowers, empty grass, dark and highlighted grass
					if(arrayOfCells[row][col][layer].id == 4 && arrayOfCells[row][col][layer].tile.getVar() == 1) {
						int random = RandomNumGen.getRandomNumberInRange(16, 21);
						if(random == 21)
							arrayOfCells[row][col][layer].tile.setVar(1);
						else
							arrayOfCells[row][col][layer].tile.setVar(random);
					}
					//sea stars
					if(arrayOfCells[row][col][layer].id == 3 && arrayOfCells[row][col][layer].tile.getVar() == 1) {
						int random = RandomNumGen.getRandomNumberInRange(0, 20);
						if(random == 0)
							arrayOfCells[row][col][layer].tile.setVar(16);
					}
				}
						
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
		else
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
	
	public int getPixelWidth() {
		return map.length*16;
	}

	public int getPixelHeight() {
		return map[0].length*16;
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
