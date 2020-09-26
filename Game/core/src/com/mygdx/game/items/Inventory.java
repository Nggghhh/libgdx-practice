package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.world.GameMap;

public class Inventory {
	protected int maxInventorySize = 10;
	private int pointer = 0;
	public Items[] inventory = new Items[maxInventorySize];
	public Inventory() {
		printInventory();
	}
	
	public void collectInput(GameMap map) {
		if(Gdx.input.isKeyJustPressed(Keys.M)) {
			dropItem(pointer, map);
		}
		if(Gdx.input.isKeyJustPressed(Keys.N)) {
			printInventory();
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			movePointerToTheRight();
		}
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			movePointerToTheLeft();
		}
	}
	
	public void dropItem(int indexOfItem, GameMap map) {
		if(!checkIfEmpty() && indexOfItem <= maxInventorySize) {
			Entity item = inventory[indexOfItem];
			if(item != null) {
				map.getEntities().add(item);
				item.setDisabled(false);
				item.setPos(map.getHero().getX(), map.getHero().getY());
				item.throwEntity(map.getHero(), 100f);
				inventory[indexOfItem] = null;
			}
		}
	}
	
	public boolean checkIfEmpty() {
		for(int i = 0; i<maxInventorySize; i++) {
			if(inventory[i] != null)
				return false;
		}
		return true;
	}
	
	public int getEmptySlot() {
		for(int i = 0; i<maxInventorySize; i++) {
			if(inventory[i] == null)
				return i;
		}
		return -1;
	}
	
	public void put(Items item, int slot) {
		inventory[slot] = item;
	}
	
	public void printInventory() {
		for(int i = 0; i<maxInventorySize; i++) {
			Items item = inventory[i];
			if(item != null)
				System.out.println(i+". "+item.getType().getName()+" "+item.isDisabled());
		}
	}
	
	public Items[] getItemList() {
		return inventory;
	}
	
	public int getMaxSize() {
		return maxInventorySize;
	}

	public void movePointerToTheLeft() {
		if(pointer > 0) {
			pointer--;
		}
	}

	public void movePointerToTheRight() {
		if(pointer < maxInventorySize) {
			pointer++;
		}
	}

	public int getMaxInventorySize() {
		return maxInventorySize;
	}

	public void setMaxInventorySize(int maxInventorySize) {
		this.maxInventorySize = maxInventorySize;
	}

	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}
}
