package com.mygdx.game.entities.animations;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EntityAssetManager {
	private static HashMap<String,TextureRegion[][]> textures;
	public EntityAssetManager() {
		textures = new HashMap<String,TextureRegion[][]>();
		textures.put("PLAYER/IDLE", TextureRegion.split(new Texture("PLAYER/IDLE/IDLE2.png"), 32, 32));
		textures.put("PLAYER/MOVE", TextureRegion.split(new Texture("PLAYER/MOVE/MOVE.png"), 32, 32));
//		textures.put("PLAYER/IDLE", TextureRegion.split(new Texture("PLAYER/IDLE/IDLE.png"), 64, 64));
		textures.put("PLAYER/HURT", TextureRegion.split(new Texture("PLAYER/HURT/HURT.png"), 64, 64));
		textures.put("ITEMS", TextureRegion.split(new Texture("ITEMS/ITEM_ICONS.png"), 16, 16));
		textures.put("GOBLIN/MOVE", TextureRegion.split(new Texture("PLAYER/IDLE/IDLE.png"), 64, 64));
		textures.put("SMALL_BUSH/IDLE", TextureRegion.split(new Texture("SMALL_BUSH/IDLE/IDLE.png"), 16, 28));
		textures.put("BUSH/IDLE", TextureRegion.split(new Texture("BUSH/IDLE/IDLE.png"), 32, 28));
	}
	
	public static void addAnimation(String name, String state) {
		if(textures.containsKey(name+"/"+state))
			textures.put(name+"/"+state, TextureRegion.split(new Texture(name+"/"+state+"/"+state+".png"), 64, 64));
	}
	
	public static TextureRegion getTexture(String path, int frameNum, int direction) {
		TextureRegion[][] texture = textures.get(path);
		if(texture != null)
			return texture[direction][frameNum];
		return null;
	}
}
