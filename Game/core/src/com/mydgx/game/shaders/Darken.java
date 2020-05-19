package com.mydgx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Camera;

public class Darken extends Shader {
	public static final float DEFAULT_LIGHT_Z = 0.075f;
	public static final float AMBIENT_INTENSITY = 0.2f;
	public static final float LIGHT_INTENSITY = 1f;
	
	public static final Vector3 LIGHT_POS = new Vector3(0f,0f,DEFAULT_LIGHT_Z);
	
	//Light RGB and intensity (alpha)
	public static final Vector3 LIGHT_COLOR = new Vector3(1f, 0.8f, 3.6f);

	//Ambient RGB and intensity (alpha)
	public static final Vector3 AMBIENT_COLOR = new Vector3(0.6f, 0.6f, 1f);

	//Attenuation coefficients for light falloff
	public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);
	
	public Darken(String vertex, String fragment) {
		super(vertex, fragment);
//		addColor(0.5f, 0.5f, 0.5f);
	}
	
	public void move(Camera camera) {
		float x = Gdx.input.getX() / (float)Gdx.graphics.getWidth();
		float y = (-Gdx.input.getY()+Gdx.graphics.getHeight()) / (float)Gdx.graphics.getHeight();
		LIGHT_POS.x = x;
		LIGHT_POS.y = y;

		shader.setUniformf("u_pos", x, y);

	}
	
	public void bindToWorldObject(float x, float y, Camera camera) {
		LIGHT_POS.x = x;
		LIGHT_POS.y = y;
		LIGHT_POS.z = 0;
		Vector3 newPos = camera.getCamera().project(LIGHT_POS);
		LIGHT_POS.x = LIGHT_POS.x/(camera.getCamera().viewportWidth*2);
		LIGHT_POS.y = LIGHT_POS.y/(camera.getCamera().viewportHeight*2);
		
		shader.setUniformf("u_pos", newPos.x, newPos.y);
	}
	
	public void resize(int width, int height) {
		shader.begin();
		shader.setUniformf("u_resolution", width, height);
		shader.end();
	}
	
	public void begin() {
		shader.begin();
	}
	
	public void end() {
		shader.end();
	}

}
