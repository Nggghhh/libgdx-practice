package com.mygdx.game.world.tiles;

import com.mygdx.game.world.CustomGameMap;

public class Limestone extends CustomTileType {
	public Limestone(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 11;
		this.group = "rock";
		this.collidable = true;
		this.destroyable = true;
		this.connectable = true;
		this.replacable = true;
	}
}
