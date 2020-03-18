package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.SoundManager;

public abstract class Entity {
	
	protected Vector2 pos;
	protected Vector2 velocity;
	
	protected SoundManager sound;
	
	protected EntityType type;
	protected GameMap map;
	protected CollisionRect rect;
	
	//collisions
	protected boolean collide = false;
	protected int direction = 1;
	
	protected boolean destroy = false;
	
	public Entity(float x, float y, EntityType type, GameMap map) {
		this.pos = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y,type.getWidth(), type.getHeight());
		this.sheet = new Texture(this.type+"/"+this.state+"/"+this.currentAnim+"/"+frameNum+".png");
	}
	

	//breakpoint
	public abstract void update (OrthographicCamera camera, float deltaTime);
	
	public abstract void hurt(int damage, Entity hitter, Entity receiver);
	
	public abstract void attack(int damage, Entity hitter, int direction, String type);
	
	public abstract void render (SpriteBatch batch, OrthographicCamera camera);
	
	public abstract void recreate (int x, int y, int health);
	
	protected boolean loop = true;
	protected int frameNum = 0;
	protected int previousFrameNum = 0;
	protected int animNum = 0;
	protected int animLen = 1;
	
	protected String state = "IDLE";
	protected String previousState = "IDLE";
	protected String currentAnim = "IDLE_LEFT";
	
	protected Texture sheet;
	protected float timer = 0;
	protected int animSpeed = 2;
	
	public Texture frames(String currentAnim, int frameNum) {
		if(this.destroy == false) {
			sheet.dispose();
			return sheet = new Texture(this.type+"/"+this.state+"/"+this.currentAnim+"/"+frameNum+".png");
		}
		else {
			sheet.dispose();
			return null;
		}
	}
	
	public void animation(EntityType type, SpriteBatch batch) {
		timer += Gdx.graphics.getDeltaTime()*animSpeed;
		//timer
		if(timer > 1f && frameNum < animLen) {
			frameNum += 1;
			timer = 0;
		}
		else if(timer > 1f && frameNum == animLen) {
			frameNum = 0;
			timer = 0;
			if(loop == false) {
				this.state = "IDLE";
			}	
		}
		//states
		if(this.state == "IDLE")  {
			this.loop = true;
			this.animLen = 1;
			this.animSpeed = 2;
			
			if(this.direction == 1)
				this.currentAnim = "IDLE_LEFT";
			else if(this.direction == 2)
				this.currentAnim = "IDLE_RIGHT";
			else if(this.direction == 3)
				this.currentAnim = "IDLE_DOWN";
			else
				this.currentAnim = "IDLE_UP";
		}
		
		else if(this.state == "HURT") {
			this.loop = true;
			this.animLen = 1;
			this.animSpeed = 2;
			
			if(this.direction == 1)
				this.currentAnim = "HURT";
			else if(this.direction == 2)
				this.currentAnim = "HURT";
			else if(this.direction == 3)
				this.currentAnim = "HURT";
			else
				this.currentAnim = "HURT";
		}
		
		else if(this.state == "ATTACK") {
			this.loop = false;
			this.animLen = 5;
			this.animSpeed = 15;
			
			if(this.direction == 1)
				this.currentAnim = "ATTACK_LEFT";
			else if(this.direction == 2)
				this.currentAnim = "ATTACK_RIGHT";
			else if(this.direction == 3)
				this.currentAnim = "ATTACK_DOWN";
			else
				this.currentAnim = "ATTACK_UP";
		}
		
		if(this.previousState != this.state) {
			frameNum = 0;
			this.previousState = this.state;
		}
		batch.draw(frames(this.currentAnim, frameNum), pos.x-28, pos.y-12, 64, 64);
	}

	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(int x, int y) {
		this.pos.x = x;		
		this.pos.y = y;
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
	public int getAnimLen() {
		return animLen;
	}
	public boolean getLoop() {
		return loop;
	}
	public int getAnimSpeed() {
		return animSpeed;
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


	public boolean getDestroyed() {
		return destroy;
	}
	
	public void setDestroyed(boolean state) {
		this.destroy = state;
	}
	
	public void setDirection(int dir) {
		this.direction = dir;
	}
}