package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.entities.playerchar.Player;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public class SandCrab extends AIEntities {
	
	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
		this.SPEED = 20;
		EntityAssetManager.addAnimation(type.getName(), "IDLE", 32, 32);
		changeState("IDLE", true, 1, 2);
		findDestination((CustomGameMap) map);
	}

	@Override
	public void findDestination(CustomGameMap map) {
		Player player = map.getHero();
		this.destination = player.getPos();
	}
	
	@Override
	public void move(int angleToTransform, int speed, float delta) {
		super.move(angleToTransform, speed, delta);
		changeState("MOVE", true, 6, 7);
	}
	
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animationPlay(batch);
	}

}
