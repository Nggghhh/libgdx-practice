package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.HUD;
import com.mygdx.game.tools.SoundManager;
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
			this.velocity.x = 0;
			this.velocity.y = 0;
			dash(DASH_VELOCITY, this.direction);
			//log
			System.out.println(deltaTime+" "+super.time+" ");
			System.out.println(this.remainingRecoveryTime);
		}
		//walk
		if(this.state != "HURT" && this.state != "DASH" && this.state != "ATTACK") {
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
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			strafe(400, -5f);
			System.out.println(Gdx.input.getX()+" "+Gdx.input.getY());
		}
		
		if(Gdx.input.justTouched() && this.state != "ATTACK" && this.previousState != "HURT") {
			attack(1, this, this.direction, "physical");
		}
			
		if(this.HEALTH > this.MAX_HEALTH) this.HEALTH = this.MAX_HEALTH;
		super.update(deltaTime);
	}
	@Override
	public void render(SpriteBatch batch) {
		hud.render(this.HEALTH, batch);
		animation(this.type, batch);
	}
	
	float mouseX;
	float mouseY;
	public void strafe(float velocity, float distance) {
		mouseX = Gdx.input.getX();
		mouseY = Gdx.graphics.getHeight()-Gdx.input.getY();
		this.velocity.x = 0;
		this.velocity.y = 0;
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, mouseX, mouseY);
        float ratio = distance/distanceBetweenEntities;
		this.velocity.x = (this.pos.x-mouseX)*ratio*100;
		this.velocity.y = (this.pos.y-mouseY)*ratio*100;
	}

	@Override
	public void attack(int damage, Entity hitter, int direction, String type) {
		strafe(400, -1f);
		this.state = "ATTACK";
		SoundManager.play("slash");
	}
}
