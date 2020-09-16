package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;

public class GreenGrass extends CustomTileType {
	public GreenGrass(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 4;
		this.name = "greenGrass";
		this.tileGroup = TileGroup.GRASS;
		this.collidable = false;
		this.destroyable = false;
		this.connectable = true;
	}
	
	@Override
	public void diverce() {
		if(this.variation == 1) {
			int random = RandomNumGen.getRandomNumberInRange(17, 21);
			if(random == 21)
				this.variation = 1;
			else
				this.variation = random;
		}
	}
}
