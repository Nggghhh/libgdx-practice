package com.mygdx.game.world.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.world.CustomGameMap;

public class AnimatedTile extends CustomTileType {
	protected int frameNum = 0;
	protected int startingFrame = 0;
	protected int animNum = 0;
	protected int animLen = 3;
	protected float timer = 0;
	protected int animSpeed = 3;
	protected boolean loop = true;
	
	public AnimatedTile(int x, int y, CustomGameMap map) {
		super(x, y, map);
	}
	
	@Override
	public void timer() {
		timer += Gdx.graphics.getDeltaTime()*animSpeed;
		if(timer > 1f) {
			timer = 0;
			if(frameNum < animLen) {
				frameNum += 1;
			}
			else if(frameNum == animLen) {
				if(loop == true) {
					frameNum = 0;
				}
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
	}
}
