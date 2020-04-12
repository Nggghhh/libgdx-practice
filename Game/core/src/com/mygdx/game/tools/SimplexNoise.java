package com.mygdx.game.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import com.mygdx.game.world.CustomGameMapData;

public class SimplexNoise {
	private static final int LAYERS = 5;
	
	public float[][] Create(int width, int height, int octave, int seed) {
		return GeneratePerlinNoise(GenerateSmoothNoise(noise(width, height, seed),octave),octave);
	}
	
	public float[][] CreateRoundedArray(int width, int height, int octave, int roundUp, int seed) {
		float[][] noiseArray = GeneratePerlinNoise(GenerateSmoothNoise(noise(width, height, seed),octave),octave);
		for(int row = 0; row<noiseArray.length; row++ )
			for(int col = 0; col<noiseArray.length; col++ )
				noiseArray[row][col] = (float) round(noiseArray[row][col], roundUp);
		return noiseArray;
	}
	
	public float[][] noise(int width, int height, int seed)
	{
	    Random random = new Random(seed); //Seed to 0 for testing
	    float[][] noise = new float[width][height];
	 
	    for (int i = 0; i < width; i++)
	    {
	        for (int j = 0; j < height; j++)
	        {
	            noise[i][j] = (float)random.nextDouble() % 1;
	        }
	    }
	 
	    return noise;
	}
	public float[][] GenerateSmoothNoise(float[][] baseNoise, int octave)
	{
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][] smoothNoise = new float[width][height];
	 
	   int samplePeriod = 1 << octave; // calculates 2 ^ k
	   float sampleFrequency = 1.0f / samplePeriod;
	 
	   for (int i = 0; i < width; i++)
	   {
	      //calculate the horizontal sampling indices
	      int sample_i0 = (i / samplePeriod) * samplePeriod;
	      int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
	      float horizontal_blend = (i - sample_i0) * sampleFrequency;
	 
	      for (int j = 0; j < height; j++)
	      {
	         //calculate the vertical sampling indices
	         int sample_j0 = (j / samplePeriod) * samplePeriod;
	         int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
	         float vertical_blend = (j - sample_j0) * sampleFrequency;
	 
	         //blend the top two corners
	         float top = Interpolate(baseNoise[sample_i0][sample_j0],
	            baseNoise[sample_i1][sample_j0], horizontal_blend);
	 
	         //blend the bottom two corners
	         float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
	            baseNoise[sample_i1][sample_j1], horizontal_blend);
	 
	         //final blend
	         smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
	      }
	   }
	 
	   return smoothNoise;
	}
	
	private float Interpolate(float x0, float x1, float alpha)
	{
	   return x0 * (1 - alpha) + alpha * x1;
	}
	
	public float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount)
	{
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing
	 
	   float persistance = 0.5f;
	 
	   //generate smooth noise
	   for (int i = 0; i < octaveCount; i++)
	   {
	       smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
	   }
	 
	    float[][] perlinNoise = new float[width][height];
	    float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
	 
	    //blend noise together
	    for (int octave = octaveCount - 1; octave >= 0; octave--)
	    {
	       amplitude *= persistance;
	       totalAmplitude += amplitude;
	 
	       for (int i = 0; i < width; i++)
	       {
	          for (int j = 0; j < height; j++)
	          {
	             perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
	          }
	       }
	    }
	 
	   //Normalization
	   for (int i = 0; i < width; i++)
	   {
	      for (int j = 0; j < height; j++)
	      {
	         perlinNoise[i][j] /= totalAmplitude;
	      }
	   }
	 
	   return perlinNoise;
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public float[][][] generateBaseTerrain(float[][] array) {
		float[][][] terrain = new float[array.length][array[0].length][2];
		
		for(int layer = 0; layer<terrain[0][0].length; layer++)
			for(int row = 0; row<terrain.length; row++ )
				for(int col = 0; col<terrain[0].length; col++ ) {
					if(array[row][col] < 0.4) //deep water
						terrain[row][col][0] = 1;
					else if(array[row][col] < 0.5 && array[row][col] > 0.4) //shallow water
						terrain[row][col][0] = 2;
					else if(array[row][col] == 0.5) //sand
						terrain[row][col][0] = 3;
					else //grass
						terrain[row][col][0] = 4;
					
					if(array[row][col] > 0.81) //mountains
						terrain[row][col][1] = 5;
					else //empty space
						terrain[row][col][1] = 0;
			}
		return terrain;
	}
}