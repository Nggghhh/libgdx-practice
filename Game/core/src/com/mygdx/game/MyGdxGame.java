package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TiledGameMap;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Camera camera;
	GameManager gm;
	GameMap gameMap;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new Camera();
		gameMap = new CustomGameMap();
		gm = new GameManager(gameMap);
		camera.init(batch, gameMap);
	}

	@Override
	public void render () {
		float deltaTime = Math.min(1/30f, Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0,0,0,255);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN))
			gameMap.init("Menu");
		
		if(Gdx.input.isKeyJustPressed(Keys.H)) {
			gm.saveData(gameMap.getHero(), gameMap.getHero().getPlayerInventory());
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.J)) {
			gm.loadData(gameMap.getHero(), gameMap);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.K))
			gm.printJson();
		
		camera.render(gameMap, deltaTime, batch);
	}
	
	@Override
	public void dispose () {
		camera.dispose();
		batch.dispose();
		gameMap.dispose();
	}
}
