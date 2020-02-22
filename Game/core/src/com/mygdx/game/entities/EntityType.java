package com.mygdx.game.entities;

public enum EntityType {
	PLAYER("PLAYER", 8, 6, 6),
	GOBLIN("GOBLIN", 8, 6, 2);
	
	private String name;
	private int width, height, health;
	
	private EntityType(String name, int width, int height, int health) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.health = health;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getHealth() {
		return health;
	}
}
