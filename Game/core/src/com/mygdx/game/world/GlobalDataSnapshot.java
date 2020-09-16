package com.mygdx.game.world;

import com.mygdx.game.decks.deckOfSeasons.Seasons;

public class GlobalDataSnapshot {
	public int x = 0;
	public int y = 0;
	public float timeInMinutes = 700;
	public float timeInHours = 0;
	public int[] date = {1, 1, 500};
	public int moonPhase = 0, pastHour = 23;
	public String pastSeason;
	public Seasons currentSeason = Seasons.SEASON_OF_BEGINNINGS;
}
