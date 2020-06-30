package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.items.Knife;
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
		System.out.println("destination");
	}
	
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		batch.draw(EntityAssetManager.getShadow(), pos.x-3, pos.y-1, 11, 3);
		animationPlay(batch);
	}

}
