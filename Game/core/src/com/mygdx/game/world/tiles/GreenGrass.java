package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GreenGrass extends CustomTileType {
	public GreenGrass(String name, int x, int y) {
		super(name, x, y);
		this.id = 4;
		this.collidable = false;
		this.destroyable = false;
	}
}
