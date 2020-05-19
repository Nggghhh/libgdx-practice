package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.CustomGameMap;

public class Sand extends CustomTileType {
	public Sand(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 3;
		this.group = "sand";
		this.collidable = false;
		this.destroyable = false;
		this.connectable = true;
	}
}
