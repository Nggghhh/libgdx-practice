package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Shader {
	protected ShaderProgram shader;
	protected FileHandle vertexShader;
	protected FileHandle fragmentShader;
	
	public Shader(String vertex, String fragment) {
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(Gdx.files.internal("shaders/"+vertex+".vert"), Gdx.files.internal("shaders/"+vertex+".frag"));
		if(!shader.isCompiled())
			System.out.println(shader.getLog());
	}
	
	
	public ShaderProgram getShader() {
		return shader;
	}
	
	public void dispose() {
		shader.dispose();
	}
}
