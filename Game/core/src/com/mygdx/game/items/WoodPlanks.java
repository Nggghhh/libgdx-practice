package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Camera;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.tools.Unprojecter;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.tiles.CustomTileType;
import com.mygdx.game.world.tiles.TileGridCell;
import com.mygdx.game.world.tiles.WoodFloor;
import com.mygdx.game.world.tiles.WoodWall;

public class WoodPlanks extends Items {	
	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
		this.frameNum = 0;
		this.frameRow = 0;
		this.id = 6;
		this.consumable = true;
	}

	@Override
	public void action(CustomGameMap map, Entity actor,  Inventory actorsInventory, OrthographicCamera camera) {
		TileGridCell wall;
		TileGridCell floor;
		float x;
		float y;
		
		if(actor.getDirection() == 1) {
			x = actor.getX()-16;
			y = actor.getY();
		}
		else if(actor.getDirection() == 2) {
			x = actor.getX()+actor.getWidth()/2+16;
			y = actor.getY();
		}
		else if(actor.getDirection() == 3) {
			x = actor.getX();
			y = actor.getY()-16;
		}
		else {
			x = actor.getX();
			y = actor.getY()+actor.getHeight()/2+16;
		}
		wall = map.getTileTypeByLocation(1, x, y);
		floor = map.getTileTypeByLocation(0, x, y);
		
		if(!wall.isOccupied(map, 0)) {
			if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				if(wall.tile.getId() == 0) {
					wall.tileChange(new WoodWall(floor.x, floor.y, map), map);
					actorsInventory.getItemList()[0] = null;
				}
			} else {
				if(floor.tile.isReplacable() && wall.tile.getId() == 0) {
					floor.tileChange(new WoodFloor(floor.x, floor.y, map), map);
					actorsInventory.getItemList()[0] = null;
				}
			}
		}
	}
}
