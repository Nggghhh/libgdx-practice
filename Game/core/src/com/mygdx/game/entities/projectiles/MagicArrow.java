package com.mygdx.game.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.world.GameMap;

public class MagicArrow extends Bullet {
	private boolean move;
	private float updateParticleTimer;
	private transient Particle[] particles;
	private int speed;

	@Override
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
		changeState("IDLE", true, 3, 9);
		this.sliperyness = 1f;
		this.defaultStrenght = 1;
		this.caster = snapshot.getString("caster", "nobody");
		this.move = true;

		this.particles = new Particle[3];
		particles[0] = new Particle(0, this.pos.x, this.pos.y, this);
		particles[1] = new Particle(1, this.pos.x, this.pos.y, this);
		particles[2] = new Particle(2, this.pos.x, this.pos.y, this);
	}

	private float time = 0;
	private int origAngle = 0;

	@Override
	public void update(OrthographicCamera camera, float deltaTime, GameMap map) {
		super.update(camera, deltaTime, map);

		time += deltaTime*100;
		angle = origAngle + (int) (60 * Math.sin(0.5 * Math.PI * 0.15 * time));

		if(move) {
			move(angle, speed, deltaTime);
		}

		float newX = pos.x;
		newX += this.velocity.x*deltaTime;

		//apply velocity on Y axis
		float newY = pos.y;
		newY += this.velocity.y*deltaTime;
		//check if collision with solid tile on Y axis occurred
		if(map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())) {
			this.pos.x = (float) Math.floor(pos.x);
			destroy();
		}
		else {
			this.pos.x = newX;
		}

		//check if collision with solid tile on X axis occurred
		if(map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
			this.pos.y = (float) Math.floor(pos.y);
			destroy();
		}
		else {
			this.pos.y = newY;
		}

		//linear damping, adding drag to object velocity
		linearDumping(deltaTime, sliperyness);

		if(this.state.equals("EXPLOSION") && this.frameNum == 8) {
			this.disabled = true;
		}

		//destroy after flinch animation is ended
	}

	protected void move(int angleToTransform, int speed, float delta) {
		double angle = Math.toRadians(angleToTransform-225);
		float newX = (float) (Math.cos(angle) - Math.sin(angle));
		float newY = (float) (Math.sin(angle) + Math.cos(angle));
		if(velocity.x != speed*newX)
			velocity.x =+ speed*newX;
		if(velocity.y != speed*newY)
			velocity.y =+ speed*newY;
	}

	private void drawTrail(SpriteBatch batch) {
		for(Particle particle : particles) {
			particle.draw(batch, this);
		}
	}

	private void updateTrail() {
		for(Particle particle : particles) {
			particle.update();
		}
	}

	@Override
	public void checkForCollision() {
		for(Entity entity : map.getEntities()) {
			if (!(entity instanceof Bullet)) {
				if (map.getEntities().indexOf(this) != map.getEntities().indexOf(entity) && !entity.isDestroyed() && !entity.isDisabled()) {
					if (this.rect.isEnabled() && entity.getRect().isEnabled() && this.rect.collidesWith(entity.getRect())) {
						entity.push(this.getX(), this.getY(), entity.getType().getWeight());
						destroy();
					}
				}
			}
		}
	}

	@Override
	public void hurt(int damage, Entity hitter, Entity receiver) {
		// TODO Auto-generated method stub

	}
	
	public void destroy() {
		this.velocity.x = 0;
		this.velocity.y = 0;
		this.move = false;
		this.rect.setEnabled(false);
		this.changeState("EXPLOSION", false, 8, 18);
	}

	@Override
	public void attack(int damage, Entity hitter, float velocity, String type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		updateTrail();
		if(this.getAbsoluteVelocity() > 10) {
			updateTrail();
			drawTrail(batch);
		}
		super.render(batch, camera);
	}

	@Override
	public void recreate(int x, int y, int health) {
		// TODO Auto-generated method stub

	}

	@Override
	public EntitySnapshot getSaveSnapshot() {
		EntitySnapshot entitySnapshot = super.getSaveSnapshot();
		entitySnapshot.putString("caster", caster);
		return entitySnapshot;
	}

	@Override
	public void push(float x, float y, float distance) {
        if(this.pos.x != x && this.pos.y != y) {
            float distanceBetweenEntities = Vector2.dst(this.pos.x, this.pos.y, x, y);
            float ratio = distance/distanceBetweenEntities;
        	this.velocity.x = (this.pos.x-x)*ratio*100;
        	this.velocity.y = (this.pos.y-y)*ratio*100;
        }
        else {
            float distanceBetweenEntities = Vector2.dst(this.pos.x-1f, this.pos.y-1f, x+1f, y+1f);
            float ratio = distance/distanceBetweenEntities;
        	this.velocity.x = ((this.pos.x-1f)-x)*ratio*100;
        	this.velocity.y = ((this.pos.y-1f)-y)*ratio*100;
        }
	}

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public int getOrigAngle() {
		return origAngle;
	}

	public void setOrigAngle(int origAngle) {
		this.origAngle = origAngle;
	}
}




class Particle {
	private float particleX, particleY, timer;
	private int frameNum;
	private final Entity projectile;

	public Particle(int startingFrame, float particleX, float particleY, Entity projectile) {
		this.frameNum = startingFrame;
		this.particleX = particleX;
		this.particleY = particleY;
		this.projectile = projectile;
	}

	public void update() {
		int ANIM_SPEED = 12;
		int MAX_FRAME = 3;

		timer += Gdx.graphics.getDeltaTime()*ANIM_SPEED;
		if(timer > 1.0f) {
			timer = 0;
			if(frameNum < MAX_FRAME) {
				frameNum++;
			} else {
				this.particleX = projectile.getX();
				this.particleY = projectile.getY();
				frameNum = 0;
			}
		}
	}

	public void draw(SpriteBatch batch, Entity entity) {
		TextureRegion firstPart = EntityAssetManager.getTexture("MAGIC_ARROW/TRAIL", frameNum);
		int textureWidth = firstPart.getRegionWidth();
		int entityHalfWidth = entity.getWidth()/2;
		int entityHalfHeight = entity.getHeight()/2;
		batch.draw(firstPart, particleX+entityHalfWidth-textureWidth/2, particleY-entityHalfHeight);
	}

	public float getParticleX() {
		return particleX;
	}

	public void setParticleX(float particleX) {
		this.particleX = particleX;
	}

	public float getParticleY() {
		return particleY;
	}

	public void setParticleY(float particleY) {
		this.particleY = particleY;
	}

	public int getFrame() {
		return frameNum;
	}

	public void setFrame(int frame) {
		this.frameNum = frame;
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
	}


}
