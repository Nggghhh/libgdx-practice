package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Camera;
import com.mygdx.game.world.CustomGameMap;

public class Raycaster {
	public float lenght = 0, originX, originY, oldOriginX, oldOriginY, newX, newY;
	double angleToTransform = 0;
	ShapeRenderer sh;
	
	public Raycaster() {
		sh = new ShapeRenderer();
	}
	
	public void raycast(CustomGameMap map, SpriteBatch batch, Camera camera) {
		lenght = 0;
		
		while(true) {
			double angle = Math.toRadians(angleToTransform);
			
			originX = map.getTileTypeByLocation(0, map.getHero().getX(), map.getHero().getY()).x+8;
			originY = map.getTileTypeByLocation(0, map.getHero().getX(), map.getHero().getY()).y+8;
			
			float x = originX+lenght;
			float y = originY+lenght;
			
			newX = (float) (Math.cos(angle)*(x-originX) - Math.sin(angle)*(y-originY)+originX);
			newY = (float) (Math.sin(angle)*(x-originX) + Math.cos(angle)*(y-originY)+originY);
			lenght++;
			for(int layer = 0; layer < map.getLayers(); layer++) {
				if(map.getCells()[(int) newX/16][(int) newY/16][layer].tile != null && map.getCells()[(int) newX/16][(int) newY/16][layer].tile.isVisible())
					map.getCells()[(int) newX/16][(int) newY/16][layer].render = true;
			}
			
			if(newX < camera.getLeftB() || newY < camera.getBottomB() || newX > camera.getRightB() || newY > camera.getTopB() || map.getTileTypeByCoordinate((int)newX/16, (int)newY/16, 1).tile.isCollidable()) {
				break;
			}
		}
		angleToTransform += 2;
	}
	
	public void raycastAtAngle(int angleToTransform, CustomGameMap map, Camera camera) {
		lenght = 0;
		
		originX = map.getTileTypeByLocation(0, map.getHero().getX(), map.getHero().getY()).x+8;
		originY = map.getTileTypeByLocation(0, map.getHero().getX(), map.getHero().getY()).y+8;
		
		while(true) {
			double angle = Math.toRadians(angleToTransform);
			
			float x = originX+lenght;
			float y = originY+lenght;
			
			newX = (float) (Math.cos(angle)*(x-originX) - Math.sin(angle)*(y-originY)+originX);
			newY = (float) (Math.sin(angle)*(x-originX) + Math.cos(angle)*(y-originY)+originY);
			lenght += 4;
			for(int layer = 0; layer < map.getLayers(); layer++) {
				if(map.getCells()[(int) newX/16][(int) newY/16][layer].tile != null && map.getCells()[(int) newX/16][(int) newY/16][layer].tile.isVisible())
					map.getCells()[(int) newX/16][(int) newY/16][layer].render = true;
			}
			
			if(newX < camera.getLeftB() || newY < camera.getBottomB() || newX > camera.getRightB() || newY > camera.getTopB() || map.getTileTypeByCoordinate((int)newX/16, (int)newY/16, 1).tile.isCollidable()) {
				break;
			}
		}
	}
	
	public void updateTiles(CustomGameMap map) {
		for(int layer = 0; layer < map.getLayers(); layer++)
			for(int row = map.getWidth()-1; row > 0; row--)
				for(int col = map.getHeight()-1; col > 0; col--)
					if(map.getCells()[row][col][layer] != null)
						map.getCells()[row][col][layer].render = false;
	}
	
	public void visualizeRay(Camera camera) {
		sh.begin(ShapeType.Line);
		sh.setProjectionMatrix(camera.getCamera().combined);
		sh.setColor(255, 0, 0, 255);
		sh.line(originX, originY, newX, newY);
		sh.end();
		
//		if(oldOriginX != originX || oldOriginY != originY) {
//			while((int)newX/16 > 0 && (int)newY/16 > 0 && (int)newX/16 < 99 && (int)newY/16 < 99) {
//				if(newX < (map.getWidth()-1)*16 && newY < (map.getHeight()-1)*16 && newX > 16 && newY > 16) {
//					if(map.getTileTypeByLocation(1, newX, newY).tile != null && !map.getTileTypeByLocation(1, newX, newY).tile.isCollidable()) {
//						lenght += 1;
//						System.out.println(newX);
//					}
//					else {
//						finalX = (int) newX/16;
//						finalY = (int) newY/16;
//						System.out.println(finalX+" "+finalY+" "+" "+originX/16+" "+originY/16+" "+lenght);
//					}
//				}
//				else {
//					finalX = (int) newX/16;
//					finalY = (int) newY/16;
//					System.out.println(finalX+" "+finalY);
//				}
//			}
//			System.out.println("new");
//		}
	}
}
