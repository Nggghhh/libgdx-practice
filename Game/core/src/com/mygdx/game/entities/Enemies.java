package com.mygdx.game.entities;

import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.GameMap;

public abstract class Enemies extends LivingEntity {
	
	private static final float MAX_STRAY_TIME = 1f;
	private static final int SPEED = 20;
	protected float remainingStrayTime = 1f;
	protected String behavior = "PASSIVE";
	public Enemies(float x, float y, EntityType type, GameMap map) {
		super(x, y, type, map);
		// TODO Auto-generated constructor stub
	}
	
	public enum Behavior{
		AGRESSIVE,
		PASSIVE;
	}
	
	@Override
	public void update(float deltaTime) {
		float newX = pos.x;
		newX += this.velocityX*deltaTime;
		
		rect.move(this.pos.x, this.pos.y);
		
		float newY = pos.y;
		newY += this.velocityY*deltaTime;
		
		if(map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			if(velocityX < 0) {
				this.pos.x = (float) Math.floor(pos.x);
			}
			this.velocityX = 0;
		}
		else {
			this.pos.x = newX;
		}
		
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			if(velocityY < 0) {
				this.pos.y = (float) Math.floor(pos.y);
			}
			this.velocityY = 0;
		}
		else {
			this.pos.y = newY;
		}
		timer();
		remainingStrayTime -= deltaTime;
		
		if(remainingRecoveryTime < RECOVERY_TIME/2 && this.state == "HURT")
			this.state = "IDLE";
		
		if(remainingRecoveryTime > 0)
			remainingRecoveryTime -= deltaTime;
		else if(remainingRecoveryTime < 0)
			remainingRecoveryTime = 0;
		
		if(this.HEALTH == 0)
			super.destroy = true;

		if(this.behavior == "PASSIVE" && this.state != "HURT") {
			if(remainingStrayTime > 0) {
				if(this.direction == 1) {
					moveX(-SPEED*deltaTime);
					this.state = "IDLE";
				}
				else if(this.direction == 2) {
					moveX(SPEED*deltaTime);
					this.state = "IDLE";
				}
				else if(this.direction == 3) {
					moveY(-SPEED*deltaTime);
					this.state = "IDLE";
				}
				else if(this.direction == 4) {
					moveY(SPEED*deltaTime);
					this.state = "IDLE";
				}
			}
			else if(remainingStrayTime < -MAX_STRAY_TIME) {
				this.direction = RandomNumGen.getRandomNumberInRange(1, 4);
				remainingStrayTime = MAX_STRAY_TIME;
			}
		}
	}
}
