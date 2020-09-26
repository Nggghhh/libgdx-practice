package com.mygdx.game.magic.spells;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.EntitySnapshot;
import com.mygdx.game.entities.EntityType;
import com.mygdx.game.entities.playerchar.Player;
import com.mygdx.game.entities.projectiles.MagicArrow;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public class Spell implements Castable {
    public Spell(){

    }

    @Override
    public void cast(Entity caster, GameMap map, int angle) {
        EntitySnapshot arrow = new EntitySnapshot();
        arrow.x = rotateX(caster.getX(), 8, angle);
        arrow.y = rotateY(caster.getY(), 8, angle);
        arrow.type = "MAGIC_ARROW";
        arrow.HEALTH = 2;
//        for(int i = -1; i<2; i++) {
//            Entity entity = EntityType.createEntityUsingSnapshot(arrow, (CustomGameMap) map);
//            entity.setAngle(angle + i*5);
//            ((MagicArrow) entity).setSpeed(80);
//            map.getEntities().add(entity);
//        }
        Entity entity = EntityType.createEntityUsingSnapshot(arrow, (CustomGameMap) map);
        assert entity != null;
        entity.setAngle(angle);
        ((MagicArrow) entity).setSpeed(80);
        ((MagicArrow) entity).setOrigAngle(angle);
        map.getEntities().add(entity);
    }

    @Override
    public void passiveEffect(float deltaTime, Entity caster) {

    }

    public float rotateX(float x, int length, int angleToTransform) {
        double angle = Math.toRadians(angleToTransform-225);
        float newX = (float) (Math.cos(angle) - Math.sin(angle));
        return x+(length*newX);
    }

    public float rotateY(float y, int length, int angleToTransform) {
        double angle = Math.toRadians(angleToTransform-225);
        float newY = (float) (Math.sin(angle) + Math.cos(angle));
        return y+(length*newY);
    }
}
