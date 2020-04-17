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
	int mapLeft, mapRight, mapUp, mapDown;
	public float cameraLeft, cameraRight, cameraBottom, cameraTop, cameraLeftBorder, cameraRightBorder, cameraBottomBorder, cameraTopBorder, cameraHalfWidth, cameraHalfHeight;
	
	public void init(SpriteBatch batch, GameMap gameMap) {
		camera = new OrthographicCamera();
		sh = new ShapeRenderer();
		mapLeft = 0;
		mapRight = 0 + gameMap.getPixelWidth();
		mapDown = 0;
		mapUp = 0 + gameMap.getPixelHeight();
	}
	
	public void render(GameMap gameMap, float deltaTime, SpriteBatch batch) {
		camera.setToOrtho(false, Gdx.graphics.getWidth()*0.5f, Gdx.graphics.getHeight()*0.5f);
		camera.position.y = gameMap.getHero().getY();
		camera.position.x = gameMap.getHero().getX();
		
		cameraHalfWidth = camera.viewportWidth * .5f;
		cameraHalfHeight = camera.viewportHeight * .5f;
		
		cameraLeft = camera.position.x - cameraHalfWidth;
		cameraRight = camera.position.x + cameraHalfWidth;
		cameraBottom = camera.position.y - cameraHalfHeight;
		cameraTop = camera.position.y + cameraHalfHeight;
		
		if(cameraLeft > mapLeft)
			cameraLeftBorder = cameraLeft;
		if(cameraRight < mapRight)
			cameraRightBorder = cameraRight;
		if(cameraBottom > mapDown)
			cameraBottomBorder = cameraBottom;
		if(cameraTop < mapUp)
			cameraTopBorder = cameraTop;
		
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
		
		gameMap.update(camera, deltaTime);
		gameMap.render(this, batch);
		
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
