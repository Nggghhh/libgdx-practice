package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Air extends CustomTileType {

	public Air(String name, int x, int y) {
		super(name, x, y);
		this.id = 0;
		this.collidable = false;
		this.destroyable = false;
	}
	
	@Override
	public void render(SpriteBatch batch, TextureRegion tiles) {
		
	}
}
