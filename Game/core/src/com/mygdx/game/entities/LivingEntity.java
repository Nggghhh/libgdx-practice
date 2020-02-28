package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.world.GameMap;

public abstract class LivingEntity extends Entity {
	
	protected final float RECOVERY_TIME = 1f;
	protected float remainingRecoveryTime;
	
	protected boolean dash = false;
	
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
		//apply velocity on X axis
		float newX = pos.x;
		newX += this.velocity.x*deltaTime;
		
		//move collision rectangle with its sprite
		rect.move(this.pos.x, this.pos.y);
		
		//apply velocity on Y axis
		float newY = pos.y;
		newY += this.velocity.y*deltaTime;
		
		//check if collision with solid tile on Y axis occurred
		if(map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			this.pos.x = (float) Math.floor(pos.x);
			this.velocity.x = 0;
		}
		else {
			this.pos.x = newX;
		}
		
		//check if collision with solid tile on X axis occurred
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = (float) Math.floor(pos.y);
			this.velocity.y = 0;
		}
		else {
			this.pos.y = newY;
		}
		
		//linear damping, adding drag to object velocity
		timer(deltaTime);
		
		//after recovery time is ended, reset back to idling state
		if(remainingRecoveryTime < RECOVERY_TIME/2 && this.state == "HURT")
			this.state = "IDLE";
		
		if(remainingRecoveryTime > 0)
			remainingRecoveryTime -= deltaTime;
		else if(remainingRecoveryTime < 0)
			remainingRecoveryTime = 0;
		
		//destroy after flinch animation is ended
		if(this.HEALTH == 0 && this.state != "HURT")
			this.destroy = true;
	}
	
	//flinch and receive damage method
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
	//move object on X axis
	protected void moveX (float amount) {
		float newX = pos.x + amount;
		if (!map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight()))
			this.pos.x = newX;	
	}
	//move object on Y axis
	protected void moveY (float amount) {
		float newY = pos.y + amount;
		if (!map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight()))
			this.pos.y = newY;
	}
	//dash method, revision needed
	public void dash (float velocity, int direction) {
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
	//push object from other object
	public void push(float velocity, Entity pusher,float distance) {
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, pusher.pos.x, pusher.pos.y);
        float ratio = distance/distanceBetweenEntities;
		this.velocity.x = (this.pos.x-pusher.pos.x)*ratio*100;
		this.velocity.y = (this.pos.y-pusher.pos.y)*ratio*100;
	}
	
	//linear damping, adding drag to object velocity
	public void timer(float deltaTime) {
		velocity.scl(1 - (4f * deltaTime*2));
	}
}
