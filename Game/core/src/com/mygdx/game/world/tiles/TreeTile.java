package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.world.CustomGameMap;

public class TreeTile extends CustomTileType {
	private Texture tree;
	public TreeTile(String name, int x, int y, CustomGameMap map) {
		super(name, x, y, map);
		this.id = 6;
		this.group = "trees";
		this.collidable = false;
		this.destroyable = true;
		this.connectable = false;
		this.visible = true;
		this.replacable = true;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(TileTextureManager.getTree(), this.x, this.y);
	}
}
