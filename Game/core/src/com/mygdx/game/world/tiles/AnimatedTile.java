package com.mygdx.game.world.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedTile extends CustomTileType {
	protected int frameNum = 0;
	protected int animNum = 0;
	protected int animLen = 3;
	protected float timer = 0;
	protected int animSpeed = 3;
	protected boolean loop = true;
	
	public AnimatedTile(String name, int x, int y) {
		super(name, x, y);
	}
	
	@Override
	public void render(SpriteBatch batch) {
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
		batch.draw(TileTextureManager.getLiquids(this.variation, this.frameNum), x, y);
	}
}
