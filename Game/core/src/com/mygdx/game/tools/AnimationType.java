package com.mygdx.game.tools;

public enum AnimationType {
	IDLE("IDLE", 1, 2),
	HURT("HURT", 1, 2);
	
	private String name;
	private int row, column;
	
	private AnimationType(String name, int row, int column) {
		this.name = name;
		this.row = row;
		this.column = column;
	}
	
	public String getName() {
		return name;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
}
