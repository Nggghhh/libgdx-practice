package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.HUD;
import com.mygdx.game.world.GameMap;

public class Player extends LivingEntity {
	HUD hud = new HUD();
	private static int SPEED = 130;
	private static int DASH_VELOCITY = 400;
	private int MAX_HEALTH = 6;
	
	public Player(float x, float y, GameMap map) {
		super(x, y, EntityType.PLAYER, map);
	}
	
	@Override
	public void update(float deltaTime) {
		//dash
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			this.velocityX = 0;
			this.velocityY = 0;
			dash(DASH_VELOCITY, this.direction);
			//log
			System.out.println(velocityY+" "+velocityX+" "+deltaTime+" "+super.time+" ");
			System.out.println(this.remainingRecoveryTime);
		}
		//walk
		if(this.state != "HURT" && this.state != "DASH") {
			if(Gdx.input.isKeyPressed(Keys.A)) {
				moveX(-SPEED*deltaTime);
				this.direction = 1;
				this.state = "IDLE";
			}
			if(Gdx.input.isKeyPressed(Keys.D)) {
				moveX(+SPEED*deltaTime);
				this.direction = 2;
				this.state = "IDLE";
			}
			if(Gdx.input.isKeyPressed(Keys.S)) {
				moveY(-SPEED*deltaTime);
				this.direction = 3;
				this.state = "IDLE";
			}
			if(Gdx.input.isKeyPressed(Keys.W)) {
				moveY(+SPEED*deltaTime);
				this.direction = 4;
				this.state = "IDLE";
			}
		}
		
		if(this.HEALTH < 0) this.HEALTH = 0;
		if(this.HEALTH > this.MAX_HEALTH) this.HEALTH = this.MAX_HEALTH;
		super.update(deltaTime);
	}
	@Override
	public void render(SpriteBatch batch) {
		hud.render(this.HEALTH, batch);
		animation(this.type, batch);
	}
}
