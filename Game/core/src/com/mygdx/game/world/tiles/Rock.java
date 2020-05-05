package com.mygdx.game.world.tiles;

import com.mygdx.game.world.CustomGameMap;

public class Rock extends CustomTileType {
	public Rock(String name, int x, int y, CustomGameMap map) {
		super(name, x, y, map);
		this.id = 5;
		this.group = "rock";
		this.collidable = true;
		this.destroyable = true;
		this.connectable = true;
		this.replacable = true;
	}
}
