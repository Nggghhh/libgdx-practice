package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.world.GameMap;

public class Brazier extends Entity {

    @Override
    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);
        changeState("IDLE", true, 3, 6);
    }

    @Override
    public void hurt(int damage, Entity hitter, Entity receiver) {

    }

    @Override
    public void attack(int damage, Entity hitter, float velocity, String type) {

    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        animationPlay(batch);
    }

    @Override
    public void recreate(int x, int y, int health) {

    }
}
