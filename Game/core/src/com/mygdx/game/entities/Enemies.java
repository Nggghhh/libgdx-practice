package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.GameMap;

public abstract class Enemies extends LivingEntity {
	
	private static final float MAX_STRAY_TIME = 1f;
	private static final int SPEED = 20;
	protected float remainingStrayTime = 1f;
	protected String behavior = "AGRESSIVE";
	public Enemies(float x, float y, EntityType type, GameMap map) {
		super(x, y, type, map);
		// TODO Auto-generated constructor stub
	}
	
	public enum Behavior{
		AGRESSIVE,
		PASSIVE;
	}
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime) {
		super.update(camera, deltaTime);

		remainingStrayTime -= deltaTime;
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
		if(this.behavior == "AGRESSIVE" && this.state != "HURT") {
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
	}
}
