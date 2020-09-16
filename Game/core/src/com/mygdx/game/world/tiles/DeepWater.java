package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;

public class DeepWater extends AnimatedTile {
	public DeepWater(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 1;
		this.tileGroup = TileGroup.WATER;
		this.collidable = true;
		this.destroyable = false;
		this.replacable = false;
		this.connectable = true;
		this.liquid = true;
		this.startingFrame = 4;
		this.level = 1;
//		this.frameNum = RandomNumGen.getRandomNumberInRange(0, 3);
		this.frameNum = 0;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		batch.draw(TileTextureManager.getLiquids(this.variation, this.frameNum+this.startingFrame), x, y);
	}
}
