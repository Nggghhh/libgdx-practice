package com.mydgx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Vignette {
	ShaderProgram shader;
	
	public Vignette() {
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/default.frag"));
		if(!shader.isCompiled())
			System.out.println(shader.getLog());
		editCircle(.3f, .2f, 0.95f);
		circlePos(0.0f, 0.0f);
	}
	
	public ShaderProgram applyShader() {
		return shader;
	}
	
	public void resize(int width, int height) {
		shader.begin();
		shader.setUniformf("u_resolution", width, height);
		shader.end();
	}
	
	public void circlePos(float x, float y) {
		shader.begin();
		shader.setUniformf("u_pos", x, y);
		shader.end();
	}
	
	public void editCircle(float outRadius, float inRadius, float intensity) {
		shader.begin();
		shader.setUniformf("u_outRadius", outRadius);
		shader.setUniformf("u_inRadius", inRadius);
		shader.setUniformf("u_intensity", intensity);
		shader.end();
	}
	
	public void dispose() {
		shader.dispose();
	}
}
