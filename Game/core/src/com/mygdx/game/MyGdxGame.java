package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TiledGameMap;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Camera camera;

	GameMap gameMap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new Camera();
		camera.init(batch);
		gameMap = new TiledGameMap();
	}

	@Override
	public void render () {
		float deltaTime = Math.min(1/30f, Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(90/255f,168/255f,92/255f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN))
			gameMap.init("Menu");
		
		camera.render(gameMap, deltaTime, batch);
	}
	
	@Override
	public void dispose () {
		camera.dispose();
		batch.dispose();
		gameMap.dispose();
	}
}
