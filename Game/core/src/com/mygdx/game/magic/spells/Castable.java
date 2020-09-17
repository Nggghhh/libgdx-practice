package com.mygdx.game.magic.spells;

public interface Castable {
    public abstract void cast();
    public abstract void passiveEffect(float deltaTime);
}
