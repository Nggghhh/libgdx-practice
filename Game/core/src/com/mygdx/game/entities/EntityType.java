package com.mygdx.game.entities;

public enum EntityType {
	DAGGER("DAGGER", 8, 3, 32, 3, 4),
	
	PLAYER("PLAYER", 8, 6, 6, 28, 12),
	GOBLIN("GOBLIN", 8, 6, 2, 28, 12);
	
	private String name;
	private int width, height, health, pivotPointX, pivotPointY;
	
	private EntityType(String name, int width, int height, int health, int x, int y) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.health = health;
		this.pivotPointX = x;
		this.pivotPointY = y;
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
	
	public int getPivotX() {
		return pivotPointX;
	}
	
	public int getPivotY() {
		return pivotPointY;
	}
}
