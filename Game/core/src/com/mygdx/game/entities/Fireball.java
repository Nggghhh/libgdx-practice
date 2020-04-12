package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.GameMap;

public class Fireball extends Bullet {
	public Fireball(float x, float y, EntityType type, GameMap map, int id, String caster) {
		super(x, y, type, map, id, caster);
		
		animationLoader(this.type, "IDLE", 4, 1);
		changeState("IDLE", true, 3, 6);
		this.sliperyness = 1f;
		this.defaultStrenght = 1;
	}
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
		
		float newX = pos.x;
		newX += this.velocity.x*deltaTime;

		//apply velocity on Y axis
		float newY = pos.y;
		newY += this.velocity.y*deltaTime;
		//check if collision with solid tile on Y axis occurred
		if(map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			this.pos.x = (float) Math.floor(pos.x);
			this.HEALTH -= 1;
			this.velocity.x *= -1;
		}
		else {
			this.pos.x = newX;
		}

		//check if collision with solid tile on X axis occurred
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = (float) Math.floor(pos.y);
			this.HEALTH -= 1;
			this.velocity.y *= -1;
		}
		else {
			this.pos.y = newY;
		}

		//linear damping, adding drag to object velocity
		timer(deltaTime, sliperyness);

		//destroy after flinch animation is ended
		if(this.HEALTH < 0 && this.state != "HURT") {
		}
		
		for(Entity entity : map.getEntities())
			if(this.caster != entity.getType().getName() && entity.getId() != this.getId() && entity instanceof LivingEntity && !entity.getDestroyed())
				if(this.rect.collidesWith(entity.getRect())) {
					this.push(entity.getX(), entity.getY(), entity.getType().getWeight());
					entity.hurt(1, this, entity);
				}
	}

	@Override
	public void hurt(int damage, Entity hitter, Entity receiver) {
		// TODO Auto-generated method stub

	}
	
	public void destroy() {
		this.disabled = true;
	}

	@Override
	public void attack(int damage, Entity hitter, int direction, String type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		super.render(batch, camera);
	}

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void push(float x, float y, float distance) {
        if(this.pos.x != x && this.pos.y != y) {
            float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, x, y);
            float ratio = distance/distanceBetweenEntities;
        	this.velocity.x = (this.pos.x-x)*ratio*100;
        	this.velocity.y = (this.pos.y-y)*ratio*100;
        }
        else {
            float distanceBetweenEntities = Vector2.dst(this.pos.x-1f, this.pos.y-1f, x+1f, y+1f);
            float ratio = distance/distanceBetweenEntities;
        	this.velocity.x = (this.pos.x-x)*ratio*100;
        	this.velocity.y = (this.pos.y-y)*ratio*100;
        }
	}
}
