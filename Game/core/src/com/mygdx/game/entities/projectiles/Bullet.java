package com.mygdx.game.entities.projectiles;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.world.GameMap;

public abstract class Bullet extends Entity {
	protected String caster;
	protected int defaultStrenght;
	protected int strength;
	protected int HEALTH;
	protected float sliperyness = 1f;
	
	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
//		this.caster = snapshot.getString("caster", "nobody");
	}
	
	

	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
		
	}

	@Override
	public void hurt(int damage, Entity hitter, Entity receiver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animationPlay(batch);
	}

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void push(float x, float y, float distance) {
		// TODO Auto-generated method stub

	}

	public String getCaster() {
		return caster;
	}

	public void setCaster(String caster) {
		this.caster = caster;
	}

	public int getDefaultStrenght() {
		return defaultStrenght;
	}
	
	
	public void linearDumping(float deltaTime, float sliperyness) {
		velocity.scl(1 - (sliperyness * deltaTime*2));
	}
}
