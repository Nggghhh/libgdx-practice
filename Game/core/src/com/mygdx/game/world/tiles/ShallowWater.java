package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShallowWater extends CustomTileType {
	public ShallowWater(String name, int x, int y) {
		super(name, x, y);
		this.id = 2;
		this.collidable = false;
		this.destroyable = false;
		this.replacable = false;
	}
}