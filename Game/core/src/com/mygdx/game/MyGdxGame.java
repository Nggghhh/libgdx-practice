package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TiledGameMap;

public class MyGdxGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	ScreenViewport view;
	
	GameMap gameMap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		view = new ScreenViewport(camera);
		view.setUnitsPerPixel(1f);
		view.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		view.apply();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
		
		gameMap = new TiledGameMap();
	}

	@Override
	public void render () {
		float deltaTime = Math.min(1/30f, Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		gameMap.update(deltaTime);
		gameMap.render(camera, batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		gameMap.dispose();
	}
}
