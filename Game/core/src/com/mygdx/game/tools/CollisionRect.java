package com.mygdx.game.tools;

public class CollisionRect {
	public float x;
	float y;
	int width, height;
	boolean isCollisionEnabled;
	
	public CollisionRect (float x, float y, int width, int height, boolean isEnabled) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isCollisionEnabled = isEnabled;
	}
	
	public void move (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean collidesWith (CollisionRect rect) {
		if(isCollisionEnabled)
			return x<rect.x+rect.width && y<rect.y+rect.height && x+width>rect.x && y+height>rect.y;
		return false;
	}
	
	public boolean collidesWithTile(int tileX, int tileY) {
		if(isCollisionEnabled)
			return x<tileX+16 && y<tileY+16 && x+width>tileX && y+height>tileY;
		return false;
	}
	
	public boolean collidesWithAtOffset (CollisionRect rect, int direction, int distance, int range) {
		if(isCollisionEnabled) {
			switch (direction) {
				case 1:
					return (x-distance)<rect.x+rect.width && (y-range)<rect.y+rect.height && x+width>rect.x && (y+range)+height>rect.y;
				case 2:
					return x<rect.x+rect.width && (y-range)<rect.y+rect.height && (x+distance)+width>rect.x && (y+range)+height>rect.y;
				case 3:
					return (x-range)<rect.x+rect.width && (y-distance)<rect.y+rect.height && (x+range)+width>rect.x && y+height>rect.y;
				default:
					return (x-range)<rect.x+rect.width && y<rect.y+rect.height && (x+range)+width>rect.x && (y+distance)+height>rect.y;
			}
//			if(direction == 1)
//				return (x-distance)<rect.x+rect.width && (y-range)<rect.y+rect.height && x+width>rect.x && (y+range)+height>rect.y;
//			else if(direction == 2)
//				return x<rect.x+rect.width && (y-range)<rect.y+rect.height && (x+distance)+width>rect.x && (y+range)+height>rect.y;
//			else if(direction == 3)
//				return (x-range)<rect.x+rect.width && (y-distance)<rect.y+rect.height && (x+range)+width>rect.x && y+height>rect.y;
//			else
//				return (x-range)<rect.x+rect.width && y<rect.y+rect.height && (x+range)+width>rect.x && (y+distance)+height>rect.y;
		}
		return false;
	}
	
	public boolean isEnabled() {
		return isCollisionEnabled;
	}
	
	public void setEnabled(boolean isCollisionEnabled) {
		this.isCollisionEnabled = isCollisionEnabled;
	}
}
