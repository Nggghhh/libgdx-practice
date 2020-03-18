package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.GameMap;

public class Goblin extends Enemies {
	
	public Goblin(float x, float y, GameMap map) {
		super(x, y, EntityType.GOBLIN, map);
		this.direction = RandomNumGen.getRandomNumberInRange(1, 4);
	}
	
	public void update(OrthographicCamera camera, float deltaTime) {
		super.update(camera, deltaTime);
	}
	
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animation(this.type, batch);
	}

	@Override
	public void attack(int damage, Entity hitter, int direction, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recreate(int x, int y, int health) {
		this.pos.x = x;
		this.pos.y = y;
		this.HEALTH = health;
		this.destroy = false;
	}

}
