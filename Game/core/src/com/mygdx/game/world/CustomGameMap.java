package com.mygdx.game.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mydgx.game.shaders.Vignette;
import com.mygdx.game.Camera;
import com.mygdx.game.HUD;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.Raycaster;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.tiles.LanternTile;
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
	private HUD hud;
	private Vignette vignetteShader;
	private TileTextureManager tileTextures;
	private EntityAssetManager entityTextures;
	
	private int mapNum = 0;

	public CustomGameMap() {
		CustomGameMapData data = CustomGameMapLoader.loadMap("bruhlands", mapNum);
		this.id = data.id;
		this.name = data.name;
		this.map = data.map;
		hud = new HUD();
		vignetteShader = new Vignette("default", "default");
		tileTextures = new TileTextureManager();
		entityTextures = new EntityAssetManager();
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
		
//		batch.setShader(darken.getShader());
//		darken.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
		vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
		batch.setShader(vignetteShader.getShader());
		
//		if(inGameTime.getHours() > 21 || inGameTime.getHours() < 4) {
//			vignetteShader.newColor(0.0f, 0.4f, 0.4f);
//			vignetteShader.editCircle(.3f, .2f, 0.95f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else if(inGameTime.getHours() > 20) {
//			vignetteShader.newColor(0.0f, 0.4f, 0.4f);
//			vignetteShader.editCircle(.4f, .3f, 0.80f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else if(inGameTime.getHours() > 19) {
//			vignetteShader.newColor(0.0f, 0.4f, 0.4f);
//			vignetteShader.editCircle(.5f, .4f, 0.70f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else if(inGameTime.getHours() > 18) {
//			vignetteShader.newColor(0.0f, 0.1f, 0.1f);
//			vignetteShader.editCircle(.6f, .5f, 0.60f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else if(inGameTime.getHours() > 17) {
//			vignetteShader.newColor(0.0f, 0.1f, 0.1f);
//			vignetteShader.editCircle(.7f, .6f, 0.30f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else if(inGameTime.getHours() > 16) {
//			vignetteShader.newColor(0.0f, 0.0f, 0.0f);
//			vignetteShader.editCircle(.10f, .6f, 0.10f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else if(inGameTime.getHours() < 8) {
//			vignetteShader.newColor(0.1f, 0.1f, 0.0f);
//			vignetteShader.editCircle(.5f, .4f, 0.5f);
//			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
//			batch.setShader(vignetteShader.getShader());
//		}
//		else
//			batch.setShader(null);
		
		int mapLeftBorder = (int) camera.getLeftB()/16;
		int mapRightBorder = (int) camera.getRightB()/16;
		int mapTopBorder = (int) camera.getTopB()/16;
		int mapBottomBorder = (int) camera.getBottomB()/16;
		int playerLayer = getHero().getLayer();
		int roofLayer = getHero().getLayer()+2;
		
		for(int layer = playerLayer; layer < roofLayer; layer++) {
			for(int row = mapRightBorder; row > mapLeftBorder-1; row--) {
				for(int col = mapTopBorder; col > mapBottomBorder-1; col--) {
					if(arrayOfCells[row][col][layer].tile != null && arrayOfCells[row][col][layer].tile.isVisible()) {
						arrayOfCells[row][col][layer].tile.adjustLight(inGameTime.getLight());
						if(arrayOfCells[row][col][layer].tile.getGroup() == "light") {
							LanternTile tile = (LanternTile) arrayOfCells[row][col][layer].tile;
							tile.light(this);
						}
						arrayOfCells[row][col][layer].tile.updateLight();
						arrayOfCells[row][col][layer].render(batch);
					}
				}
			}
		}
		
//		if(this.getHero().getX() != ray.oldOriginX || this.getHero().getY() != ray.oldOriginY) {
//			ray.oldOriginX = this.getHero().getX();
//			ray.oldOriginY = this.getHero().getY();
//			raycastUpdate(camera);
//		}
//////		
//		for(int layer = playerLayer; layer < roofLayer; layer++) {
//			for(int row = mapRightBorder; row > mapLeftBorder-1; row--) {
//				for(int col = mapTopBorder; col > mapBottomBorder-1; col--) {
//					if(arrayOfCells[row][col][layer].render) {
//						arrayOfCells[row][col][layer].tile.setColors(1.0f, 1.0f, 1.0f);
//					}
//					else {
//						arrayOfCells[row][col][layer].tile.setColors(0.5f, 0.5f, 0.5f);
//					}
//					arrayOfCells[row][col][layer].render(batch);
//				}
//			}
//		}
			
		super.render(camera, batch);
		batch.setShader(null);
		
		hud.render(this.getHero().getHealth(), batch, camera.getCamera(), inGameTime.getHours(), inGameTime.getMoonPhase());
		batch.end();
	}
	
	public void raycastUpdate(Camera camera) {
		ray.updateTiles(this);
		for(int i = 0; i<360; i++)
			ray.raycastAtAngle(i, this, camera);
	}

	@Override
	public void update (Camera camera, float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.H)) {
			changeMap();
		}
		int row = (int)Unprojecter.getMouseCoords(camera.getCamera()).x/16;
		int col = (int)Unprojecter.getMouseCoords(camera.getCamera()).y/16;
		if(Gdx.input.justTouched())
		//			if(arrayOfCells[row][col][1].tile.isReplacable() && arrayOfCells[row][col][1].tile.getName() == "air") {
		//				arrayOfCells[row][col][1].tileChange(new Rock("rock", arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
		//				arrayOfCells[row][col][0].tileChange(new Sand("sand", arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y, this), this);
		//			}
					if(arrayOfCells[row][col][0].tile.isReplacable() && arrayOfCells[row][col][0].tile.getName() == "greenGrass") {
						arrayOfCells[row][col][1].tileChange(new LanternTile(arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
					}
		//			else if(arrayOfCells[row][col][1].tile.isReplacable() && arrayOfCells[row][col][1].tile.getName() == "rock")
		//				arrayOfCells[row][col][1].tileChange(new Air(arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y, this), this);
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
			if(midCell.tile != null)
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
