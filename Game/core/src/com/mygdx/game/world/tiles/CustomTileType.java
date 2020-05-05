package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public abstract class CustomTileType {
	protected int id, x, y, variation, level = 0;
	protected String name, group;
	protected boolean collidable, destroyable, replacable = true, connectable = false, liquid = false, visible = true;
	
	protected final int TILE_SIZE = 16;
	
	public CustomTileType(String name, int x, int y, CustomGameMap map) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.variation = 0;
	}
	
	public void render(SpriteBatch batch) {
		TextureRegion texture = TileTextureManager.getTex(this.variation, this.id);
		batch.draw(texture, x, y);
	}
	
	public void destroy(CustomGameMap map) {
		
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
	
	public boolean isConnectable() {
		return connectable;
	}

	public boolean isLiquid() {
		return liquid;
	}
	
	public int getVar() {
		return variation;
	}
	
	public void setVar(int newVar) {
		this.variation = newVar;
	}
	
	public String getGroup() {
		return group;
	}
	
	public int getLevel() {
		return level;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean setVisible) {
		this.visible = setVisible;
	}
	
	public void setTile(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
