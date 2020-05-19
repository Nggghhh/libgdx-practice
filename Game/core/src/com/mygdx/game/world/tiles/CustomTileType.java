package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public abstract class CustomTileType {
	protected int id, x, y, variation, level = 0;
	protected String name, group;
	protected int bottomPoint;
	protected float r = 1.0f, g = 1.0f, b = 1.0f;
	protected boolean collidable, destroyable, replacable = true, connectable = false, liquid = false, visible = true;
	protected Vector3 additionalLight;
	
	protected final int TILE_SIZE = 16;
	
	public CustomTileType(int x, int y, CustomGameMap map) {
		this.x = x;
		this.y = y;
		this.variation = 0;
		this.bottomPoint = (x*TILE_SIZE)-TILE_SIZE;
		this.additionalLight = new Vector3(0.0f,0.0f,0.0f);
	}
	
	public void render(SpriteBatch batch) {
		TextureRegion texture = TileTextureManager.getTex(this.variation, this.id);
		batch.setColor(r,g,b,1.0f);
		batch.draw(texture, x, y);
		batch.setColor(1.0f,1.0f,1.0f,1.0f);
	}
	
	public void update() {
		
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
	
	public int getBottomPoint() {
		return bottomPoint;
	}
	
	public void setVisible(boolean setVisible) {
		this.visible = setVisible;
	}
	
	public void setTile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public float[] getColors() {
		float[] colors = {r,g,b};
		return colors;
	}
	
	public void setColors(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void addColors(float r, float g, float b) {
		this.r += r;
		this.g += g;
		this.b += b;
	}
	
	public void adjustLight(Vector3 ambientLight) {
		this.r = 0.0f + ambientLight.x + additionalLight.x;
		this.g = 0.0f + ambientLight.y + additionalLight.y;
		this.b = 0.0f + ambientLight.z + additionalLight.z;
	}
	
	public void updateLight() {
		additionalLight.set(0.0f, 0.0f, 0.0f);
	}
	
	public void addLight(float r, float g, float b) {
		additionalLight.add(r, g, b);
	}
}
