package com.mygdx.game.world.tiles;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.TileType;

public class TileTextureManager {
	private TextureRegion[][] tiles;
	private static CustomTileType[] tileList;
	public TileTextureManager() {
		tileList = new CustomTileType[32];
		tileList[0] = new Air("air", 0, 0);
		tileList[1] = new DeepWater("deepWater", 0, 0);
		tileList[2] = new ShallowWater("shallowWater", 0, 0);
		tileList[3] = new Sand("sand", 0, 0);
		tileList[4] = new GreenGrass("greenGrass", 0, 0);
		tileList[5] = new Rock("rock", 0, 0);
		tiles = TextureRegion.split(new Texture("MainNwwew.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
	}
	
	public TextureRegion getTex(int row, int col) {
		return 	fixBleeding(tiles[row][col]);
	}
	
	public static CustomTileType getTile(int id) {
		return tileList[id];
	}
	
	public static TextureRegion fixBleeding(TextureRegion region) {
        float fix = 0.01f;
        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight); // Trims Region
        return region;
    }
}
