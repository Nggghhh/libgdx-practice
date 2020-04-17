package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public class TileGridCell {
	private final int CELL_SIZE = 16;
	public int x, y, id, layer;
	public GameMap map;
	protected CollisionRect rect;
	public boolean occupied = false;
	
	public CustomTileType tile;
	
	public TileGridCell(CustomGameMap map, int x, int y, int layer, int tileId) {
		this.map = map;
		this.x = x*CELL_SIZE;
		this.y = y*CELL_SIZE;
		this.layer = layer;
		this.id = tileId;

		if(this.id == 5)
			tile = new Rock("rock", this.x, this.y);
		if(this.id == 4)
			tile = new GreenGrass("greenGrass", this.x, this.y);
		if(this.id == 3)
			tile = new Sand("sand", this.x, this.y);
		if(this.id == 2)
			tile = new ShallowWater("shallowWater", this.x, this.y);
		if(this.id == 1)
			tile = new DeepWater("deepWater", this.x, this.y);
		if(this.id == 0)
			tile = new Air("air", this.x, this.y);
		
		this.rect = new CollisionRect(this.x, this.y, CELL_SIZE, CELL_SIZE, true);
	}

	public CollisionRect getRect() {
		return rect;
	}
	
	public void tileChange(CustomTileType tile, CustomGameMap map) {
		if(this.tile != tile) {
			this.tile = tile;
			this.id = tile.id;
			map.updateTile((this.x/CELL_SIZE), (this.y/CELL_SIZE)+1, this.layer);
			map.updateTile((this.x/CELL_SIZE)+1, (this.y/CELL_SIZE), this.layer);
			map.updateTile((this.x/CELL_SIZE), (this.y/CELL_SIZE)-1, this.layer);
			map.updateTile((this.x/CELL_SIZE)-1, (this.y/CELL_SIZE), this.layer);
			map.updateTile((this.x/CELL_SIZE), (this.y/CELL_SIZE), this.layer);
		}
	}
}
