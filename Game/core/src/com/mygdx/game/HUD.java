package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HUD {
	private transient Texture image;
	private transient Texture dayAndNight;
	private transient Texture dateFrame;
	
	public HUD() {
		image = new Texture("HEALTH.png");
		dayAndNight = new Texture("SunAndMoon2.png");
		dateFrame = new Texture("DateHUDframe.png");
	}

	public void render(int health, SpriteBatch hud, Camera camera, float hour, int moonPhase) {
		TextureRegion[][] cropDnNC = TextureRegion.split(dayAndNight, 48, 8);
		OrthographicCamera cam = camera.getCamera();
		
		float sunX = ((cam.position.x-cropDnNC[0][moonPhase].getRegionWidth()/2)+cropDnNC[0][moonPhase].getRegionWidth());
		float moonX = (cam.position.x-cropDnNC[0][moonPhase].getRegionWidth()/2);
		
		if(hour <= 12) {
			hud.draw(cropDnNC[moonPhase][0], moonX-hour*4, (cam.position.y+camera.viewportHeight/2)-9, cropDnNC[0][0].getRegionWidth(), cropDnNC[0][0].getRegionHeight());
			hud.draw(cropDnNC[moonPhase][1], sunX-hour*4, (cam.position.y+camera.viewportHeight/2)-9, cropDnNC[0][0].getRegionWidth(), cropDnNC[0][0].getRegionHeight());
		}
		else if(hour > 12) {
			hud.draw(cropDnNC[moonPhase][0], (sunX+cropDnNC[0][0].getRegionWidth())-hour*4, (cam.position.y+camera.viewportHeight/2)-9, cropDnNC[0][0].getRegionWidth(), cropDnNC[0][0].getRegionHeight());
			hud.draw(cropDnNC[moonPhase][1], (moonX+cropDnNC[0][0].getRegionWidth())-hour*4, (cam.position.y+camera.viewportHeight/2)-9, cropDnNC[0][0].getRegionWidth(), cropDnNC[0][0].getRegionHeight());
		}
		hud.draw(dateFrame, (cam.position.x-dateFrame.getWidth()/2), (cam.position.y+camera.viewportHeight/2)-dateFrame.getHeight());
		
		if(health >= 0) {
			TextureRegion[][] crop = TextureRegion.split(image, 24, 24);
			hud.draw(crop[0][health], cam.position.x-camera.viewportWidth/2, cam.position.y-camera.viewportHeight/2);
		}
	}
}