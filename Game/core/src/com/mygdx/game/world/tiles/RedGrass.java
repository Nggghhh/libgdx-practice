package com.mygdx.game.world.tiles;

import com.mygdx.game.world.CustomGameMap;

public class RedGrass extends CustomTileType {

	public RedGrass(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 10;
		this.name = "redGrass";
		this.tileGroup = TileGroup.GRASS;
		this.collidable = false;
		this.destroyable = false;
		this.connectable = true;
	}

}
