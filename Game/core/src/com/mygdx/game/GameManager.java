package com.mygdx.game;

import java.util.ArrayList;
import java.util.Base64;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.Player;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.items.Items;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

public class GameManager {
	private FileHandle playerInfo = Gdx.files.local("bin/playerData.json");
	private FileHandle playerInventory = Gdx.files.local("bin/playerInventory.json");
	private JsonValue jsonValue;
	private JsonReader jsonReader;
	
	private Json json;
	
	public GameManager(GameMap gameMap) {
		json = new Json();
		jsonReader = new JsonReader();
		if (!playerInfo.exists() && !playerInventory.exists()) {
			saveData(gameMap.getHero(), gameMap.getHero().getPlayerInventory());
		}
//		else {
//			loadData(gameMap.getHero(), gameMap);
//		}
	}

	public void saveData(Player entity, Inventory inventory) {
		if (entity != null && inventory != null) {
			playerInfo.writeString(Base64Coder.encodeString(json.prettyPrint(entity)), false);
			playerInventory.writeString(Base64Coder.encodeString(json.prettyPrint(inventory)), false);;
		}
	}

	public void loadData(Player entity, GameMap gameMap) {
		jsonValue = jsonReader.parse(Base64Coder.decodeString(playerInfo.readString()));
		int health = jsonValue.get("HEALTH").asInt();
		float posX = jsonValue.get("pos").require("x").asFloat();
		float posY = jsonValue.get("pos").require("y").asFloat();
		JsonValue name = jsonValue.get("playerInventory").require("inventory");
//		Inventory inventory = json.fromJson(Inventory.class, Base64Coder.decodeString(playerInventory.readString()));
		
		
		gameMap.getHero().setPos(posX, posY);
		gameMap.getHero().setHealth(health);
//		gameMap.getHero().getPlayerInventory().getItemList().get(0).setType(EntityType.valueOf(name.require(0).require("type").asString()));
		
//		gameMap.getHero().getPlayerInventory().getItemList().removeAll(gameMap.getHero().getPlayerInventory().getItemList());
//		gameMap.getHero().getPlayerInventory().getItemList().add(new Items(0, 0, EntityType.valueOf(name.require(0).require("type").asString()), gameMap, 2222));
	}
	
	public void printJson() {
		jsonValue = jsonReader.parse(Base64Coder.decodeString(playerInfo.readString()));
		int health = jsonValue.get("HEALTH").asInt();
		float posX = jsonValue.get("pos").require("x").asFloat();
		float posY = jsonValue.get("pos").require("y").asFloat();
		JsonValue name = jsonValue.get("playerInventory").require("inventory");
		System.out.println(posX+" // "+posY+" // "+health);
	}
}