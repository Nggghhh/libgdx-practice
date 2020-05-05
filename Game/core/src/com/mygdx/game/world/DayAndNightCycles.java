package com.mygdx.game.world;

import java.text.DecimalFormat;

import com.mygdx.game.decks.deckOfSeasons.Seasons;

public class DayAndNightCycles {
	private float timeInMinutes = 0;
	private float timeInHours = 0;
	private int[] date;
	private int timeAcceleration = 90;
	private Seasons pastSeason;
	private Seasons currentSeason;
	private int moonPhase = 0;
	private DecimalFormat df;
	
	public DayAndNightCycles() {
		this.date = new int[3];
		this.date[0] = 1; //day
		this.date[1] = 1; //month
		this.date[2] = 500; //year
		this.currentSeason = Seasons.SEASON_OF_BEGINNINGS;
		df = new DecimalFormat("00");
	}
	public void timePasses(GameMap map, float delta) {
		timeInMinutes += delta*timeAcceleration;
		timeInHours = timeInMinutes/60;
		if(timeInHours >= 24) {
			timeInHours = 0;
			timeInMinutes = 0;
			date[0]++;
		}
		if(date[0] >= 31) {
			date[0] = 1;
			date[1]++;
		}
		if(date[1] >= 13) {
			date[1] = 1;
			date[2]++;
		}
//		System.out.println(df.format((int) timeInHours)+":"+df.format((int) timeInMinutes%60)+" "+date[0]+" day"+" "+date[1]+" month");
	}
	
	public float getHours() {
		return timeInHours;
	}
	
	public float getMinutes() {
		return timeInMinutes;
	}
	
	public int getMoonPhase() {
		return moonPhase;
	}
}
