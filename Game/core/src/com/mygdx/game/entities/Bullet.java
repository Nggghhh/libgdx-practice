package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.world.GameMap;

public abstract class Bullet extends Entity {
	protected String caster;
	protected int defaultStrenght;
	protected int strength;
	protected int HEALTH;
	protected float sliperyness = 1f;
	
	public Bullet(float x, float y, EntityType type, GameMap map, int id, String caster) {
		super(x, y, type, map, id);
		this.caster = caster;
		this.HEALTH = this.type.getHealth();
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
	public void attack(int damage, Entity hitter, int direction, String type) {
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
	
	public int getDefaultStrenght() {
		return defaultStrenght;
	}
	
	
	public void timer(float deltaTime, float sliperyness) {
		velocity.scl(1 - (sliperyness * deltaTime*2));
	}
}
