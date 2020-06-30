package com.mygdx.game.entities.animations;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EntityAssetManager {
	private static HashMap<String,TextureRegion[][]> textures;
	private static Texture smallShadow = new Texture("ENTITIES_MISC/SHADOWS/SHADOW.png");
	static {
		textures = new HashMap<String,TextureRegion[][]>();
		textures.put("PLAYER/IDLE", TextureRegion.split(new Texture("PLAYER/IDLE/IDLE2.png"), 32, 32));
		textures.put("PLAYER/MOVE", TextureRegion.split(new Texture("PLAYER/MOVE/MOVE.png"), 32, 32));
//		textures.put("PLAYER/IDLE", TextureRegion.split(new Texture("PLAYER/IDLE/IDLE.png"), 64, 64));
		textures.put("PLAYER/HURT", TextureRegion.split(new Texture("PLAYER/HURT/HURT.png"), 64, 64));
		textures.put("PLAYER/A_KNIFE_BASIC", TextureRegion.split(new Texture("PLAYER/ATTACK/A_KNIFE_BASIC.png"), 32, 32));
		textures.put("ITEMS", TextureRegion.split(new Texture("ITEMS/ITEM_ICONS.png"), 16, 16));
		textures.put("GOBLIN/MOVE", TextureRegion.split(new Texture("PLAYER/IDLE/IDLE.png"), 64, 64));
		textures.put("SMALL_BUSH/IDLE", TextureRegion.split(new Texture("SMALL_BUSH/IDLE/IDLE.png"), 16, 28));
		textures.put("BUSH/IDLE", TextureRegion.split(new Texture("BUSH/IDLE/IDLE.png"), 32, 28));
		textures.put("BIG_TREE/IDLE", TextureRegion.split(new Texture("BIG_TREE/IDLE/IDLE.png"), 64, 64));
	}
	
	public static void addAnimation(String name, String state, int sizeX, int sizeY) {
		if(!textures.containsKey(name+"/"+state)) {
			System.out.println(name+"/"+state+" texture was added");
			textures.put(name+"/"+state, TextureRegion.split(new Texture(name+"/"+state+"/"+state+".png"), sizeX, sizeY));
		}
	}
	
	public static void dispose(String key) {
		if(textures.containsKey(key)) {
			TextureRegion[][] texture = textures.get(key);
			for(int i = 0; i<texture.length; i++)
				for(int b = 0; b<texture[0].length; b++) {
					texture[i][b].getTexture().dispose();
				}
		}
		
	}
	
	public static TextureRegion getTexture(String path, int frameNum, int direction) {
		TextureRegion[][] texture = textures.get(path);
		if(texture != null)
			return texture[direction][frameNum];
		return null;
	}
	
	public static Texture getShadow() {
		return smallShadow;
	}
}
