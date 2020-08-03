package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.HUD;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.items.Items;
import com.mygdx.game.items.Knife;
import com.mygdx.game.items.Weapons;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

public class Player extends LivingEntity {
	private static int DASH_VELOCITY = 400;
	private int MAX_HEALTH = 6;
	private Inventory playerInventory;
	private Weapons weapon;
	
	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
		playerInventory = new Inventory(map);
		weapon = new Knife();
		this.faction = EntityFactions.PLAYER;
	}
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
		if(this.HEALTH > this.MAX_HEALTH) 
			this.HEALTH = this.MAX_HEALTH;
		
		if(!this.destroy) {
			livingState(camera, deltaTime, map);
		}
		else {
			changeState("DEATH", false, 2, 6);
		}
	}
	
	@Override
	public void livingState(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.livingState(camera, deltaTime, map);
		
		if(Gdx.input.isKeyJustPressed(Keys.E))
			pickUpItems(map);
		
		playerInventory.collectInput(map);
		
		//walk
//		if(this.state != "HURT" && this.state != "DASH" && this.state.charAt(0) != 'A') {
//			if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.W)) {
//				changeState("MOVE", true, 7, 6);
//				if(Gdx.input.isKeyPressed(Keys.W)) {
//					angle = 270;
//				}
//				else if(Gdx.input.isKeyPressed(Keys.A)) {
//					angle = 360;
//				}
//				else if(Gdx.input.isKeyPressed(Keys.S)) {
//					angle = 90;
//				}
//				else if(Gdx.input.isKeyPressed(Keys.D)) {
//					angle = 180;
//				}
//				move(angle, SPEED, deltaTime);
//			}
//			else {
//				changeState("IDLE", true, 1, 2);
//			}
//		}
		
		if(this.state != "HURT" && this.state != "DASH" && this.state.charAt(0) != 'A') {
			if(Gdx.input.isKeyPressed(Keys.W)) {
				changeState("MOVE", true, 7, 6);
				angle = (int) calculateAngle(Unprojecter.getMouseCoords(camera).x, Unprojecter.getMouseCoords(camera).y);
				move(angle, SPEED, deltaTime);
			}
			else {
				changeState("IDLE", true, 1, 2);
			}
		}

//		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
//			strafe(-7f, camera);
//		}

		if(Gdx.input.justTouched() && this.state != "ATTACK" && this.previousState != "HURT") {
			if(playerInventory.getItemList()[0] != null) {
				CustomGameMap newMap = (CustomGameMap) map;
				playerInventory.getItemList()[0].action(newMap, this, playerInventory, camera);
			}
		}
		
		if(weapon != null)
			weapon.update(this, map);
		
	}
	
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animationPlay(batch);
	}
	
	public void strafe(float distance, OrthographicCamera camera) {
		this.velocity.x = 0;
		this.velocity.y = 0;
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, Unprojecter.getMouseCoords(camera).x, Unprojecter.getMouseCoords(camera).y);
        float ratio = distance/distanceBetweenEntities;
		this.velocity.x = (pos.x-Unprojecter.getMouseCoords(camera).x)*ratio*100;
		this.velocity.y = (pos.y-Unprojecter.getMouseCoords(camera).y)*ratio*100;
	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
		if(this.frameNum == 1) {
			if(this.direction == 1) {
				this.velocity.x -= velocity;
			}
			if(this.direction == 2) {
				this.velocity.x += velocity;
			}
			if(this.direction == 3) {
				this.velocity.y -= velocity;
			}
			if(this.direction == 4) {
				this.velocity.y += velocity;
			}
		}
	}

	@Override
	public void recreate(int x, int y, int health) {
		this.pos.x = x;
		this.pos.y = y;
		this.HEALTH = health;
		this.destroy = false;
	}
	
	public Inventory getPlayerInventory() {
		return playerInventory;
	}
	
	public void pickUpItems(GameMap map) {
		for(int b = 0; b < map.getEntities().size(); b++) {
			Entity entity = map.getEntities().get(b);
			if(entity instanceof Items && !entity.isDisabled()) {
				if(rect.collidesWith(entity.getRect()) && isDestroyed() == false) {
					int slot = playerInventory.getEmptySlot();
					if(slot != -1) {
						playerInventory.put((Items) entity, slot);
						map.getEntities().remove(entity);
						return;
					}
				}
			}
		}
	}
	
	@Override
	public void addHealth(int amount) {
		if(this.HEALTH+amount > this.MAX_HEALTH) 
			this.HEALTH = this.MAX_HEALTH;
		else
			this.HEALTH += amount;
	}
}
