package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.CustomGameMap;

public class GreenGrass extends CustomTileType {
	public GreenGrass(String name, int x, int y, CustomGameMap map) {
		super(name, x, y, map);
		this.id = 4;
		this.group = "grass";
		this.collidable = false;
		this.destroyable = false;
		this.connectable = true;
	}
}
