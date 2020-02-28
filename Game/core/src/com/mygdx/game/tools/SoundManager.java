package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private static final String PATH = "SOUNDS/";
	
	public static void play(String name) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(PATH+name+".wav"));
		sound.play(0.1f);
	}
}
