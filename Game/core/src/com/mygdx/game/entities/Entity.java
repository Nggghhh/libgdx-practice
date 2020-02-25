package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.tools.CollisionRect;

public abstract class Entity {
	
	protected Vector2 pos;
	protected EntityType type;
	protected GameMap map;
	protected CollisionRect rect;
	
	//collisions
	protected boolean collide = false;
	protected int direction = 1;
	
	protected boolean destroy = false;
	
	protected float time = 0;
	
	public Entity(float x, float y, EntityType type, GameMap map) {
		this.pos = new Vector2(x, y);
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y,type.getWidth(), type.getHeight());
		this.PATH = this.type+"/SPRITE_SHEET.png";
		this.sheet = new Texture(PATH);
	}
	

	//breakpoint
	public abstract void update (float deltaTime);
	
	public abstract void hurt(int damage, Entity hitter, Entity receiver);
	
	public abstract void attack(int damage, Entity hitter, int direction);
	
	public abstract void render (SpriteBatch batch);
	
	protected boolean loop = true;
	protected int frameNum = 0;
	protected int animNum = 0;
	
	protected String state = "IDLE";
	protected String previousState = "IDLE";
	protected String currentAnim = "IDLELEFT";
	
	protected String PATH;
	protected Texture sheet;
	protected float timer = 0;
	protected int animLenght = 2;
	
	public void animation(EntityType type, SpriteBatch batch) {
		timer += Gdx.graphics.getDeltaTime()*animLenght;
		
		if(timer > 1f && frameNum < 1) {
			frameNum += 1;
			timer = 0;
		}
		else if(timer > 1f && frameNum == 1) {
			frameNum = 0;
			timer = 0;
		}
		
		TextureRegion[][] crop = TextureRegion.split(sheet, 64, 64);
		batch.draw(crop[animNum][frameNum], pos.x-28, pos.y-12, 64, 64);
		
		if(this.previousState != this.state)
			frameNum = 0;
			this.previousState = this.state;
		
		if(this.state == "IDLE") 
			if(this.direction == 1)
				this.animNum = 0;
			else if(this.direction == 2)
				this.animNum = 1;
			else if(this.direction == 3)
				this.animNum = 2;
			else
				this.animNum = 3;
		
		else if(this.state == "HURT")
			if(this.direction == 1)
				this.animNum = 4;
			else if(this.direction == 2)
				this.animNum = 4;
			else if(this.direction == 3)
				this.animNum = 4;
			else
				this.animNum = 4;
	}

	public Vector2 getPos() {
		return pos;
	}

	public EntityType getType() {
		return type;
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public int getWidth() {
		return type.getWidth();
	}
	
	public int getHeight() {
		return type.getHeight();
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getFrame() {
		return frameNum;
	}
	
	public String getState() {
		return state;
	}
	
	public CollisionRect getRect() {
		return rect;
	}
	
	public boolean getCollide() {
		return collide;
	}
	
	
	public void setCollide(boolean c) {
		this.collide = c;
	}
	
	public void setState(String state) {
		this.state = state;
	}


	public boolean getAlive() {
		return destroy;
	}
}