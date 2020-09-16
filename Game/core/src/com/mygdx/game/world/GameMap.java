package com.mygdx.game.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Camera;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Enemies;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.Fireball;
import com.mygdx.game.entities.Goblin;
import com.mygdx.game.entities.Player;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.items.Items;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.tiles.CustomTileType;
import com.mygdx.game.world.tiles.TileGridCell;

public abstract class GameMap {
	protected transient ArrayList<Entity> entities;
	protected transient ArrayList<Entity> renderOrder;
	protected boolean mapIsLoading;

	public GameMap() {
		entities = new ArrayList<>();
		renderOrder = new ArrayList<>();
	}
	
	public void render (Camera camera, SpriteBatch batch) {
		renderOrder();
		for(Entity entity : renderOrder) {
			if(!mapIsLoading)
				if(!entity.isDisabled()) {
					if(camera.getRightB()>entity.getX()-entity.getTextureWidth() && camera.getTopB()>entity.getY() && camera.getLeftB()<entity.getX()+entity.getTextureWidth() && camera.getBottomB()<entity.getY()+entity.getTextureHeight())
						entity.render(batch, camera.getCamera());
				}
		}
	}
	
	public Entity getEntityByCoordinate(float row, float col, int layer) {
		for (Entity entity : entities) {
			float x = entity.getX();
			float y = entity.getY();
			int width = entity.getWidth();
			int height = entity.getHeight();
			int entityLayer = 0;

			if (entityLayer == layer)
				if (x <= row && x + width >= row && y <= col && y + height >= col)
					return entity;
		}
		return null;
	}
	
	public void renderOrder() {
		renderOrder.removeAll(renderOrder);
		renderOrder.addAll(entities);
		for(int i = 0; i<renderOrder.size(); i++)
			for(int j = 0; j<renderOrder.size(); j++) {
				if(renderOrder.get(j).getY() < renderOrder.get(i).getY())
					Collections.swap(renderOrder, i, j);
				
				else if(renderOrder.get(j).getY() == renderOrder.get(i).getY()) {
					if(renderOrder.get(j).getX() < renderOrder.get(i).getX()) {
						Collections.swap(renderOrder, i, j);
					}
				}
			}
	}
	//update method and collision checks, revision needed
	public void update (Camera camera, float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.L))
			printEntities();
		
		if(!mapIsLoading)
			for(int i = 0; i < entities.size(); i++) {
				//get entity out of pull and place it on stage
				//			if(entity.getDestroyed() == true) {
				//				entity.recreate(40, 300, entity.getType().getHealth());
				//			}
				
				Entity entity1 = entities.get(i);
				if(!entity1.isDisabled())
					entity1.update(camera.getCamera(), delta, this);

			//check entities for collision
			for(int b = 0; b < entities.size(); b++) {
				Entity entity2 = entities.get(b);
				if(!entity1.isDisabled() && !entity2.isDisabled()) {
					if(entity1.getRect().collidesWith(entity2.getRect()) && !entity2.isDestroyed() && entity2.getState() != "ATTACK") {
						if(entity1 instanceof Player && entity2 instanceof Enemies) {
							entity1.hurt(1, entity2, entity1);
						}
						if(entity1 instanceof Items && entity2 instanceof Items) {
							if(entities.indexOf(entity1) != entities.indexOf(entity2))
								entity1.push(entity2.getX(), entity2.getY(), entity2.getType().getWeight());
						} else if(!(entity1 instanceof Items) && !(entity2 instanceof Items))
							if(entities.indexOf(entity1) != entities.indexOf(entity2) && entity1.getState() != "HURT" && entity1.getState() != "ATTACK" && !entity1.isDestroyed())
								entity1.push(entity2.getX(), entity2.getY(), entity2.getType().getWeight());
						
						if(entity1.getRect().collidesWithAtOffset(entity2.getRect(), entity1.getDirection(), 32, 12)) {
							if(entity1 instanceof Player && entity2 instanceof Enemies && entity1.getState() == "ATTACK" && entity1.getFrame() == 1) {
								entity2.hurt(1, entity1, entity2);
							}
						}
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
	public TileGridCell getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate((int) (x/TileType.TILE_SIZE), (int) (y/TileType.TILE_SIZE), layer);
	}
	/**
	 * Get a tile at its coordinate within the map at a specific layer
	 * @param layer
	 * @param col
	 * @param row
	 * @return
	 */
	public abstract TileGridCell getTileTypeByCoordinate(int row, int col, int layer);
	public abstract TileType setTile(int layer, int col, int row, int id);
	
	public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
		if (x < 0 || y < 0 || x+width > getPixelWidth() || y + height > getPixelHeight()) {
			return true;
		}
		for (int row = (int) (x / TileType.TILE_SIZE); row < Math.ceil((x+height)/TileType.TILE_SIZE); row++) {
			for (int col = (int) (y / TileType.TILE_SIZE); col < Math.ceil((y+width)/TileType.TILE_SIZE); col++) {
				TileGridCell cell = getTileTypeByCoordinate(row, col, 1);
					if (cell != null)
						if(cell.tile != null && cell.tile.isCollidable())
							return true;
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
					return row == mY && col == mX;
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
	
	public Player getHero() {
		for(Entity entity : entities)
			if(entity instanceof Player) {
				Player player = (Player) entity;
				return player;
			}
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
	public void printEntities() {
		System.out.flush();
		System.out.println("[");
		for(Entity entity : entities)
			System.out.println(entities.indexOf(entity)+". "+entity.getType().getName()+" "+entity.isDisabled()+" "+entity.getPos().x +" "+entity.getId());
		System.out.println("]");
	}
	public void clearEntities() {
		Player player = getHero();
		entities.removeAll(entities);
		entities.add(player);
	}
	
	public void resize(int width, int height) {
		
	}
}
