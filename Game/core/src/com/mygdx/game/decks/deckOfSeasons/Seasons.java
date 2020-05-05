package com.mygdx.game.decks.deckOfSeasons;

public enum Seasons {
	SEASON_OF_AMBIGUITY("Season of ambiguity"),
	SEASON_OF_BEGINNINGS("Season of beginnings"),
	SEASON_OF_RAIN_AND_THUNDER("Season of rain and thunder"),
	SEASON_OF_ANTICIPATION("Season of anticipation");
	
	
	private String name;
	
	private Seasons(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
