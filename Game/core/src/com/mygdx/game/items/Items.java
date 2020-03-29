package com.mygdx.game.items;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.world.GameMap;

public class Items extends Entity {
	public Items(float x, float y, EntityType type, GameMap map, int id) {
		this.pos = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y,type.getWidth(), type.getHeight(), true);
		this.sheet = new Texture(this.type+"/IDLE/IDLE_LEFT/0.png");
		this.id = id;
	}

	@Override
	public void hurt(int damage, Entity hitter, Entity receiver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attack(int damage, Entity hitter, int velocity, String type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		batch.draw(sheet, pos.x-this.type.getPivotX(), pos.y-this.type.getPivotY(), 16, 16);
	}

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}

}
