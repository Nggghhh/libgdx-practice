package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.CustomGameMap;

public class ShallowWater extends AnimatedTile {
	public ShallowWater(String name, int x, int y, CustomGameMap map) {
		super(name, x, y, map);
		this.id = 2;
		this.group = "water";
		this.collidable = false;
		this.destroyable = false;
		this.replacable = false;
		this.connectable = true;
		this.liquid = true;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		batch.draw(TileTextureManager.getLiquids(this.variation, this.frameNum+this.startingFrame), x, y);
	}
}
