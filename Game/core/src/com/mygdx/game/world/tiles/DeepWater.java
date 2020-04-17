package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DeepWater extends AnimatedTile {
	public DeepWater(String name, int x, int y) {
		super(name, x, y);
		this.id = 1;
		this.group = "water";
		this.collidable = true;
		this.destroyable = false;
		this.replacable = false;
		this.connectable = true;
		this.liquid = true;
	}
}
