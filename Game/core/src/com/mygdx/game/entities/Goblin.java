package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

public class Goblin extends Enemies {
	
	public Goblin(float x, float y, EntityType type, GameMap map, int id) {
		super(x, y, type, map, id);
		this.direction = RandomNumGen.getRandomNumberInRange(1, 4);
		this.FOV = 200f;
		this.DEFAULT_SPEED = 50;
		this.SPEED = this.DEFAULT_SPEED;
		this.attackVelocity = 30f;
//		
//		animationLoader(this.type, "IDLE", 2, 4);
//		animationLoader(this.type, "MOVE", 2, 4);
//		animationLoader(this.type, "HURT", 3, 1);
//		animationLoader(this.type, "ATTACK", 3, 4);
//		animationLoader(this.type, "DEATH", 3, 1);
	}
	
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
		
		if(this.destroy == false) {
			livingState(camera, deltaTime, map);
		}
		else
			changeState("DEATH", false, 2, 6);
	}
	
	@Override
	public void livingState(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.livingState(camera, deltaTime, map);
		if(this.remainingAttackTime > 0)
			this.behavior = behavior.RETREAT;
		else
			this.behavior = behavior.PASSIVE;
		
		if(map.getHero().isDestroyed() == false) {
			if(this.rect.collidesWithAtOffset(map.getHero().getRect(), this.direction, 30, 3) && remainingAttackTime < 0) {
				remainingAttackTime = 1f;
				changeState("ATTACK", false, 2, 6);
			}
			if(this.state == "ATTACK")
				attack(1, this, this.attackVelocity, "stab");
		}
	}
	
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animationPlay(batch);
	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
		if(this.getRect().collidesWithAtOffset(map.getHero().getRect(), this.direction, 12, 0) && (this.frameNum == 1 || this.frameNum == 2))
			map.getHero().hurt(damage, hitter, map.getHero());
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
	
	public void strafe(float distance, float x, float y) {
		this.velocity.x = 0;
		this.velocity.y = 0;
        float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, x, y);
        float ratio = distance/distanceBetweenEntities;
		this.velocity.x = -((pos.x-x)*ratio*100);
		this.velocity.y = -((pos.y-y)*ratio*100);
	}

	@Override
	public void recreate(int x, int y, int health) {
		this.pos.x = x;
		this.pos.y = y;
		this.HEALTH = health;
		this.destroy = false;
	}

}
