package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sand extends CustomTileType {
	public Sand(String name, int x, int y) {
		super(name, x, y);
		this.id = 3;
		this.group = "sand";
		this.collidable = false;
		this.destroyable = false;
		this.connectable = true;
	}
}
