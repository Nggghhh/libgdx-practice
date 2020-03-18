package com.mygdx.game.tools;

public class CollisionRect {
	public float x;
	float y;
	int width, height;
	
	public CollisionRect (float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
//		System.out.println(x+" "+y);
	}
	
	public void move (float x, float y) {
		this.x = x;
		this.y = y;
//		System.out.println(x+" "+y);
	}
	
	public boolean collidesWith (CollisionRect rect) {
		return x<rect.x+rect.width && y<rect.y+rect.height && x+width>rect.x && y+height>rect.y;
	}
	
	public boolean collidesWithAtOffset (CollisionRect rect, int direction, int distance) {
		if(direction == 1)
			return (x-distance)<rect.x+rect.width && (y-12)<rect.y+rect.height && x+width>rect.x && (y+12)+height>rect.y;
		else if(direction == 2)
			return x<rect.x+rect.width && (y-12)<rect.y+rect.height && (x+distance)+width>rect.x && (y+12)+height>rect.y;
		else if(direction == 3)
			return (x-12)<rect.x+rect.width && (y-distance)<rect.y+rect.height && (x+12)+width>rect.x && y+height>rect.y;
		else
			return (x-12)<rect.x+rect.width && y<rect.y+rect.height && (x+12)+width>rect.x && (y+distance)+height>rect.y;	
	}
}
