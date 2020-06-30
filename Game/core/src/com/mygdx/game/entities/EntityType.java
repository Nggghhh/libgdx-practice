package com.mygdx.game.entities;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.mygdx.game.items.WoodPlanks;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;;

public enum EntityType {
//	DAGGER("DAGGER", Dagger.class, 8, 3, 32, 3, 4, 0.5f),
//	SHORT_SWORD("SWORD", ShortSword.class, 8, 3, 32, 3, 4, 0.8f),
//	RUBY("RUBY", 8, 3, 32, 3, 4, 0.2f),
	WOODPLANK("WOODPLANK", WoodPlanks.class, 8, 16, 32, 0, 0, 1f),
	BIG_TREE("BIG_TREE", BigTreeEntity.class, 10, 6, 6, 24, 3, 1f),
	BUSH("BUSH", TreeEntity.class, 10, 13, 8, 10, 2, 1.0f),
	SMALL_BUSH("SMALL_BUSH", SmallBush.class, 8, 4, 4, 4, 2, 1.0f),
	PLAYER("PLAYER", Player.class, 8, 4, 6, 13, 3, 0.5f),
	SAND_CRAB("SAND_CRAB", SandCrab.class, 8, 4, 6, 13, 3, 0.5f);
	
	private Class<? extends Entity> loaderClass;
	private String name;
	private int width, height, health, pivotPointX, pivotPointY;
	float weight;
	
	private EntityType(String name, Class<? extends Entity> loaderClass, int width, int height, int health, int x, int y, float weight) {
		this.name = name;
		this.loaderClass = loaderClass;
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
	
	public static Entity createEntityUsingSnapshot (EntitySnapshot eS, CustomGameMap map) {
		System.out.println(eS);
		EntityType type = entityTypes.get(eS.type);
		if(type != null) {
			try {
				Entity entity = ClassReflection.newInstance(type.loaderClass);
				entity.create(eS, type, map);
				return entity;
			} catch (ReflectionException e) {
				Gdx.app.error("Entity loader", "Could not load entity of type" + type.name);
				return null;
			}
		}
		return null;
	}
	
	private static HashMap<String, EntityType> entityTypes;
	
	static {
		entityTypes = new HashMap<String, EntityType>();
		for(EntityType type : EntityType.values())
			entityTypes.put(type.name, type);
	}
	
}
