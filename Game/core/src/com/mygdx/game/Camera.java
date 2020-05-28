package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.world.GameMap;

public class Camera {
	OrthographicCamera camera;
	ShapeRenderer sh;
//	float lenght = 0;
//	int angleToTransform = 0;
	int mapLeft, mapRight, mapUp, mapDown;
	public float viewportHeight, viewportWidth, cameraLeft, cameraRight, cameraBottom, cameraTop, cameraHalfWidth, cameraHalfHeight;
	
	public void init(SpriteBatch batch, GameMap gameMap) {
		camera = new OrthographicCamera();
		sh = new ShapeRenderer();
		mapLeft = 16;
		mapRight = 0 + gameMap.getPixelWidth()-16;
		mapDown = 16;
		mapUp = 0 + gameMap.getPixelHeight()-16;
		viewportHeight = camera.viewportHeight;
		viewportWidth = camera.viewportWidth;
	}
	
	public void render(GameMap gameMap, float deltaTime, SpriteBatch batch) {
		camera.setToOrtho(false, Gdx.graphics.getWidth()*0.5f, Gdx.graphics.getHeight()*0.5f);
		if(gameMap.getHero() != null) {
			camera.position.y = gameMap.getHero().getY();
			camera.position.x = gameMap.getHero().getX();
		}
		else {
			camera.position.y = 500;
			camera.position.x = 500;
		}
		viewportHeight = camera.viewportHeight;
		viewportWidth = camera.viewportWidth;
		
		cameraHalfWidth = viewportWidth * .5f;
		cameraHalfHeight = viewportHeight * .5f;
		
		cameraLeft = camera.position.x - cameraHalfWidth;
		cameraRight = camera.position.x + cameraHalfWidth;
		cameraBottom = camera.position.y - cameraHalfHeight;
		cameraTop = camera.position.y + cameraHalfHeight;
		
		if(gameMap.getPixelWidth() < camera.viewportWidth)
		{
		    camera.position.x = mapRight / 2;
		}
		else if(cameraLeft <= mapLeft)
		{
		    camera.position.x = mapLeft + cameraHalfWidth;
		}
		else if(cameraRight >= mapRight)
		{
		    camera.position.x = mapRight - cameraHalfWidth;
		}
		
		if(gameMap.getPixelHeight() < camera.viewportHeight)
		{
			camera.position.y = mapUp / 2;
		}
		else if(cameraBottom <= mapDown)
		{
			camera.position.y = mapDown + cameraHalfHeight;
		}
		else if(cameraTop >= mapUp)
		{
			camera.position.y = mapUp - cameraHalfHeight;
		}
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		gameMap.update(this, deltaTime);
		gameMap.render(this, batch);
		
//		float originX = gameMap.getHero().getX();
//		float originY = gameMap.getHero().getY();
//		
//		float x = originX+lenght;
//		float y = originY+lenght;
//		
//		double angle = Math.toRadians(angleToTransform);
//		
//		float newX = (float) (Math.cos(angle)*(x-originX) - Math.sin(angle)*(y-originY)+originX);
//		float newY = (float) (Math.sin(angle)*(x-originX) + Math.cos(angle)*(y-originY)+originY);
//		
//		if(newX < gameMap.getWidth()*16 && newY < gameMap.getHeight()*16 && newX > 0 && newY > 0)
//			if(gameMap.getTileTypeByLocation(1, newX, newY).tile != null && !gameMap.getTileTypeByLocation(1, newX, newY).tile.isCollidable())
//				lenght += 16;
//			else
//				lenght = 0;
//		else
//			lenght = 0;
//		
//		angleToTransform += 1;
//		
//		if(angleToTransform > 360) {
//			angleToTransform = 0;
//		}
		
//		sh.begin(ShapeType.Line);
//		sh.setProjectionMatrix(camera.combined);
//		sh.setColor(255, 0, 0, 255);
//		sh.line(originX, originY, newX, newY);
//		sh.end();
		
//		sh.begin(ShapeType.Line);
//		sh.setProjectionMatrix(camera.combined);
//		sh.setColor(255, 0, 0, 255);
//		sh.line(gameMap.getHero().getX(), gameMap.getHero().getY(), gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY());
//		sh.line(gameMap.getHero().getX(), gameMap.getHero().getY()+gameMap.getHero().getHeight(), gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY()+gameMap.getHero().getHeight());
//		sh.line(gameMap.getHero().getX(), gameMap.getHero().getY()+gameMap.getHero().getHeight(), gameMap.getHero().getX(), gameMap.getHero().getY());
//		sh.line(gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY()+gameMap.getHero().getHeight(), gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY());
//		sh.end();
	}
	
	public void dispose() {
		sh.dispose();
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public float getLeftB() {
		return camera.position.x - cameraHalfWidth;
	}
	
	public float getRightB() {
		return camera.position.x + cameraHalfWidth;
	}
	
	public float getBottomB() {
		return camera.position.y - cameraHalfHeight;
	}
	
	public float getTopB() {
		return camera.position.y + cameraHalfHeight;
	}
}
