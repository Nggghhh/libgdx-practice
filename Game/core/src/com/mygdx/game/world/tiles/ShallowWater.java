package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.CustomGameMap;

public class ShallowWater extends AnimatedTile {
	public ShallowWater(int x, int y, CustomGameMap map) {
		super(x, y, map);
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
		batch.setColor(r,g,b,1.0f);
		batch.draw(TileTextureManager.getLiquids(this.variation, this.frameNum+this.startingFrame), x, y);
		batch.setColor(1.0f,1.0f,1.0f,1.0f);
	}
}
