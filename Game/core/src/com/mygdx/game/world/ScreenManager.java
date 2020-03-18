package com.mygdx.game.world;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class ScreenManager{
	TiledMap tiledMap;
	
	public enum screens {
		MAIN_MENU,
		PLAINS_1,
		PLAINS_2;
	}
	public ScreenManager(String level) {
		init(level);
	}
	private TiledGameMap init(String level) {
		return new TiledGameMap();
	}
}
