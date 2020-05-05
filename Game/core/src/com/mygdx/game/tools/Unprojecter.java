package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Unprojecter {
	static Vector3 mousePos = new Vector3();
	public static Vector3 unproject(OrthographicCamera camera, Vector3 vec) {
		return camera.unproject(vec);
	}
	
	public static Vector3 project(OrthographicCamera camera, Vector3 vec) {
		return camera.project(vec);
	}
	
	public static Vector3 getMouseCoords(OrthographicCamera camera) {
		mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		return camera.unproject(mousePos);
	}
}
