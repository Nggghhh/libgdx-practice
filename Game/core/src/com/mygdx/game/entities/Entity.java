package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.tools.Unprojecter;

public abstract class Entity {
	protected int id;
	
	protected Vector2 pos;
	protected int layer;
	protected transient Vector2 velocity;
	protected float weight, r, g, b;;
	protected float pushStrenght;
	protected float slippery = 4f;
	
	protected transient SoundManager sound;
	
	protected EntityType type;
	protected transient GameMap map;
	protected transient CollisionRect rect;
	protected boolean canMove = true;
	protected boolean isPickable = false;
	protected boolean disabled = false;
	
	//collisions
	protected int direction = 1;
	
	protected int HEALTH;
	
	protected boolean destroy = false;
	
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		float x = snapshot.getX();
		float y = snapshot.getY();
		int HEALTH = snapshot.HEALTH;
		this.pos = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y, type.getWidth(), type.getHeight(), true);
		this.HEALTH = HEALTH;
	}
		
//	public Entity(float x, float y, EntityType type, GameMap map, int id) {
//		this.pos = new Vector2(x, y);
//		this.velocity = new Vector2(0, 0);
//		this.type = type;
//		this.map = map;
//		this.rect = new CollisionRect(x,y,type.getWidth(), type.getHeight(), true);
//		this.id = id;
//	}
	

	//breakpoint
	public void update (OrthographicCamera camera, float deltaTime, GameMap map) {
		this.r = map.getTileTypeByLocation(layer, pos.x, pos.y).tile.getColors()[0];
		this.g = map.getTileTypeByLocation(layer, pos.x, pos.y).tile.getColors()[1];
		this.b = map.getTileTypeByLocation(layer, pos.x, pos.y).tile.getColors()[2];
		animation();
		
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
	
	public abstract void attack(int damage, Entity hitter, float velocity, String type);
	
	public abstract void render (SpriteBatch batch, OrthographicCamera camera);
	
	public abstract void recreate (int x, int y, int health);
	
	protected boolean loop = true;
	protected int frameNum = 0;
	protected int previousFrameNum = 0;
	protected int animNum = 0;
	protected int animLen = 0;
	
	protected String state = "IDLE";
	protected transient String previousState = "IDLE";
	protected String currentAnim = "0";
	
	protected transient Texture sheet;
	protected transient float timer = 0;
	protected int animSpeed = 2;
	
	public void animation() {
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
		try {
			TextureRegion texture = EntityAssetManager.getTexture(this.type+"/"+this.state, frameNum, direction-1);
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
	}
	
	public EntitySnapshot getSaveSnapshot() {
		return new EntitySnapshot(type.getName(), pos.x, pos.y, HEALTH);
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


	public boolean isDestroyed() {
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
	
	public int getLayer() {
		return layer;
	}
}