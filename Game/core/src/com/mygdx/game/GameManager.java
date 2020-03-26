package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TiledGameMap;

public class GameManager {
	private FileHandle fileHandle = Gdx.files.local("bin/playerData.json");
	private JsonValue jsonValue;
	private JsonReader jsonReader;
	
	private Json json;
	
	public GameManager(GameMap gameMap) {
		json = new Json();
		jsonReader = new JsonReader();
		if (!fileHandle.exists()) {
			saveData(gameMap.getHero());
		}
		else {
			loadData(gameMap.getHero(), gameMap);
		}
	}

	public void saveData(Entity entity) {
		if (entity != null) {
			fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(entity)), false);
		}
	}

	public void loadData(Entity entity, GameMap gameMap) {
		jsonValue = jsonReader.parse(Base64Coder.decodeString(fileHandle.readString()));
		int health = jsonValue.get("HEALTH").asInt();
		float posX = jsonValue.get("pos").require("x").asFloat();
		float posY = jsonValue.get("pos").require("y").asFloat();
		gameMap.getHero().setPos(posX, posY);
	}
	
	public void printJson() {
		jsonValue = jsonReader.parse(Base64Coder.decodeString(fileHandle.readString()));
		int health = jsonValue.get("HEALTH").asInt();
		float posX = jsonValue.get("pos").require("x").asFloat();
		float posY = jsonValue.get("pos").require("y").asFloat();
		System.out.println(posX+" // "+posY+" // "+health);
//		System.out.println(Base64Coder.decodeString(fileHandle.readString()));
	}
}