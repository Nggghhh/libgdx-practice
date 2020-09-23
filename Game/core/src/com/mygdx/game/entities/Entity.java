package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.tools.CollisionRect;
import com.mygdx.game.tools.SoundManager;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.tiles.CustomTileType;

public abstract class Entity {
	protected int id;
	
	protected float RECOVERY_TIME;
	protected transient float remainingRecoveryTime;
	
	protected Vector2 pos;
	protected transient Vector2 velocity;
	protected float weight, r, g, b;
	protected float pushStrenght;
	protected float slippery = 8f;
	protected int angle = 0;
	protected int textureHeight = 64;
	protected int textureWidth = 64;
	
	protected int SPEED = 0;
	
	protected transient SoundManager sound;
	
	protected EntityType type;
	protected transient GameMap map;
	protected transient CollisionRect rect;
	protected boolean canMove = true;
	protected boolean isPickable = false;
	protected boolean disabled = false;
	protected boolean rotatable = false;
	
	//collisions
	protected int direction = 1;
	
	protected int HEALTH;
	
	protected boolean destroy = false;
	
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		float x = snapshot.getX();
		float y = snapshot.getY();
		this.HEALTH = snapshot.HEALTH;
		this.pos = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.type = type;
		this.map = map;
		this.rect = new CollisionRect(x,y, type.getWidth(), type.getHeight(), true);
		this.SPEED = type.getSpeed();
	}

	//breakpoint
	public void update (OrthographicCamera camera, float deltaTime, GameMap map) {
		this.r = map.getTileTypeByLocation(0, pos.x, pos.y).tile.getColors()[0];
		this.g = map.getTileTypeByLocation(0, pos.x, pos.y).tile.getColors()[1];
		this.b = map.getTileTypeByLocation(0, pos.x, pos.y).tile.getColors()[2];
		animation();
		
		//move collision rectangle with its sprite
		if(rect.isEnabled())
			rect.move(this.pos.x, this.pos.y);
		if(this.isDisabled())
			this.rect.setEnabled(false);

		checkForCollision();
		
	}

	public void checkForCollision() {
		for(Entity entity : map.getEntities()) {
			if (map.getEntities().indexOf(this) != map.getEntities().indexOf(entity) && !entity.isDestroyed() && !entity.isDisabled()) {
				if (this.rect.collidesWith(entity.getRect())) {
//					entity.hurt(1, this, entity);
					entity.push(this.getX(), this.getY(), entity.getType().getWeight());
					if(entity.getAbsoluteVelocity() > 0) {
						this.push(entity.getX(), entity.getY(), entity.getType().getWeight());
					}
				}
			}
		}
	}
	
	public void livingState(OrthographicCamera camera, float deltaTime, GameMap map) {
		//after recovery time is ended, reset back to idling state
		if(remainingRecoveryTime > 0) {
			remainingRecoveryTime -= deltaTime;
		}
		
		//destroy after flinch animation is ended
		if(HEALTH == 0 && state != "HURT") {
			destroy = true;
		} else if(remainingRecoveryTime < 0 && state == "HURT") {
			remainingRecoveryTime = 0;
			changeState("IDLE", true, 1, 2);
		}
		
		checkIfSwimming();
	}
	
	public abstract void hurt(int damage, Entity hitter, Entity receiver);
	
	public void push(float x, float y, float distance) {}

	public void push(float x, float y, float distance, float absoluteVelocity) {}
	
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
	
	public void swimming() {}
	
	public void checkIfSwimming() {
		CustomTileType tileBelow1 = map.getTileTypeByLocation(0, this.getX(), this.getY()).tile;
		CustomTileType tileBelow2 = map.getTileTypeByLocation(0, this.getX()+this.getWidth(), this.getY()+this.getHeight()).tile;
		if(tileBelow1.isLiquid() && tileBelow2.isLiquid()) {
			swimming();
			this.SPEED = type.getSwimmingSpeed();
		}
		else {
			this.SPEED = type.getSpeed();
		}
	}
	
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
				if(loop) {
					frameNum = 0;
				}
				else {
					if(this.state != "DEATH")
						changeState("IDLE", true, 1, 2);
				}
			}
		}
	}
	
	public void animationPlay(SpriteBatch batch) {
		int angle = 0;
		if(this.rotatable) {
			angle = this.angle;
		}
		try {
			TextureRegion texture = EntityAssetManager.getTexture(this.type+"/"+this.state, frameNum, direction-1);
			if(texture != null) {
				textureHeight = texture.getRegionHeight();
				textureWidth = texture.getRegionWidth();
				int entityHalfWidth = this.getWidth()/2;
				int entityHalfHeight = this.getHeight()/2;
				batch.setColor(r,g,b,1.0f);
				batch.draw(texture, pos.x+entityHalfWidth-textureWidth/2, pos.y-entityHalfHeight, (float) texture.getRegionWidth()/2, (float) texture.getRegionHeight()/2, (float) texture.getRegionWidth(), (float) texture.getRegionHeight(), 1, 1, angle);
				batch.setColor(1.0f,1.0f,1.0f,1.0f);
			} else {
				batch.draw(EntityAssetManager.getError(), pos.x-type.getPivotX(), pos.y-type.getPivotY());
			}
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println(this.type+"/"+this.state);
			batch.draw(EntityAssetManager.getError(), pos.x-type.getPivotX(), pos.y-type.getPivotY());
		}
	}
	public void changeState(String newState, boolean loop, int animLen, int animSpeed) {
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
		return new EntitySnapshot(type.getName(), pos.x, pos.y, HEALTH, 0);
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
	
	public float getVelocityX() {
		return velocity.x;
	}
	
	public float getVelocityY() {
		return velocity.y;
	}

	public float getAbsoluteVelocity() {
		return Math.abs(velocity.x) + Math.abs(velocity.y);
	}
	
	public void setVelocityX(float x) {
		this.velocity.x = x;
	}
	
	public void setVelocityY(float y) {
		this.velocity.y = y;
	}
	
	public void addVelocityX(float velocity) {
		this.velocity.x += velocity;
	}
	
	public void addVelocityY(float velocity) {
		this.velocity.y += velocity;
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
	
	public int getTextureHeight() {
		return textureHeight;
	}
	
	public int getTextureWidth() {
		return textureWidth;
	}
}