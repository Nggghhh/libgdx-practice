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

public abstract class GameMap {
	protected transient ArrayList<Entity> entities;
	protected transient ArrayList<Entity> renderOrder;

	public GameMap() {
		entities = new ArrayList<Entity>();
		renderOrder = new ArrayList<Entity>();
		entities.add(new Player(210, 260, EntityType.PLAYER, this, 0));
		entities.add(new Items(215, 265, EntityType.SHORT_SWORD, this, 22));
		entities.add(new Items(230, 265, EntityType.RUBY, this, 25));
		entities.add(new Items(250, 300, EntityType.DAGGER, this, 23));
		entities.add(new Fireball(270, 300, EntityType.FIREBALL, this, 264, "UNKNOWN"));
		entities.add(new Fireball(280, 270, EntityType.FIREBALL, this, 265, "UNKNOWN"));
		for(int i = 1; i<5; i++)
			entities.add(new Goblin(RandomNumGen.getRandomNumberInRange(50, 400), RandomNumGen.getRandomNumberInRange(150, 300), EntityType.GOBLIN, this, i));
	}
	
	public void render (OrthographicCamera camera, SpriteBatch batch) {
		renderOrder();
		for(Entity entity : renderOrder) {
			if(!entity.isDisabled())
				if(camera.frustum.pointInFrustum(entity.getX(), entity.getY()+16, 0))
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
	//update method and collision checks, revision needed
	public void update (OrthographicCamera camera, float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.G))
			entities.add(new Goblin(Unprojecter.getMouseCoords(camera).x, Unprojecter.getMouseCoords(camera).y, EntityType.GOBLIN, this, 0));
		if(Gdx.input.isKeyJustPressed(Keys.L))
			printEntities();
		
		for(int i = 0; i < entities.size(); i++) {
			//get entity out of pull and place it on stage
			//			if(entity.getDestroyed() == true) {
			//				entity.recreate(40, 300, entity.getType().getHealth());
			//			}
			if(!entities.get(i).isDisabled())
				entities.get(i).update(camera, delta, this);

			//check entities for collision
			for(int b = 0; b < entities.size(); b++) {
				if(!entities.get(i).isDisabled() && !entities.get(b).isDisabled()) {
					if(entities.get(i).getRect().collidesWith(entities.get(b).getRect()) && entities.get(b).getDestroyed() == false && entities.get(b).getState() != "ATTACK") {
						if(entities.get(i) instanceof Player && entities.get(b) instanceof Enemies) {
							entities.get(i).hurt(1, entities.get(b), entities.get(i));
						}
						if(entities.get(i) instanceof Items && entities.get(b) instanceof Items) {
							if(entities.indexOf(entities.get(i)) != entities.indexOf(entities.get(b)))
								entities.get(i).push(entities.get(b).getX(), entities.get(b).getY(), entities.get(b).getType().getWeight());
						} else
							if(entities.indexOf(entities.get(i)) != entities.indexOf(entities.get(b)) && entities.get(i).getType() == entities.get(b).getType() && entities.get(i).getState() != "HURT" && !entities.get(i).getDestroyed())
								entities.get(i).push(entities.get(b).getX(), entities.get(b).getY(), entities.get(b).getType().getWeight());
						
						if(entities.get(i).getRect().collidesWithAtOffset(entities.get(b).getRect(), entities.get(i).getDirection(), 32, 12)) {
							if(entities.get(i) instanceof Player && entities.get(b) instanceof Enemies && entities.get(i).getState() == "ATTACK" && entities.get(i).getFrame() == 1) {
								entities.get(b).hurt(1, entities.get(i), entities.get(b));
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
	public CustomTileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate((int) (x/TileType.TILE_SIZE), (int) (y/TileType.TILE_SIZE), layer);
	}
	/**
	 * Get a tile at its coordinate within the map at a specific layer
	 * @param layer
	 * @param col
	 * @param row
	 * @return
	 */
	public abstract CustomTileType getTileTypeByCoordinate(int row, int col, int layer);
	public abstract TileType setTile(int layer, int col, int row, int id);
	
	public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
		if (x < 0 || y < 0 || x+width > getPixelWidth() || y + height > getPixelHeight()) {
			return true;
		}
		for (int row = (int) (x / TileType.TILE_SIZE); row < Math.ceil((x+height)/TileType.TILE_SIZE); row++) {
			for (int col = (int) (y / TileType.TILE_SIZE); col < Math.ceil((y+width)/TileType.TILE_SIZE); col++) {
					CustomTileType tile = getTileTypeByCoordinate(row, col, 1);
					if (tile != null && tile.isCollidable())
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
		for(Entity entity : entities)
			System.out.println(entities.indexOf(entity)+". "+entity.getType().getName()+" "+entity.isDisabled()+" "+entity.getPos().x +" "+entity.getId());
	}
}
