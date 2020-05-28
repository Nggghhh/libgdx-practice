package com.mygdx.game.world;

import java.text.DecimalFormat;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.decks.deckOfSeasons.Seasons;

public class DayAndNightCycles {
	private float timeInMinutes = 800;
	private float timeInHours = 0;
	private int pastHour = 23;
	private int[] date;
	private int timeAcceleration = 1;
	private Seasons pastSeason;
	private Seasons currentSeason;
	private int moonPhase = 0;
	private DecimalFormat df;
	private Vector3 ambientLight;
	
	public DayAndNightCycles() {
		this.date = new int[3];
		this.date[0] = 1; //day
		this.date[1] = 1; //month
		this.date[2] = 500; //year
		this.currentSeason = Seasons.SEASON_OF_BEGINNINGS;
		df = new DecimalFormat("00");
		ambientLight = new Vector3(1.0f, 1.0f, 1.0f);
	}
	public void timePasses(CustomGameMap map, float delta) {
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
//		ambientLight.set(timeInMinutes/1440, timeInMinutes/1440, timeInMinutes/1440);
		
		if(pastHour != (int) timeInHours) {
			if(timeInHours > 21 || timeInHours < 4) {
				ambientLight.set(0.05f, 0.05f, 0.1f);
				map.setUpdate(false);
			}
			else if(timeInHours > 20) {
				ambientLight.set(0.2f, 0.2f, 0.2f);
				map.setUpdate(false);
			}
			else if(timeInHours > 19) {
				ambientLight.set(0.5f, 0.3f, 0.2f);
				map.setUpdate(false);
			}
			else if(timeInHours > 18) {
				ambientLight.set(0.6f, 0.35f, 0.3f);
				map.setUpdate(false);
			}
			else if(timeInHours > 17) {
				ambientLight.set(0.5f, 0.7f, 0.7f);
				map.setUpdate(false);
			}
			else if(timeInHours > 16) {
				ambientLight.set(0.6f, 0.8f, 0.8f);
				map.setUpdate(false);
			}
			else if(timeInHours < 5) {
				ambientLight.set(0.1f, 0.2f, 0.2f);
				map.setUpdate(false);
			}
			else if(timeInHours < 6) {
				ambientLight.set(0.2f, 0.3f, 0.3f);
				map.setUpdate(false);
			}
			else if(timeInHours < 7) {
				ambientLight.set(0.3f, 0.4f, 0.4f);
				map.setUpdate(false);
			}
			else if(timeInHours < 8) {
				ambientLight.set(0.4f, 0.5f, 0.5f);
				map.setUpdate(false);
			}
			else if(timeInHours < 9) {
				ambientLight.set(0.5f, 0.6f, 0.6f);
				map.setUpdate(false);
			}
			else {
				ambientLight.set(1.0f, 1.0f, 1.0f);
				map.setUpdate(false);
			}
			pastHour = (int) timeInHours;
			map.setUpdate(false);
		}

//		System.out.println(df.format((int) timeInHours)+":"+df.format((int) timeInMinutes%60)+" "+date[0]+" day"+" "+date[1]+" month");
//		System.out.println((int) timeInMinutes);
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
	
	public Vector3 getLight() {
		return ambientLight;
	}
}
