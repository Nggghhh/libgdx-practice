package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.HUD;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

public class Player extends LivingEntity {
	private transient HUD hud;
	private static int SPEED = 130;
	private static int DASH_VELOCITY = 400;
	private int MAX_HEALTH = 6;
	
	public Player() {
		
	}
	
	public Player(float x, float y, EntityType type, GameMap map, int id) {
		super(x, y, type, map, id);
		hud = new HUD();
	}
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		if(this.destroy == false) {
			super.update(camera, deltaTime, map);
			if(this.HEALTH > this.MAX_HEALTH) 
				this.HEALTH = this.MAX_HEALTH;
			//dash
			if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				this.velocity.x = 0;
				this.velocity.y = 0;
				dash(DASH_VELOCITY, this.direction);
				//log
			}
			//walk
			if(this.state != "HURT" && this.state != "DASH" && this.state != "ATTACK") {
				if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.W)) {
					changeState("MOVE", true, 1, 6);
					if(Gdx.input.isKeyPressed(Keys.A)) {
						moveX(-SPEED*deltaTime);
						this.direction = 1;
					}
					if(Gdx.input.isKeyPressed(Keys.D)) {
						moveX(+SPEED*deltaTime);
						this.direction = 2;
					}
					if(Gdx.input.isKeyPressed(Keys.S)) {
						moveY(-SPEED*deltaTime);
						this.direction = 3;
					}
					if(Gdx.input.isKeyPressed(Keys.W)) {
						moveY(+SPEED*deltaTime);
						this.direction = 4;
					}
				}
				else
					changeState("IDLE", true, 1, 2);
			}

			if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				strafe(400, -5f, camera);
			}

//			if(Gdx.input.justTouched() && this.state != "ATTACK" && this.previousState != "HURT") {
//				changeState("ATTACK", false, 5, 15);
//			}

			if(this.state == "ATTACK")
				attack(1, this, 60, "physical");
		}
		else
			changeState("DEATH", false, 2, 6);
	}
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		hud.render(this.HEALTH, batch, camera);
		animation(this.type, batch);
	}
	
	public void strafe(float velocity, float distance, OrthographicCamera camera) {
		this.velocity.x = 0;
		this.velocity.y = 0;
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, Unprojecter.getMouseCoords(camera).x, Unprojecter.getMouseCoords(camera).y);
        float ratio = distance/distanceBetweenEntities;
		this.velocity.x = (pos.x-Unprojecter.getMouseCoords(camera).x)*ratio*100;
		this.velocity.y = (pos.y-Unprojecter.getMouseCoords(camera).y)*ratio*100;
	}

	@Override
	public void attack(int damage, Entity hitter, int velocity, String type) {
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
}
