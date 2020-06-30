package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public class Knife extends Weapons {
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
		this.damage = 3;
		this.impact = 3;
		this.speed = 15;
	}

	@Override
	public void update(Entity user, GameMap map) {
		if(Gdx.input.isKeyJustPressed(Keys.Z))
			firstSkill(user, map);
		if(user.getState() == "A_KNIFE_BASIC" && user.getFrame() == 2) {
			if(user.getDirection() == 1) {
				user.addVelocityX(-60);
			}
			else if(user.getDirection() == 2) {
				user.addVelocityX(60);
			}
			else if(user.getDirection() == 3) {
				user.addVelocityY(-60);
			}
			else {
				user.addVelocityY(60);
			}
		}
	}

	@Override
	public void firstSkill(Entity user, GameMap map) {
		if(hitNum == 0) {
			user.changeState("A_KNIFE_BASIC", false, 6, 15);
		}
		else {
			user.changeState("A_KNIFE_BASIC2", false, 6, 15);
		}
	}

	@Override
	public void secondSkill(Entity user, GameMap map) {
		user.changeState("A_KNIFE_ALT", false, 7, 15);
	}

	@Override
	public void thirdSkill(Entity user, GameMap map) {
		user.changeState("A_KNIFE_ULT", false, 7, 15);
	}

	@Override
	public void action(CustomGameMap map, Entity actor, Inventory actorsInventory, OrthographicCamera camera) {
		// TODO Auto-generated method stub

	}

}
