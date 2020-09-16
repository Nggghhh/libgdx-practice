package com.mygdx.game.world.tiles;

import com.mygdx.game.world.CustomGameMap;

public class WoodWall extends CustomTileType {
	public WoodWall(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 6;
		this.tileGroup = TileGroup.WOOD_WALL;
		this.collidable = true;
		this.destroyable = true;
		this.connectable = true;
		this.replacable = true;
	}
}
