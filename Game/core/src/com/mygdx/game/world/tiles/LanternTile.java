package com.mygdx.game.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.world.CustomGameMap;

public class LanternTile extends CustomTileType {
	private Texture light;
	private boolean isEnabled = true;
	private int lightLen = 64;
	private float[][] lightArray = {
			{0.0f, 0.2f, 0.4f, 0.4f, 0.4f, 0.2f, 0.0f},
			{0.2f, 0.6f, 1.0f, 1.0f, 1.0f, 0.6f, 0.2f}, 
			{0.4f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.4f}, 
			{0.4f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.4f}, 
			{0.4f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.4f}, 
			{0.2f, 0.6f, 1.0f, 1.0f, 1.0f, 0.6f, 0.2f},
			{0.0f, 0.2f, 0.4f, 0.4f, 0.4f, 0.2f, 0.0f}
			};
	public LanternTile(int x, int y, CustomGameMap map) {
		super(x, y, map);
		this.id = 34;
		this.tileGroup = TileGroup.LIGHT;
		this.collidable = false;
		this.destroyable = true;
		this.connectable = false;
		this.visible = true;
		this.replacable = true;
		this.transparent = true;
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
		int playerLayer = 0;
		for(int i = 0; i<lightArray[0].length; i++) {
			int newx = i+originX-lightArray[0].length/2;
			for(int j = 0; j<lightArray.length; j++) {
				int newy = j+originY-lightArray.length/2;
				for(int l = playerLayer; l<playerLayer+2; l++) {
					try {
						TileGridCell cells = map.getCells()[newx][newy][l];
						if(cells.tile != null)
						cells.tile.addLight(lightArray[j][i], lightArray[j][i], lightArray[j][i]);
					}
					catch(IndexOutOfBoundsException ignored) { }
				}
			}
		}
	}
	
	public void light2(CustomGameMap map) {
		for(int i = 0; i<360; i += 16)
		light3(i, map);
	}
	
	public void light3(int angle, CustomGameMap map) {
		double originX = this.x+8;
		double originY = this.y+8;
		int playerLayer = 0;
		double newAngle = Math.toRadians(angle);
		for(int len = 0; len<lightLen; len += 1) {
			double x = originX + len;
			double y = originY + len;
			float newLight = 1.0f;
			float newx =(float) (Math.cos(newAngle)*(x-originX) - Math.sin(newAngle)*(y-originY)+originX);
			float newy =(float) (Math.sin(newAngle)*(x-originX) + Math.cos(newAngle)*(y-originY)+originY);
			try {
				if(map.getCells()[(int) newx/16][(int) newy/16][playerLayer+1].tile.isCollidable()) {
					TileGridCell cells = map.getCells()[(int) newx/16][(int) newy/16][playerLayer+1];
					cells.tile.addLight(newLight, newLight, newLight);
					break;
				} else {
					for(int l = playerLayer; l<playerLayer+2; l++) {
						TileGridCell cells = map.getCells()[(int) newx/16][(int) newy/16][l];
						cells.tile.addLight(newLight, newLight, newLight);
						cells.tile.adjustLight(map.getInGameTime().getLight());
						cells.tile.updateLight();
					}
				}
			} catch(IndexOutOfBoundsException ignored) { }
		}
	}
	
	public void drawLight(SpriteBatch batch) {
		batch.draw(light, this.x-16, this.y+6);
	}
}
