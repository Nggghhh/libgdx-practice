package com.mygdx.game.items;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.animations.EntityAssetManager;
import com.mygdx.game.world.GameMap;

public abstract class Weapons extends Items {
	protected int impact;
	protected int damage;
	protected int speed;
	protected int hitNum = 0;
	protected int maxHitNum = 1;
	
	protected String firstSkillTextureKey1;
	protected String firstSkillTextureKey2;
	protected String secondSkillTextureKey;
	protected String thirdSkillTextureKey;
	
	public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
		super.create(snapshot, type, map);
	}
	
	public void update(Entity user, GameMap map) {
		if(hitNum > maxHitNum)
			hitNum = 0;
	}
	
	public abstract void firstSkill(Entity user, GameMap map);
	public abstract void secondSkill(Entity user, GameMap map);	
	public abstract void thirdSkill(Entity user, GameMap map);
	
	public void dispose() {
		EntityAssetManager.dispose(firstSkillTextureKey1);
		EntityAssetManager.dispose(firstSkillTextureKey2);
		EntityAssetManager.dispose(secondSkillTextureKey);
		EntityAssetManager.dispose(thirdSkillTextureKey);
	}
}
