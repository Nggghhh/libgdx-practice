package com.mydgx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Camera;

public class Vignette extends Shader {
	private Vector3 position;
	
	public Vignette(String vertex, String fragment) {
		super(vertex, fragment);
		position = new Vector3();
		editCircle(0.6f, 0.4f, 0.5f);
		circlePos(0.0f, 0.0f);
	}
	
	public void resize(int width, int height) {
		shader.begin();
		shader.setUniformf("u_resolution", width, height);
		shader.end();
	}
	
	public void newColor(float r, float g, float b) {
		shader.begin();
		shader.setUniformf("u_newColor[0]", r, g, b);
		shader.end();
	}
	
	public void circlePos(float x, float y) {
		shader.begin();
		shader.setUniformf("u_pos[0]", x, y);
		shader.setUniformf("u_pos[1]", x, y);
		shader.end();
	}
	
	public void bindToWorldObject(float x, float y, Camera camera) {
		position.x = x;
		position.y = y;
		position.z = 0;
		Vector3 newPos = camera.getCamera().project(position);
		position.x = position.x/(camera.getCamera().viewportWidth*2);
		position.y = position.y/(camera.getCamera().viewportHeight*2);
		
		shader.begin();
		shader.setUniformf("u_pos[0]", newPos.x, newPos.y);
		shader.setUniformf("u_pos[1]", newPos.x, newPos.y);
		shader.end();
	}
	
	public void editCircle(float outRadius, float inRadius, float intensity) {
		shader.begin();
		shader.setUniformf("u_outRadius", outRadius);
		shader.setUniformf("u_inRadius", inRadius);
		shader.setUniformf("u_intensity", intensity);
		shader.end();
	}
}
