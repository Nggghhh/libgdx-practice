package com.mygdx.game.items;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Camera;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public abstract class Items extends Entity {
	protected int frameRow;
	protected boolean consumable = false;
	
	public abstract void action(CustomGameMap map, Entity actor, Inventory actorsInventory, OrthographicCamera camera);
	
	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);
		
		float newX = pos.x;
		newX += this.velocity.x*deltaTime;

		//apply velocity on Y axis
		float newY = pos.y;
		newY += this.velocity.y*deltaTime;
		//check if collision with solid tile on Y axis occurred
		if(map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			this.pos.x = (float) Math.floor(pos.x);
			this.velocity.x = 0;
		}
		else {
			this.pos.x = newX;
		}

		//check if collision with solid tile on X axis occurred
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = (float) Math.floor(pos.y);
			this.velocity.y = 0;
		}
		else {
			this.pos.y = newY;
		}
		//linear damping, adding drag to object velocity
		timer(deltaTime);
	}
	
	@Override
	public void animation() {
		
	}
	
	@Override
	public void animationPlay(SpriteBatch batch) {
		try {
			TextureRegion texture = EntityAssetManager.getTexture("ITEMS", frameNum, frameRow);
			if(texture != null) {
				batch.setColor(r,g,b,1.0f);
				batch.draw(texture, pos.x-type.getPivotX(), pos.y-type.getPivotY());
				batch.setColor(1.0f,1.0f,1.0f,1.0f);
			}
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println(this.type+"/"+this.state);
		}
	}

	@Override
	public void hurt(int damage, Entity hitter, Entity receiver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		animationPlay(batch);
	}

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void push(float x, float y ,float distance) {
        this.velocity.x = 0;
        this.velocity.y = 0;
        if(this.pos.x != x && this.pos.y != y) {
            float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, x, y);
            float ratio = distance/distanceBetweenEntities;
        	this.velocity.x = (this.pos.x-x)*ratio*100;
        	this.velocity.y = (this.pos.y-y)*ratio*100;
        }
        else {
            float distanceBetweenEntities = Vector2.dst(this.pos.x-1f, this.pos.y-1f, x+1f, y+1f);
            float ratio = distance/distanceBetweenEntities;
        	this.velocity.x = (this.pos.x-x)*ratio*100;
        	this.velocity.y = (this.pos.y-y)*ratio*100;
        }
	}
	
	public void timer(float deltaTime) {
		velocity.scl(1 - (4f * deltaTime*2));
	}

}
