package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.GameMap;

public abstract class LivingEntity extends Entity {
	
	protected transient boolean dash = false;
	protected transient boolean attack = false;
	protected transient boolean isMoving = false;
	protected EntityFactions faction;

	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		// TODO Auto-generated method stub

	}
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		//apply velocity on X axis
		super.update(camera, deltaTime, map);
		float newX = pos.x;
		newX += this.velocity.x*deltaTime;

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
		
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = (float) Math.floor(pos.y);
			this.velocity.y = 0;
		}
		else {
			this.pos.y = newY;
		}
		
		if(angle < 225 && angle > 135)
			direction = 2;
		else if(angle < 315 && angle > 225)
			direction = 4;
		else if((angle <= 360 && angle > 315) || (angle >= 0 && angle < 45))
			direction = 1;
		else if(angle < 180 && angle > 45)
			direction = 3;
		//linear damping, adding drag to object velocity
		timer(deltaTime);		
	}
	
	//flinch and receive damage method
	@Override
	public void hurt (int damage, Entity hitter, Entity receiver) {
		if(this.remainingRecoveryTime == 0) {
			this.remainingRecoveryTime = RECOVERY_TIME;
			changeState("HURT", false, 2, 6);
			this.HEALTH -= damage;
			this.push(hitter.getX(), hitter.getY(), hitter.getType().getWeight());
			System.out.println("collide");
		}
	}
	//move object on X axis
	protected void moveX (float amount) {
		float newX = pos.x + amount;
		if (!map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			this.pos.x = newX;
		}
		else
			changeState("IDLE", true, 1, 2);
	}
	//move object on Y axis
	protected void moveY (float amount) {
		float newY = pos.y + amount;
		if (!map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = newY;
		}
		else
			changeState("IDLE", true, 1, 2);
	}
	
	protected void move(int angleToTransform, int speed, float delta) {
		double angle = Math.toRadians(angleToTransform-225);
		float newX = (float) (Math.cos(angle) - Math.sin(angle));
		float newY = (float) (Math.sin(angle) + Math.cos(angle));
		if(velocity.x != speed*newX)
			velocity.x =+ speed*newX;
		if(velocity.y != speed*newY)
			velocity.y =+ speed*newY;
	}
	
	protected float calculateAngle(float x, float y) {
		float vecX = x-pos.x;
		float vecY = y-pos.y;
		float angle = (float) ((float) Math.atan2(vecY, vecX)*(180/Math.PI));
		System.out.println(angle+180);
		return angle+180;
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
	@Override
	public void push(float x, float y, float distance) {
		if(distance < 0)
			distance = 0;
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, x, y);
        float ratio = distance/distanceBetweenEntities;
		this.velocity.x = (this.pos.x-x)*ratio*100;
		this.velocity.y = (this.pos.y-y)*ratio*100;
	}
	
	//linear damping, adding drag to object velocity
	public void timer(float deltaTime) {
		velocity.scl(1 - (slippery * deltaTime*2));
	}
	
	public void setHealth(int health) {
		this.HEALTH = health;
	}
	
	public int getHealth() {
		return HEALTH;
	}
}
