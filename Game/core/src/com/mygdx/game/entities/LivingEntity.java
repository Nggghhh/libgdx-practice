package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.world.GameMap;

public abstract class LivingEntity extends Entity {
	
	protected final float RECOVERY_TIME = 1f;
	protected float remainingRecoveryTime;
	
	protected float velocityY;
	protected float velocityX;
	protected boolean dash = false;
	protected Vector2 damagedVector;
	
	protected int HEALTH;
	
	public LivingEntity(float x, float y, EntityType type, GameMap map) {
		super(x, y, type, map);
		this.HEALTH = type.getHealth();
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}
	@Override
	public void update(float deltaTime) {
		float newX = pos.x;
		newX += this.velocityX*deltaTime;
		
		rect.move(this.pos.x, this.pos.y);
		
		float newY = pos.y;
		newY += this.velocityY*deltaTime;
		
		if(map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			this.pos.x = (float) Math.floor(pos.x);
			this.velocityX = 0;
		}
		else {
			this.pos.x = newX;
		}
		
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = (float) Math.floor(pos.y);
			this.velocityY = 0;
		}
		else {
			this.pos.y = newY;
		}
		timer();
		
		if(remainingRecoveryTime < RECOVERY_TIME/2 && this.state == "HURT")
			this.state = "IDLE";
		
		if(remainingRecoveryTime > 0)
			remainingRecoveryTime -= deltaTime;
		else if(remainingRecoveryTime < 0)
			remainingRecoveryTime = 0;
	}
	
	@Override
	public void hurt (int damage, Entity hitter, Entity receiver) {
		if(this.remainingRecoveryTime == 0) {
			this.remainingRecoveryTime = RECOVERY_TIME;
			this.state = "HURT";
			this.HEALTH -= damage;
			this.push(200, hitter, 2f);
			System.out.println("collide");
		}
	}
	
	protected void moveX (float amount) {
		float newX = pos.x + amount;
		if (!map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight()))
			this.pos.x = newX;	
	}
	
	protected void moveY (float amount) {
		float newY = pos.y + amount;
		if (!map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight()))
			this.pos.y = newY;
	}
	
	public void dash (float velocity, int direction) {
		if(this.direction == 1) {
			this.velocityX -= velocity;
		}
		if(this.direction == 2) {
			this.velocityX += velocity;
		}
		if(this.direction == 3) {
			this.velocityY -= velocity;
		}
		if(this.direction == 4) {
			this.velocityY += velocity;
		}
	}
	
	public void push(float velocity, Entity pusher,float distance) {
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, pusher.pos.x, pusher.pos.y);
        float ratio = distance/distanceBetweenEntities;
		this.velocityX = (this.pos.x-pusher.pos.x)*ratio*100;
		this.velocityY = (this.pos.y-pusher.pos.y)*ratio*100;
	}
	
	public void timer() {
		time = 20;
		if(this.velocityX < 0) {
			this.velocityX += time;
			if(this.velocityX > 0)
				this.velocityX = 0;
		}
		if(this.velocityX > 0) {
			this.velocityX -= time;
			if(this.velocityX < 0)
				this.velocityX = 0;
		}
		if(this.velocityY < 0) {
			this.velocityY += time;
			if(this.velocityY > 0)
				this.velocityY = 0;
		}
		if(this.velocityY > 0) {
			this.velocityY -= time;
			if(this.velocityY < 0)
				this.velocityY = 0;
		}
	}
}
