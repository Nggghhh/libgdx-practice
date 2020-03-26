package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HUD {
	private transient Texture image;
	
	public HUD() {
		image = new Texture("HEALTH.png");
	}

	public void render(int health, SpriteBatch hud, OrthographicCamera camera) {
		if(health >= 0) {
			TextureRegion[][] crop = TextureRegion.split(image, 24, 24);
			hud.draw(crop[0][health], camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2, 48, 48);
		}
	}
}