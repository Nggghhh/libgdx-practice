package com.mygdx.game.entities;

import java.util.ArrayList;

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
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;

public abstract class Entity {
	protected int id;
	
	protected Vector2 pos;
	protected transient Vector2 velocity;
	
	protected transient SoundManager sound;
	
	protected EntityType type;
	protected transient GameMap map;
	protected transient CollisionRect rect;
	protected boolean canMove = true;
	protected boolean isPickable = false;
	
	//collisions
	protected int direction = 1;
	
	protected boolean destroy = false;
	
	public Entity() {
		
	}
		
	public Entity(float x, float y, EntityType type, GameMap map, int id) {
		this.pos = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y,type.getWidth(), type.getHeight(), true);
		this.sheet = new Texture(this.type+"/"+this.state+"/"+this.currentAnim+"/"+frameNum+".png");
		this.id = id;
	}
	

	//breakpoint
	public void update (OrthographicCamera camera, float deltaTime, GameMap map) {
//		if(Gdx.input.isKeyJustPressed(Keys.X)) {
//			if(map.getHero().getDirection() == 1) {
//				if(!map.isEntityOnTile(pos.x, pos.y, getWidth(), getHeight(), map.getHero().getX()+16, map.getHero().getY()))
//					map.setTile(2, (int)(map.getHero().getX()-16)/TileType.TILE_SIZE,(int)map.getHero().getY()/TileType.TILE_SIZE, 14);
//			}
//			else if(map.getHero().getDirection() == 2) {
//				if(!map.isEntityOnTile(pos.x, pos.y, getWidth(), getHeight(), map.getHero().getX()-16, map.getHero().getY()))
//					map.setTile(2, (int)(map.getHero().getX()+16)/TileType.TILE_SIZE,(int)map.getHero().getY()/TileType.TILE_SIZE, 14);
//			}
//			else if(map.getHero().getDirection() == 3) {
//				if(!map.isEntityOnTile(pos.x, pos.y, getWidth(), getHeight(), map.getHero().getX(), map.getHero().getY()-16))
//					map.setTile(2, (int)map.getHero().getX()/TileType.TILE_SIZE,(int)(map.getHero().getY()-16)/TileType.TILE_SIZE, 14);
//			}
//			else if(map.getHero().getDirection() == 4) {
//				if(!map.isEntityOnTile(pos.x, pos.y, getWidth(), getHeight(), map.getHero().getX(), map.getHero().getY()+16))
//					map.setTile(2, (int)map.getHero().getX()/TileType.TILE_SIZE,(int)(map.getHero().getY()+16)/TileType.TILE_SIZE, 14);
//			}
//		}
		if(Gdx.input.isKeyJustPressed(Keys.X) && !map.isEntityOnTile(pos.x, pos.y, getWidth(), getHeight(), Unprojecter.getMouseCoords(camera).x, Unprojecter.getMouseCoords(camera).y))
			map.setTile(2, (int)Unprojecter.getMouseCoords(camera).x/TileType.TILE_SIZE,(int)Unprojecter.getMouseCoords(camera).y/TileType.TILE_SIZE, 13);
	}
	
	public abstract void hurt(int damage, Entity hitter, Entity receiver);
	
	public void push(Entity pusher,float distance) {
		
	}
	
	public abstract void attack(int damage, Entity hitter, int velocity, String type);
	
	public abstract void render (SpriteBatch batch, OrthographicCamera camera);
	
	public abstract void recreate (int x, int y, int health);
	
	protected boolean loop = true;
	protected int frameNum = 0;
	protected int previousFrameNum = 0;
	protected int animNum = 0;
	protected int animLen = 1;
	
	protected String state = "IDLE";
	protected transient String previousState = "IDLE";
	protected String currentAnim = "IDLE_LEFT";
	
	protected transient Texture sheet;
	protected transient float timer = 0;
	protected int animSpeed = 2;
	
	public Texture frames(String currentAnim, int frameNum) {
		sheet.dispose();
		if(sheet != null)
			return sheet = new Texture(this.type+"/"+this.state+"/"+this.currentAnim+"/"+frameNum+".png");
		return sheet = new Texture(this.type+"/IDLE/"+this.currentAnim+"/"+frameNum+".png");
	}
	
	public void staticFrame(EntityType type, SpriteBatch batch) {
		if(currentAnim != "IDLE") {
			currentAnim = "IDLE_LEFT";
		}
		batch.draw(frames(this.currentAnim, frameNum), pos.x-this.type.getPivotX(), pos.y-this.type.getPivotY(), 64, 64);
	}
	
	public void animation(EntityType type, SpriteBatch batch) {
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
		batch.draw(frames(this.currentAnim, frameNum), pos.x-28, pos.y-12, 64, 64);
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
				this.currentAnim = state+"_LEFT";
			else if(this.direction == 2)
				this.currentAnim = state+"_RIGHT";
			else if(this.direction == 3)
				this.currentAnim = state+"_DOWN";
			else
				this.currentAnim = state+"_UP";
		}
		else {
			if(this.direction == 1)
				this.currentAnim = state;
			else if(this.direction == 2)
				this.currentAnim = state;
			else if(this.direction == 3)
				this.currentAnim = state;
			else
				this.currentAnim = state;
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
}