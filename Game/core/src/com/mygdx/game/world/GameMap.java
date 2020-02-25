package com.mygdx.game.world;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.Enemies;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Goblin;
import com.mygdx.game.entities.Player;

public abstract class GameMap {
	protected ArrayList<Entity> entities;
	public GameMap() {
		entities = new ArrayList<Entity>();
		entities.add(new Player(40, 300, this));
		entities.add(new Goblin(60, 320, this));
		entities.add(new Goblin(80, 250, this));
		entities.add(new Goblin(80, 200, this));
		entities.add(new Goblin(100, 150, this));
	}
	public void render (OrthographicCamera camera, SpriteBatch batch) {
		for(Entity entity : entities) {
			if(entity.getAlive() == false)
				entity.render(batch);
		}
	}
	
	public void update (float delta) {
		for(int i = 0; i<entities.size(); i++)
			for(int j = 0; j<entities.size(); j++)
				if(entities.get(j).getY() < entities.get(i).getY()) {
					Collections.swap(entities, i, j);
				}
		for(Entity entity : entities) {
			if(entity.getAlive() == false)
				entity.update(delta);
			
			for(Entity entityB : entities) {
				if(entity.getRect().collidesWith(entityB.getRect())) {
					if(entity instanceof Player && entityB instanceof Enemies) {
						entity.hurt(1, entityB, entity);
					}
				}
				if(entity.getRect().collidesWithAtOffset(entityB.getRect(), entity.getDirection(), 32)) {
					if(entity instanceof Player && entityB instanceof Enemies && entity.getState() == "ATTACK") {
						entityB.hurt(1, entity, entityB);
					}
				}
			}
		}
	}
	public abstract void dispose ();
	
	/**
	 * Gets a tile by pixel position within game world at a specific layer
	 * @param layer
	 * @param x
	 * @param y
	 * @return
	 */
	public TileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate(layer, (int) (x/TileType.TILE_SIZE), (int) (y/TileType.TILE_SIZE));
	}
	/**
	 * Get a tile at its coordinate within the map at a specific layer
	 * @param layer
	 * @param col
	 * @param row
	 * @return
	 */
	public abstract TileType getTileTypeByCoordinate(int layer, int col, int row);
	
	public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
		if (x < 0 || y < 0 || x+width > getPixelWidth() || y + height > getPixelHeight()) {
			return true;
		}
		for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y+height)/TileType.TILE_SIZE); row++) {
			for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x+width)/TileType.TILE_SIZE); col++) {
				for(int layer = 0; layer < getLayers(); layer++) {
					TileType type = getTileTypeByCoordinate(layer, col, row);
					if (type != null&&type.isCollidable())
						return true;
				}
			}
		}
		return false;
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getLayers();
	
	public int getPixelWidth() {
		return this.getWidth()*TileType.TILE_SIZE;
	}
	public int getPixelHeight() {
		return this.getHeight()*TileType.TILE_SIZE;
	}
	public Entity getHero() {
		return entities.get(0);
	}
}
