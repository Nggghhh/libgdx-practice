package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.world.CustomGameMap;

public class SmallBushTile extends CustomTileType {
	public SmallBushTile(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 35;
		this.group = "trees";
		this.collidable = false;
		this.destroyable = true;
		this.connectable = false;
		this.visible = true;
		this.replacable = true;
	}
	
	@Override
	public void render(SpriteBatch batch) {
//		batch.setColor(r,g,b,1.0f);
//		batch.draw(TileTextureManager.getTree(0, 1), this.x-TileTextureManager.getTree(0, 1).getRegionWidth()/4, this.y+8);
//		batch.setColor(1.0f,1.0f,1.0f,1.0f);
	}

}
