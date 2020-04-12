package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.GameMap;

public abstract class CustomTileType {
	protected int id, x, y;
	protected String name;
	protected boolean collidable, destroyable, replacable = true;
	
	protected final int TILE_SIZE = 16;
	
	public CustomTileType(String name, int x, int y) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public void render(SpriteBatch batch, TextureRegion tile) {
		batch.draw(tile, x, y);
	}
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isReplacable() {
		return replacable;
	}
	
	public boolean isCollidable() {
		return collidable;
	}
}
