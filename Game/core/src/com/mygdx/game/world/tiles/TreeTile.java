package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.world.CustomGameMap;

public class TreeTile extends CustomTileType {
	public TreeTile(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 33;
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
//		batch.draw(TileTextureManager.getTree(0, 0), this.x-TileTextureManager.getTree(0, 0).getRegionWidth()/4, this.y+8);
//		batch.setColor(1.0f,1.0f,1.0f,1.0f);
	}
}
