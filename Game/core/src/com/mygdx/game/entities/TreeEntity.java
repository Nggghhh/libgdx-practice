package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.tiles.BigTreeTile;
import com.mygdx.game.world.tiles.CustomTileType;
import com.mygdx.game.world.tiles.WoodWall;

public class TreeEntity extends Entity {
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

}
