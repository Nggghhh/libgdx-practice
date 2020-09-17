package com.mygdx.game.entities.playerchar;

import com.mygdx.game.magic.spells.Spell;

public class MagicBook {
    private int bookLevel = 1;
    private Spell[] firstLevelSpells;

    public MagicBook() {
        Spell[] firstLevelSpells = new Spell[3];
    }

    public void increaseLevel() {
        bookLevel++;
    }
}
