package com.mygdx.game.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mydgx.game.shaders.Vignette;
import com.mygdx.game.Camera;
import com.mygdx.game.HUD;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.Raycaster;
import com.mygdx.game.tools.SimplexNoise;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.tiles.Air;
import com.mygdx.game.world.tiles.CustomTileType;
import com.mygdx.game.world.tiles.GreenGrass;
import com.mygdx.game.world.tiles.Rock;
import com.mygdx.game.world.tiles.Sand;
import com.mygdx.game.world.tiles.ShallowWater;
import com.mygdx.game.world.tiles.TileGridCell;
import com.mygdx.game.world.tiles.TileTextureManager;
import com.mygdx.game.world.tiles.TreeTile;

public class CustomGameMap extends GameMap {
	String name;
	int id;
	Raycaster ray;
	float[][][] map;
	TileGridCell[][][] arrayOfCells;
	private TileTextureManager textures;
	private HUD hud;
	private Vignette vignetteShader;
	
	private int mapNum = 0;

	public CustomGameMap() {
		CustomGameMapData data = CustomGameMapLoader.loadMap("bruhlands", mapNum);
		this.id = data.id;
		this.name = data.name;
		this.map = data.map;
		textures = new TileTextureManager();
		hud = new HUD();
		vignetteShader = new Vignette();
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
		
		ray = new Raycaster();
	}

	@Override
	public void render(Camera camera, SpriteBatch batch) {
		batch.setProjectionMatrix(camera.getCamera().combined);
		batch.begin();
		vignetteShader.circlePos(Gdx.input.getX()/(camera.getCamera().viewportWidth*2), ((-Gdx.input.getY()+Gdx.graphics.getHeight())/(camera.getCamera().viewportHeight*2)));
		batch.setShader(vignetteShader.applyShader());
//		vignetteShader.circlePos(0.5f, 0.5f);
		
		int mapLeftBorder = (int) camera.getLeftB()/16;
		int mapRightBorder = (int) camera.getRightB()/16;
		int mapTopBorder = (int) camera.getTopB()/16;
		int mapBottomBorder = (int) camera.getBottomB()/16;
		
		for(int layer = getHero().getLayer(); layer < getHero().getLayer()+2; layer++) {
			for(int row = mapRightBorder; row > mapLeftBorder-1; row--) {
				for(int col = mapTopBorder; col > mapBottomBorder-1; col--) {
					if(arrayOfCells[row][col][layer].tile != null && arrayOfCells[row][col][layer].tile.isVisible()) {
						arrayOfCells[row][col][layer].render(batch);
					}
				}
			}
		}
		
//		if(this.getHero().getX() != ray.oldOriginX || this.getHero().getY() != ray.oldOriginY) {
//			ray.oldOriginX = this.getHero().getX();
//			ray.oldOriginY = this.getHero().getY();
//			raycastUpdate(batch, camera);
//		}
//		
//		for(int layer = 0; layer < getLayers(); layer++)
//			for(int row = map.length-1; row > 0; row--) {
//				for(int col = map[row].length-1; col > 0; col--) {
//					if(arrayOfCells[row][col][layer].render) {
//						arrayOfCells[row][col][layer].render(batch);
//					}
//				}
//			}
			
		super.render(camera, batch);
		batch.setShader(null);
		
		hud.render(this.getHero().getHealth(), batch, camera.getCamera(), inGameTime.getHours(), inGameTime.getMoonPhase());
		batch.end();
	}
	
	public void raycastUpdate(SpriteBatch batch, Camera camera) {
		ray.updateTiles(this);
		for(int i = 0; i<360; i++)
			ray.raycastAtAngle(i, this, camera);
	}

	@Override
	public void update (OrthographicCamera camera, float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.H)) {
			changeMap();
		}
		if(Gdx.input.justTouched()) {
			int row = (int)Unprojecter.getMouseCoords(camera).x/16;
			int col = (int)Unprojecter.getMouseCoords(camera).y/16;
			if(arrayOfCells[row][col][1].tile.isReplacable() && arrayOfCells[row][col][1].tile.getName() == "air") {
				arrayOfCells[row][col][1].tileChange(new Rock("rock", arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
				arrayOfCells[row][col][0].tileChange(new Sand("sand", arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y, this), this);
			}
//			else if(arrayOfCells[row][col][0].tile.isReplacable() && arrayOfCells[row][col][0].tile.getName() == "greenGrass") {
//				arrayOfCells[row][col][1].tileChange(new TreeTile("tree", arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
//			}
			else if(arrayOfCells[row][col][1].tile.isReplacable() && arrayOfCells[row][col][1].tile.getName() == "rock")
				arrayOfCells[row][col][1].tileChange(new Air("air", arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y, this), this);
		}
		super.update(camera, delta);
		
//		make tile occupied by player
//		if(this.getHero().getRect().collidesWith(arrayOfCells[row][col][0].getRect())) {
//			if(!arrayOfCells[row][col][0].occupied) {
//				arrayOfCells[row][col][0].occupied = true;
//				System.out.println(arrayOfCells[row][col][0].tile.getName());
////				if(arrayOfCells[row][col][0].tile.isReplacable())
////					arrayOfCells[row][col][0].tileChange(new ShallowWater("shallowwater", arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y), this);
//			}
//		}
//		else
//			arrayOfCells[row][col][0].occupied = false;
	}

	public void save() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++)
					map[row][col][layer] = arrayOfCells[row][col][layer].id;

		CustomGameMapLoader.saveMap(this.name, this.id, map);
	}
	
	public void updateTiles() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++)
					updateTile(row, col, layer);

	}
	
	public void updateTile(int row, int col, int layer) {
		try {
			TileGridCell midCell = arrayOfCells[row][col][layer];
			TileGridCell upCell = arrayOfCells[row][col+1][layer];
			TileGridCell downCell = arrayOfCells[row][col-1][layer];
			TileGridCell leftCell = arrayOfCells[row-1][col][layer];
			TileGridCell rightCell = arrayOfCells[row+1][col][layer];
			if(midCell.tile.isConnectable())
//				if(upCell.tile.getLevel() >= midCell.tile.getLevel() && downCell.tile.getLevel() >= midCell.tile.getLevel() && leftCell.tile.getLevel() >= midCell.tile.getLevel() && rightCell.tile.getLevel() >= midCell.tile.getLevel())
					if(upCell.tile.getGroup() == midCell.tile.getGroup() && downCell.tile.getGroup() == midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
						midCell.tile.setVar(1);
					else if(upCell.tile.getGroup() == midCell.tile.getGroup()  && downCell.tile.getGroup() != midCell.tile.getGroup() && leftCell.tile.getGroup() == midCell.tile.getGroup() && rightCell.tile.getGroup() == midCell.tile.getGroup())
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
		}
	}
	
	public void plants() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++) {
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
						int random = RandomNumGen.getRandomNumberInRange(0, 40);
						if(random == 0)
							arrayOfCells[row][col][layer].tile.setVar(16);
						if(random == 1)
							arrayOfCells[row][col][layer].tile.setVar(17);
					}
				}
			}
						
	}
	
	public void changeMap() {
		save();
		this.mapIsLoading = true;
		clearEntities();
//		tilesDispose();
		mapNum++;
		CustomGameMapData data = CustomGameMapLoader.loadMap("bruhlands", mapNum);
		id = data.id;
		name = data.name;
		map = data.map;
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++) {
					arrayOfCells[row][col][layer].id = (int) this.map[row][col][layer];
					arrayOfCells[row][col][layer].assignTiles();
				}

		updateTiles();
		plants();
		this.mapIsLoading = false;
	}
	
	public void tilesDispose() {
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++) {
					arrayOfCells[row][col][layer].tile = null;
				}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		vignetteShader.dispose();
	}

	@Override
	public void init(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public TileGridCell getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate((int) (x / TileType.TILE_SIZE), (int) (y / TileType.TILE_SIZE), layer);
	}

	@Override
	public TileGridCell getTileTypeByCoordinate(int row, int col, int layer) {
		if (col < 0 || col >= getWidth() || row < 0 || row >= getHeight())
			return null;
		else
			return arrayOfCells[row][col][layer];
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
	
	public TileGridCell[][][] getCells() {
		return arrayOfCells;
	}
	
	@Override
	public void resize(int width, int height) {
		vignetteShader.resize(width, height);
	}

	@Override
	public TileType setTile(int layer, int col, int row, int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
