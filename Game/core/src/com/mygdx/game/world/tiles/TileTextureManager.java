package com.mygdx.game.world.tiles;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.TileType;

public class TileTextureManager {
	private static TextureRegion[][] tiles;
	private static TextureRegion[][] liquids;
	public TileTextureManager() {
		tiles = TextureRegion.split(new Texture("MainNwwew.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
		liquids = TextureRegion.split(new Texture("liquid.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
	}
	
	public static TextureRegion getTex(int row, int col) {
		return 	fixBleeding(tiles[row][col]);
	}
	
	public static TextureRegion getLiquids(int row, int col) {
		return 	fixBleeding(liquids[row][col]);
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
