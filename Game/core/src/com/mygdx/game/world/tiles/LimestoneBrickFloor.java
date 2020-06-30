package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.RandomNumGen;
import com.mygdx.game.world.CustomGameMap;

public class LimestoneBrickFloor extends CustomTileType {
	public LimestoneBrickFloor(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 12;
		this.group = "rock";
		this.collidable = true;
		this.destroyable = true;
		this.connectable = true;
		this.replacable = true;
	}
	
	@Override
	public void diverce() {
		int random = RandomNumGen.getRandomNumberInRange(0, 3);
		
		switch(this.variation) {
		case (0):
			if(random == 1)
				this.variation = 16;
		break;
		
		case (1):
			if(random == 1)
				this.variation = 17;
		break;
		
		case (2):
			if(random == 1)
				this.variation = 18;
		break;
		}
	}
}
