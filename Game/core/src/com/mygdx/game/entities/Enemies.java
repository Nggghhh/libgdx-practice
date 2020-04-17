package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.GameMap;

public abstract class Enemies extends LivingEntity {
	
	private static final float MAX_STRAY_TIME = 1f;
	protected int SPEED;
	protected float FOV;
	
	protected float remainingStrayTime = 1f;
	protected float remainingAttackTime = 1f;
	protected Behavior behavior = Behavior.AGRESSIVE;
	public Enemies(float x, float y, EntityType type, GameMap map, int id) {
		super(x, y, type, map, id);
		// TODO Auto-generated constructor stub
	}
	
	protected enum Behavior{
		AGRESSIVE,
		PASSIVE;
	}
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
	}
	
	@Override
	public void livingState(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.livingState(camera, deltaTime, map);
		
		if(playerIsOnSight(FOV, map.getHero().getX(), map.getHero().getY()) && !map.getHero().getDestroyed())
			this.behavior = Behavior.AGRESSIVE;
		else
			this.behavior = Behavior.PASSIVE;
		
		if(remainingAttackTime > 0)
			remainingAttackTime -= deltaTime;
		if(this.behavior == behavior.PASSIVE)
			remainingStrayTime -= deltaTime;
		if(this.behavior == behavior.AGRESSIVE) {
			remainingStrayTime = MAX_STRAY_TIME;
			if(this.state != "HURT" && this.state != "ATTACK")
				turnTowardsPlayer(map);
		}

		if(this.state != "HURT" && this.state != "DEATH" && this.state != "ATTACK") {
			if(remainingStrayTime > 0) {
				changeState("MOVE", true, 1, 6);
				move(this.direction, deltaTime);
			}
			else if(remainingStrayTime < 0f && remainingStrayTime > -MAX_STRAY_TIME) {
				changeState("IDLE", true, 1, 2);
			}
			else if(remainingStrayTime < -MAX_STRAY_TIME) {
				this.direction = RandomNumGen.getRandomNumberInRange(1, 4);
				remainingStrayTime = MAX_STRAY_TIME;
			}
			else
				changeState("IDLE", true, 1, 2);
		}
	}
	public void move(int direction, float deltaTime) {
		if(this.direction == 1) {
			moveX(-SPEED*deltaTime);
		}
		else if(this.direction == 2) {
			moveX(SPEED*deltaTime);
		}
		else if(this.direction == 3) {
			moveY(-SPEED*deltaTime);
		}
		else if(this.direction == 4) {
			moveY(SPEED*deltaTime);
		}
	}
	
	public boolean playerIsOnSight(float FOV, float entityX, float entityY) {
		float distance = Vector2.dst(this.pos.x, this.pos.y, entityX, entityY);
		if(distance <= FOV)
			return true;
		return false;
	}
	
	public void turnTowardsPlayer(GameMap map) {
		if(this.getPos().x > map.getHero().getPos().x && this.getPos().x > map.getHero().getWidth()+map.getHero().getPos().x)
			this.setDirection(1);
		else if(this.getPos().x < map.getHero().getPos().x && this.getPos().x < map.getHero().getWidth()+map.getHero().getPos().x)
			this.setDirection(2);
		if(this.getPos().y > map.getHero().getPos().y && this.getPos().y > map.getHero().getHeight()+map.getHero().getPos().y)
			this.setDirection(3);
		else if(this.getPos().y < map.getHero().getPos().y && this.getPos().y < map.getHero().getHeight()+map.getHero().getPos().y)
			this.setDirection(4);
	}
}
