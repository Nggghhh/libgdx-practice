package com.mygdx.game.world.tiles;

import com.mygdx.game.world.CustomGameMap;

public class Rock extends CustomTileType {
	public Rock(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 5;
		this.group = "rock";
		this.collidable = true;
		this.destroyable = true;
		this.connectable = true;
		this.replacable = true;
	}
}
