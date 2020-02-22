package com.mygdx.game.tools;

import java.util.Random;

public class RandomNumGen {
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random ranDir = new Random();
		return ranDir.nextInt((max - min) + 1) + min;
	}
}
