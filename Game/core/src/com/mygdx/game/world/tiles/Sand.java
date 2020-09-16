package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;

public class Sand extends CustomTileType {
	public Sand(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 3;
		this.tileGroup = TileGroup.SAND;
		this.collidable = false;
		this.destroyable = false;
		this.connectable = true;
	}
	
	@Override
	public void diverce() {
		if(this.variation == 1) {
			int random = RandomNumGen.getRandomNumberInRange(0, 40);
			if(random == 0)
				this.variation = 16;
			if(random == 1)
				this.variation = 17;
		}
	}
}
