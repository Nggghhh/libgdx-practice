package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Enemies.Behavior;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public abstract class AIEntities extends LivingEntity {
	protected int SPEED = 20;
	protected int RUN_SPEED = 40;
	protected float FOV;
	protected Vector2 destination;
	protected Behavior behavior = Behavior.AGRESSIVE;
	
	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
	}
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
		
		if(!this.destroy) {
			livingState(camera, deltaTime, map);
		}
		else {
			changeState("DEATH", false, 2, 6);
		}
	}
	
	@Override
	public void livingState(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.livingState(camera, deltaTime, map);
		
		if(destination != null) {
			angle = (int) calculateAngle(destination.x, destination.y);
			this.move(angle, SPEED, deltaTime);
		}
	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
		// TODO Auto-generated method stub

	}
	
	public abstract void findDestination(CustomGameMap map);

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}

}
