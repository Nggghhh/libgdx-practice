package com.mygdx.game.world.tiles;

public class Rock extends CustomTileType {
	public Rock(String name, int x, int y) {
		super(name, x, y);
		this.id = 5;
		this.group = "rock";
		this.collidable = true;
		this.destroyable = true;
		this.connectable = true;
	}
}
