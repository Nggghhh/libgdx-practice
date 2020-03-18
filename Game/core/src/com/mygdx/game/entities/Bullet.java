package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.world.GameMap;

public class Bullet extends Entity {

	public Bullet(float x, float y, EntityType type, GameMap map) {
		super(x, y, type, map);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(OrthographicCamera camera, float deltaTime) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}
}
