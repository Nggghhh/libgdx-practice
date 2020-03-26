package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

public class Goblin extends Enemies {
	
	public Goblin(float x, float y, EntityType type, GameMap map, int id) {
		super(x, y, type, map, id);
		this.direction = RandomNumGen.getRandomNumberInRange(1, 4);
	}
	
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		if(this.destroy == false) {
			super.update(camera, deltaTime, map);
			if(map.getHero().getDestroyed() == false) {
				if(this.getRect().collidesWithAtOffset(map.getHero().getRect(), this.direction, 22, 0) && remainingAttackTime < 0) {
					remainingAttackTime = 2f;
					changeState("ATTACK", false, 2, 6);
				}
				if(this.state == "ATTACK")
					attack(1, this, 30, "stab");
			}
		}
		else
			changeState("DEATH", false, 2, 6);
	}
	
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animation(this.type, batch);
	}

	@Override
	public void attack(int damage, Entity hitter, int velocity, String type) {
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

	@Override
	public void recreate(int x, int y, int health) {
		this.pos.x = x;
		this.pos.y = y;
		this.HEALTH = health;
		this.destroy = false;
	}

}
