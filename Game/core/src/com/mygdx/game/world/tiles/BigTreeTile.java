package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.TreeEntity;
import com.mygdx.game.world.CustomGameMap;

public class BigTreeTile extends CustomTileType {
	public BigTreeTile(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 36;
		this.group = "trees";
		this.collidable = false;
		this.destroyable = true;
		this.connectable = false;
		this.visible = true;
		this.replacable = true;
//		map.getEntities().add(new TreeEntity(this.x*16, this.y*16, EntityType.PINE, map, 353, this));
	}
	
	@Override
	public void render(SpriteBatch batch) {
//		batch.setColor(r,g,b,1.0f);
//		batch.draw(TileTextureManager.getTree2(0, 0), (this.x-TileTextureManager.getTree(0, 1).getRegionWidth()/2)-4, this.y+8);
//		batch.setColor(1.0f,1.0f,1.0f,1.0f);
	}
}
