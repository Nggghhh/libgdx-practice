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
}
