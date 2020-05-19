package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.world.CustomGameMap;

public class LanternTile extends CustomTileType {
	private Texture light;
	private boolean isEnabled = true;
	private int lightLen = 6;
	private float lightArray[][] = {
			{0.0f, 0.4f, 0.6f, 0.4f, 0.0f}, 
			{0.4f, 0.6f, 0.8f, 0.6f, 0.4f}, 
			{0.6f, 0.8f, 1.0f, 0.8f, 0.6f}, 
			{0.4f, 0.6f, 0.8f, 0.6f, 0.4f},
			{0.0f, 0.4f, 0.6f, 0.4f, 0.0f}
			};
	public LanternTile(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 7;
		this.group = "light";
		this.collidable = false;
		this.destroyable = true;
		this.connectable = false;
		this.visible = true;
		this.replacable = true;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(isEnabled) {
			r = 1.0f;
			g = 1.0f;
			b = 1.0f;
		}
		batch.setColor(r,g,b,1.0f);
		batch.draw(TileTextureManager.getLantern(), this.x, this.y+6);
		batch.setColor(1.0f,1.0f,1.0f,1.0f);
	}
	
	public void light(CustomGameMap map) {
		int originX = this.x/16;
		int originY = this.y/16;
		int playerLayer = map.getHero().getLayer();
		for(int i = 0; i<lightArray[0].length; i++) {
			int newx = i+originX-lightArray[0].length/2;
			for(int j = 0; j<lightArray.length; j++) {
				int newy = j+originY-lightArray.length/2;
				for(int l = playerLayer; l<playerLayer+2; l++) {
					TileGridCell cells = map.getCells()[newx][newy][l];
					cells.tile.addLight(lightArray[j][i], lightArray[j][i], lightArray[j][i]);
				}
			}
		}
	}
	
	public void drawLight(SpriteBatch batch) {
		batch.draw(light, this.x-16, this.y+6);
	}
}
