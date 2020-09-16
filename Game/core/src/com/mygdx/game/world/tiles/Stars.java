package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;

public class Stars extends AnimatedTile {

	public Stars(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 37;
		this.tileGroup = TileGroup.VOID;
		this.collidable = false;
		this.destroyable = false;
		this.replacable = false;
		this.connectable = false;
		this.liquid = false;
		this.startingFrame = 4;
		this.level = 1;
		this.animLen = 16;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		batch.draw(TileTextureManager.getLiquids(this.variation, this.frameNum+this.startingFrame), x, y);
	}

}
