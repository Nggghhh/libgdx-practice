package com.mygdx.game.world;

import java.util.HashMap;

public enum TileType {
	
	COLLISION(13, true, "COLLISION", 0, false),
	FREESPACE(14, false, "FREESPACE", 0, false);
	
	public static final int TILE_SIZE = 16;
	private int id;
	private boolean collidable;
	private String name;
	private float damage;
	private boolean filled;
	
	private TileType(int id, boolean collidable, String name, float damage, boolean filled) {
		this.id = id;
		this.collidable = collidable;
		this.name = name;
		this.damage = damage;
		this.filled = false;
	}
	public int getId() {
		return id;
	}
	public void setFilled() {
		this.filled = true;
	}
	public void setFree() {
		this.filled = false;
	}
	public boolean isCollidable() {
		return collidable;
	}
	public boolean isFilled() {
		return filled;
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
