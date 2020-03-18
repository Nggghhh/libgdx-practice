package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.world.GameMap;

public class Camera {
	OrthographicCamera camera;
	ShapeRenderer sh;
	
	public void init(SpriteBatch batch) {
		camera = new OrthographicCamera();
		sh = new ShapeRenderer();
	}
	
	public void render(GameMap gameMap, float deltaTime, SpriteBatch batch) {
		if(Gdx.graphics.isFullscreen())
			camera.setToOrtho(false, Gdx.graphics.getWidth()*4, Gdx.graphics.getHeight()*4);
		if(!Gdx.graphics.isFullscreen())
			camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(gameMap.getHero().getX(), gameMap.getHero().getY(), 0);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		gameMap.update(camera, deltaTime);
		gameMap.render(camera, batch);
		
		sh.begin(ShapeType.Line);
		sh.setProjectionMatrix(camera.combined);
		sh.setColor(255, 0, 0, 255);
		sh.line(gameMap.getHero().getX(), gameMap.getHero().getY(), gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY());
		sh.line(gameMap.getHero().getX(), gameMap.getHero().getY()+gameMap.getHero().getHeight(), gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY()+gameMap.getHero().getHeight());
		sh.line(gameMap.getHero().getX(), gameMap.getHero().getY()+gameMap.getHero().getHeight(), gameMap.getHero().getX(), gameMap.getHero().getY());
		sh.line(gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY()+gameMap.getHero().getHeight(), gameMap.getHero().getX()+gameMap.getHero().getWidth(), gameMap.getHero().getY());
		sh.end();
	}
	
	public void dispose() {
		sh.dispose();
	}
}
