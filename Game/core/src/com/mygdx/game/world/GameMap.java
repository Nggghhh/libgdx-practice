package com.mygdx.game.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.Enemies;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.Goblin;
import com.mygdx.game.entities.Player;
import com.mygdx.game.items.Items;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.Unprojecter;

public abstract class GameMap {
	protected transient ArrayList<Entity> entities;
	protected transient ArrayList<Entity> renderOrder;
	protected transient ArrayList<Items> inventory;
	public GameMap() {
		entities = new ArrayList<Entity>();
		renderOrder = new ArrayList<Entity>();
		entities.add(new Player(210, 260, EntityType.PLAYER, this, 0));
		entities.add(new Items(215, 265, EntityType.DAGGER, this, 22));
		for(int i = 1; i<5; i++)
			entities.add(new Goblin(RandomNumGen.getRandomNumberInRange(50, 400), RandomNumGen.getRandomNumberInRange(150, 300), EntityType.GOBLIN, this, i));
	}
	public void render (OrthographicCamera camera, SpriteBatch batch) {
		renderOrder();
		for(Entity entity : renderOrder) {
			if(camera.frustum.pointInFrustum(entity.getX(), entity.getY(), 0))
				entity.render(batch, camera);
		}
	}
	
	public void renderOrder() {
		renderOrder.removeAll(renderOrder);
		renderOrder.addAll(entities);
		for(int i = 0; i<renderOrder.size(); i++)
			for(int j = 0; j<renderOrder.size(); j++)
				if(renderOrder.get(j).getY() < renderOrder.get(i).getY())
					Collections.swap(renderOrder, i, j);
	}
	//removes object from collection
	public void remove(Entity entity) {
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext()) {
			Entity ent = it.next();
			if(ent == entity) {
				it.remove();
			}
		}
	}
	//update method and collision checks, revision needed
	public void update (OrthographicCamera camera, float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.N))
			remove(getItems());
		if(Gdx.input.isKeyJustPressed(Keys.G))
			entities.add(new Goblin(Unprojecter.getMouseCoords(camera).x, Unprojecter.getMouseCoords(camera).y, EntityType.GOBLIN, this, 0));
		for(Entity entity : entities) {
			//get entity out of pull and place it on stage
			//			if(entity.getDestroyed() == true) {
			//				entity.recreate(40, 300, entity.getType().getHealth());
			//			}
			//pursue player
			entity.update(camera, delta, this);

			//check entities for collision
			for(Entity entityB : entities) {
				if(entity.getRect().collidesWith(entityB.getRect()) && entityB.getDestroyed() == false && entityB.getState() != "ATTACK") {
					if(entity instanceof Player && entityB instanceof Enemies) {
						entity.hurt(1, entityB, entity);
					}
					if(entity.getId() != entityB.getId() && entity.getType() == entityB.getType() && entity.getState() != "HURT")
						entity.push(entityB, 0.5f);
				}
				if(entity.getRect().collidesWithAtOffset(entityB.getRect(), entity.getDirection(), 32, 12)) {
					if(entity instanceof Player && entityB instanceof Enemies && entity.getState() == "ATTACK" && entity.getFrame() == 1) {
						entityB.hurt(1, entity, entityB);
					}
				}
			}

		}
	}
	public abstract void dispose ();
	public abstract void init(String name);
	
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
	public abstract TileType setTile(int layer, int col, int row, int id);
	
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
	/**
	 * Check if entity is on this tile. MouseX and MouseY are coordinates of the tile, which you want to check, X and Y - coordinates of the entity. If they match each other, then this method returns true
	 */
	public boolean isEntityOnTile(float x, float y, int width, int height, float mouseX, float mouseY) {
		int mX = (int) mouseX/TileType.TILE_SIZE;
		int mY = (int) mouseY/TileType.TILE_SIZE;
		for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y+height)/TileType.TILE_SIZE); row++) {
			for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x+width)/TileType.TILE_SIZE); col++) {
				for(int layer = 0; layer < getLayers();) {
					if (row == mY && col == mX)
						return true;
					else
						return false;
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
		for(Entity entity : entities)
			if(entity instanceof Player)
				return entity;
		return null;
	}
	public Entity getEnemies() {
		for(Entity entity : entities)
			if(entity instanceof Enemies)
				return entity;
		return null;
	}
	public Entity getItems() {
		for(Entity entity : entities)
			if(entity instanceof Items)
				return entity;
		return null;
	}
	public ArrayList<Entity> getEntities() {
		return entities;
	}
}
