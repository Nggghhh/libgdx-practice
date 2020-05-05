package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.CustomGameMap;

public class Air extends CustomTileType {

	public Air(String name, int x, int y, CustomGameMap map) {
		super(name, x, y, map);
		this.id = 0;
		this.collidable = false;
		this.destroyable = false;
		this.replacable = true;
		this.visible = false;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
	}
}
