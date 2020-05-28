package com.mygdx.game.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mydgx.game.shaders.Vignette;
import com.mygdx.game.Camera;
import com.mygdx.game.HUD;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.SmallBush;
import com.mygdx.game.entities.TreeEntity;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.Raycaster;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.tiles.Air;
import com.mygdx.game.world.tiles.LanternTile;
import com.mygdx.game.world.tiles.Rock;
import com.mygdx.game.world.tiles.Sand;
import com.mygdx.game.world.tiles.ShallowWater;
import com.mygdx.game.world.tiles.TileGridCell;
import com.mygdx.game.world.tiles.TileTextureManager;
import com.mygdx.game.world.tiles.TreeTile;
import com.mygdx.game.world.tiles.WoodFloor;
import com.mygdx.game.world.tiles.WoodWall;

public class CustomGameMap extends GameMap {
	String name;
	int id;
	Raycaster ray;
	float[][][] map;
	TileGridCell[][][] arrayOfCells;
	protected DayAndNightCycles inGameTime;
	private HUD hud;
	private Vignette vignetteShader;
	private TileTextureManager tileTextures;
	private EntityAssetManager entityTextures;
	private boolean update = false;
	
	private int mapNum = 0;

	public CustomGameMap() {
		CustomGameMapData data = CustomGameMapLoader.loadMap("bruhlands", mapNum);
		this.id = data.id;
		this.name = data.name;
		this.map = data.map;
		loadEntities(data.entities);
		inGameTime = new DayAndNightCycles();
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
		
		if(getHero() != null) {
			vignetteShader.bindToWorldObject(this.getHero().getX(), this.getHero().getY(), camera);
			batch.setShader(vignetteShader.getShader());
		}
		
		
		int mapLeftBorder = (int) camera.getLeftB()/16;
		int mapRightBorder = (int) camera.getRightB()/16;
		int mapTopBorder = (int) camera.getTopB()/16;
		int mapBottomBorder = (int) camera.getBottomB()/16;
		int playerLayer;
		int roofLayer;
		if(getHero() != null) {
			playerLayer = getHero().getLayer();
			roofLayer = getHero().getLayer()+2;
		}
		else {
			playerLayer = 0;
			roofLayer = 2;
		}
			
		
		if(!update) {
			updateLight(playerLayer, roofLayer);
			update = true;
		}
		
		for(int layer = playerLayer; layer < roofLayer; layer++) {
			for(int col = mapTopBorder; col > mapBottomBorder-1; col--) {
				for(int row = mapRightBorder; row > mapLeftBorder-1; row--) {
					if(arrayOfCells[row][col][layer].tile != null && arrayOfCells[row][col][layer].tile.isVisible()) {
						arrayOfCells[row][col][layer].render(batch);
					}
				}
			}
		}
		
		super.render(camera, batch);
		batch.setShader(null);
		
		if(getHero() != null)
			hud.render(this.getHero().getHealth(), batch, camera, inGameTime.getHours(), inGameTime.getMoonPhase());
		batch.end();
	}
	
	public void updateLight(int playerLayer, int roofLayer) {
		for(int layer = playerLayer; layer < roofLayer+1; layer++) {
			for(int row = getWidth()-1; row > -1; row--) {
				for(int col = getHeight()-1; col > -1; col--) {
					if(arrayOfCells[row][col][layer].tile != null && arrayOfCells[row][col][layer].tile.isVisible()) {
						if(arrayOfCells[row][col][layer].tile.getGroup() == "light") {
							LanternTile tile2 = (LanternTile) arrayOfCells[row][col][layer].tile;
							tile2.light(this);
						}
					}
				}
			}
		}
		for(int layer = playerLayer; layer < roofLayer+1; layer++) {
			for(int row = getWidth()-1; row > -1; row--) {
				for(int col = getHeight()-1; col > -1; col--) {
					if(arrayOfCells[row][col][layer].tile != null && arrayOfCells[row][col][layer].tile.isVisible()) {
						arrayOfCells[row][col][layer].tile.adjustLight(inGameTime.getLight());
						arrayOfCells[row][col][layer].tile.updateLight();
					}
				}
			}
		}
	}
	
	public void raycastUpdate(Camera camera) {
		ray.updateTiles(this);
		for(int i = 0; i<360; i++)
			ray.raycastAtAngle(i, this, camera);
	}

	@Override
	public void update (Camera camera, float delta) {
		inGameTime.timePasses(this, delta);
		
		if(Gdx.input.isKeyJustPressed(Keys.H)) {
			changeMap();
		}
		int row = (int) Unprojecter.getMouseCoords(camera.getCamera()).x/16;
		int col = (int) Unprojecter.getMouseCoords(camera.getCamera()).y/16;

		if(Gdx.input.justTouched()) {
			if(arrayOfCells[row][col][1].tile.getId() == 0) {
				arrayOfCells[row][col][1].tileChange(new LanternTile(arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
			}
//			if(arrayOfCells[row][col][1].tile.getId() == 0  && !Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
//				arrayOfCells[row][col][1].tileChange(new WoodWall(arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
//			if(arrayOfCells[row][col][0].tile.isReplacable() && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
//				arrayOfCells[row][col][0].tileChange(new WoodFloor(arrayOfCells[row][col][0].x, arrayOfCells[row][col][0].y, this), this);
//			else if(arrayOfCells[row][col][1].tile.getId() == 34)
//				arrayOfCells[row][col][1].tileChange(new Air(arrayOfCells[row][col][1].x, arrayOfCells[row][col][1].y, this), this);
		}
		super.update(camera, delta);
		

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
					TileGridCell cell = arrayOfCells[row][col][layer];
					//flowers, empty grass, dark and highlighted grass
					if(cell.id == 4 && cell.tile.getVar() == 1) {
						int random = RandomNumGen.getRandomNumberInRange(17, 21);
						if(random == 21)
							cell.tile.setVar(1);
						else
							cell.tile.setVar(random);
					}
					//sea stars
					if(cell.id == 3 && cell.tile.getVar() == 1) {
						int random = RandomNumGen.getRandomNumberInRange(0, 40);
						if(random == 0)
							cell.tile.setVar(16);
						if(random == 1)
							cell.tile.setVar(17);
					}
					//trees
//					if(cell.id == 4) {
//						int random = RandomNumGen.getRandomNumberInRange(0, 40);
//						if(random == 40)
//							entities.add(new TreeEntity(cell.x+8, cell.y+8, EntityType.BUSH, this, 10));
//						else if(random == 39)
//							entities.add(new SmallBush(cell.x+8, cell.y+8, EntityType.SMALL_BUSH, this, 10));
//					}
				}
			}
						
	}
	
	public void loadEntities(EntitySnapshot[] snapshots) {
		for(EntitySnapshot snapshot : snapshots) {
			if(snapshot != null)
			entities.add(EntityType.createEntityUsingSnapshot(snapshot, this));
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
		loadEntities(data.entities);
		for(int col = 0; col < map.length; col++)
			for(int row = 0; row < map[col].length; row++)
				for(int layer = 0; layer < map[col][row].length; layer++) {
					arrayOfCells[row][col][layer].id = (int) this.map[row][col][layer];
					arrayOfCells[row][col][layer].assignTiles();
				}

		updateTiles();
		updateLight(0, 2);
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
	
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public boolean isSpaceOccupied(float checkX, float checkY) {
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			float x = entity.getX();
			float y = entity.getY();
			float width = entity.getX()+entity.getWidth();
			float height = entity.getY()+entity.getHeight();
			
			if(checkX > x && checkX < width && checkY > y && checkY < height)
				return true;
		}
		return false;
	}
}
