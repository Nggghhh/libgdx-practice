package com.mygdx.game.entities;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;

public abstract class Entity {
	protected int id;
	
	protected Vector2 pos;
	protected transient Vector2 velocity;
	protected float weight;
	protected float pushStrenght;
	
	protected transient SoundManager sound;
	
	protected EntityType type;
	protected transient GameMap map;
	protected transient CollisionRect rect;
	protected boolean canMove = true;
	protected boolean isPickable = false;
	protected boolean disabled = false;
	
	//collisions
	protected int direction = 1;
	
	protected boolean destroy = false;
	
	public Entity() {
		
	}
		
	public Entity(float x, float y, EntityType type, GameMap map, int id) {
		this.pos = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.entityTextures = new HashMap<String, Texture>();
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y,type.getWidth(), type.getHeight(), true);
		this.id = id;
	}
	

	//breakpoint
	public void update (OrthographicCamera camera, float deltaTime, GameMap map) {
		animation(this.type);
		
		//move collision rectangle with its sprite
		if(rect.isEnabled())
			rect.move(this.pos.x, this.pos.y);
		if(this.isDisabled())
			this.rect.setEnabled(false);
		
	}
	
	public abstract void hurt(int damage, Entity hitter, Entity receiver);
	
	public void push(float x, float y, float distance) {
		
	}
	
	public void throwEntity(Entity thrower, float power) {
		if(thrower.getDirection() == 1)
			this.setVelocityX(-power);
		else if(thrower.getDirection() == 2)
			this.setVelocityX(+power);
		else if(thrower.getDirection() == 3)
			this.setVelocityY(-power);
		else if(thrower.getDirection() == 4)
			this.setVelocityY(+power);
	}
	
	public abstract void attack(int damage, Entity hitter, int velocity, String type);
	
	public abstract void render (SpriteBatch batch, OrthographicCamera camera);
	
	public abstract void recreate (int x, int y, int health);
	
	protected transient HashMap<String, Texture> entityTextures;
	
	protected boolean loop = true;
	protected int frameNum = 0;
	protected int previousFrameNum = 0;
	protected int animNum = 0;
	protected int animLen = 1;
	
	protected String state = "IDLE";
	protected transient String previousState = "IDLE";
	protected String currentAnim = "0";
	
	protected transient Texture sheet;
	protected transient float timer = 0;
	protected int animSpeed = 2;
	
	public void animation(EntityType type) {
		timer += Gdx.graphics.getDeltaTime()*animSpeed;
		if(timer > 1f) {
			timer = 0;
			if(frameNum < animLen) {
				frameNum += 1;
			}
			else if(frameNum == animLen) {
				if(loop == true) {
					frameNum = 0;
				}
				else if(loop == false) {
					if(this.state != "DEATH")
						changeState("IDLE", true, 1, 2);
				}
			}
		}
	}
	
	public void animationPlay(SpriteBatch batch) {
		if(entityTextures.get(this.type+"/"+this.state+"/"+this.currentAnim+"/"+frameNum+".png") != null)
			batch.draw(entityTextures.get(this.type+"/"+this.state+"/"+this.currentAnim+"/"+frameNum+".png"), pos.x-type.getPivotX(), pos.y-type.getPivotY());
	}
	
	protected void animationLoader(EntityType type, String name, int frameNum, int animationNumber) {
		for(int i = 0; i < animationNumber; i++)
			for(int j = 0; j < frameNum; j++)
				this.entityTextures.put(type+"/"+name+"/"+i+"/"+j+".png", new Texture(type+"/"+name+"/"+i+"/"+j+".png"));
	}
	
	protected void changeState(String newState, boolean loop, int animLen, int animSpeed) {
		this.loop = loop;
		this.animLen = animLen;
		this.animSpeed = animSpeed;
		this.state = newState;
		
		if(this.previousState != this.state) {
			this.timer = 0;
			this.frameNum = 0;
			this.previousState = this.state;
		}
		if(state != "DEATH" && state != "HURT") {
			if(this.direction == 1)
				this.currentAnim = "0";
			else if(this.direction == 2)
				this.currentAnim = "1";
			else if(this.direction == 3)
				this.currentAnim = "2";
			else
				this.currentAnim = "3";
		}
		else {
			if(this.direction == 1)
				this.currentAnim = "0";
			else if(this.direction == 2)
				this.currentAnim = "0";
			else if(this.direction == 3)
				this.currentAnim = "0";
			else
				this.currentAnim = "0";
		}
	}
	
	public int getId() {
		return id;
	}

	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(float posX, float posY) {
		this.pos.x = posX;		
		this.pos.y = posY;
	}

	public EntityType getType() {
		return type;
	}
	
	public void setType(EntityType type) {
		this.type = type;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public float velocityX() {
		return velocity.x;
	}
	
	public float velocityY() {
		return velocity.y;
	}
	
	public void setVelocityX(float x) {
		this.velocity.x = x;
	}
	
	public void setVelocityY(float y) {
		this.velocity.y = y;
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
	
	public boolean getCanMove() {
		return canMove;
	}
	
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}