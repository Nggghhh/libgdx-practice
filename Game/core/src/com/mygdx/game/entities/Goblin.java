package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.GameMap;

public class Goblin extends Enemies {
	
	public Goblin(float x, float y, GameMap map) {
		super(x, y, EntityType.GOBLIN, map);
		this.direction = RandomNumGen.getRandomNumberInRange(1, 4);
	}
	
	public void update(float deltaTime) {
//		if(Gdx.input.isKeyPressed(Keys.A)) {
//			moveX(-SPEED*deltaTime);
//			this.direction = 1;
//			this.state = "IDLE";
//		}
//		if(Gdx.input.isKeyPressed(Keys.D)) {
//			moveX(+SPEED*deltaTime);
//			this.direction = 2;
//			this.state = "IDLE";
//		}
//		if(Gdx.input.isKeyPressed(Keys.S)) {
//			moveY(-SPEED*deltaTime);
//			this.direction = 3;
//			this.state = "IDLE";
//		}
//		if(Gdx.input.isKeyPressed(Keys.W)) {
//			moveY(+SPEED*deltaTime);
//			this.direction = 4;
//			this.state = "IDLE";
//		}
		super.update(deltaTime);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		animation(this.type, batch);
	}

	@Override
	public void attack(int damage, Entity hitter, int direction) {
		// TODO Auto-generated method stub
		
	}

}
