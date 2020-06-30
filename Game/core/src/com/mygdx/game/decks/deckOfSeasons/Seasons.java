package com.mygdx.game.decks.deckOfSeasons;

import java.util.HashMap;

import com.mygdx.game.entities.EntityType;

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
	
	public static Seasons getSeason(String name) {
		return seasons.get(name);
	}
	
	private static HashMap<String, Seasons> seasons;
	
	static {
		seasons = new HashMap<String, Seasons>();
		for(Seasons season : Seasons.values())
			seasons.put(season.name, season);
	}
}
