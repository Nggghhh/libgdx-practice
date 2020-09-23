package com.mygdx.game.entities.projectiles;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.LivingEntity;
import com.mygdx.game.entities.projectiles.Bullet;
import com.mygdx.game.world.GameMap;

public class MagicArrow extends Bullet {
	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
		changeState("IDLE", true, 3, 6);
		this.sliperyness = 1f;
		this.defaultStrenght = 1;
		this.caster = snapshot.getString("caster", "nobody");
		System.out.println(caster);
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
	}

	@Override
	public void hurt(int damage, Entity hitter, Entity receiver) {
		// TODO Auto-generated method stub

	}
	
	public void destroy() {
		this.disabled = true;
	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
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
	public EntitySnapshot getSaveSnapshot() {
		EntitySnapshot entitySnapshot = super.getSaveSnapshot();
		entitySnapshot.putString("caster", caster);
		return entitySnapshot;
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
        	this.velocity.x = ((this.pos.x-1f)-x)*ratio*100;
        	this.velocity.y = ((this.pos.y-1f)-y)*ratio*100;
        }
	}
}
