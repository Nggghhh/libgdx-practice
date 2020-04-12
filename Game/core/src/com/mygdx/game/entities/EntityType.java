package com.mygdx.game.entities;

public enum EntityType {
	EMPTY_SPACE("EMPTY_SPACE", 0, 0, 0, 0, 0, 0),
	DAGGER("DAGGER", 8, 3, 32, 3, 4, 0.5f),
	SHORT_SWORD("SWORD", 8, 3, 32, 3, 4, 0.8f),
	RUBY("RUBY", 8, 3, 32, 3, 4, 0.2f),
	
	FIREBALL("FIEBALL", 8, 3, 4, 3, 4, 1.0f),
	
	PLAYER("PLAYER", 8, 6, 6, 28, 12, 0.5f),
	GOBLIN("GOBLIN", 8, 6, 2, 28, 12, 0.5f);
	
	private String name;
	private int width, height, health, pivotPointX, pivotPointY;
	float weight;
	
	private EntityType(String name, int width, int height, int health, int x, int y, float weight) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.health = health;
		this.pivotPointX = x;
		this.pivotPointY = y;
		this.weight = weight;
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
	
	public float getWeight() {
		return weight;
	}
}
