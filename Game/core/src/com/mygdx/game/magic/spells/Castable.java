package com.mygdx.game.magic.spells;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.world.CustomGameMap;
import com.mygdx.game.world.GameMap;

public interface Castable {
    void cast(Entity caster, GameMap map, int angle);
    void passiveEffect(float deltaTime, Entity caster);
}
