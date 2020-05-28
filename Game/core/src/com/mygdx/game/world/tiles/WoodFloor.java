package com.mygdx.game.world.tiles;

import com.mygdx.game.world.CustomGameMap;

public class WoodFloor extends CustomTileType {
	public WoodFloor(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 7;
		this.group = "woodFloor";
		this.collidable = false;
		this.destroyable = true;
		this.connectable = false;
		this.replacable = true;
	}
}
