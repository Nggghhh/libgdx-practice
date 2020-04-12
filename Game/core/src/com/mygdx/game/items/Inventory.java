package com.mygdx.game.items;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.Player;
import com.mygdx.game.world.GameMap;

public class Inventory {
	protected int maxInventorySize = 10;
	protected ArrayList<Items> inventory;
	public Inventory(GameMap map) {
		inventory = new ArrayList<Items>();
		inventory.add(new Items(215, 265, EntityType.DAGGER, map, 224));
		inventory.add(new Items(215, 265, EntityType.SHORT_SWORD, map, 222));
	}
	
	public void collectInput(GameMap map) {
		if(Gdx.input.isKeyJustPressed(Keys.M))
			dropItem(0, map);
		if(Gdx.input.isKeyJustPressed(Keys.N)) {
			printInventory();
		}
	}
	
	public void dropItem(int indexOfItem, GameMap map) {
		if(!inventory.isEmpty()) {
			Entity item = inventory.get(indexOfItem);
			if(item != null && item.getType() != EntityType.EMPTY_SPACE) {
				map.getEntities().add(item);
				item.setDisabled(false);
				item.setPos(map.getHero().getX(), map.getHero().getY());
				item.throwEntity(map.getHero(), 100f);
				inventory.remove(item);
			}
		}
	}
	
	public void printInventory() {
		for(Items item : inventory)
			System.out.println(inventory.indexOf(item)+". "+item.getType().getName()+" "+item.isDisabled());
	}
	
	public ArrayList<Items> getItemList() {
		return inventory;
	}
	
	public int getSize() {
		return maxInventorySize;
	}
}
