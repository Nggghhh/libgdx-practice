package com.mygdx.game.world;

import java.util.HashMap;

public enum TileType {
	
	SANDSTONE_DARK(1, false, "Sandstone"),
	SANDSTONE_FRAME(2, false, "Sandstone"),
	GRASS_0(42, false, "Grass"),
	GRASS_1(43, false, "Grass"),
	GRASS_2(44, false, "Grass"),
	GRASS_3(74, false, "Grass"),
	GRASS_4(75, false, "Grass"),
	GRASS_5(76, false, "Grass"),
	GRASS_6(106, false, "Grass"),
	GRASS_7(107, false, "Grass"),
	GRASS_8(108, false, "Grass"),
	COLLISION(13, true, "COLLISION"),
	FLOWERS(78, false, "Flowers");
	
	public static final int TILE_SIZE = 16;
	private int id;
	private boolean collidable;
	private String name;
	private float damage;
	
	private TileType(int id, boolean collidable, String name) {
		this(id, collidable, name, 0);
	}
	private TileType(int id, boolean collidable, String name, float damage) {
		this.id = id;
		this.collidable = collidable;
		this.name = name;
		this.damage = damage;
	}
	public int getId() {
		return id;
	}
	public boolean isCollidable() {
		return collidable;
	}
	public String getName() {
		return name;
	}
	public float getDamage() {
		return damage;
	}
	
	private static HashMap<Integer, TileType> tileMap;
	
	static {
		tileMap = new HashMap<Integer, TileType>();
		for (TileType tileType:TileType.values()) {
			tileMap.put(tileType.getId(), tileType);
		}
	}
	public static TileType getTileTypeById(int id) {
		return tileMap.get(id);
	}
}
