package com.mygdx.game.entities.playerchar;

import com.mygdx.game.magic.spells.Spell;

public class MagicBook {
    private int bookLevel = 1;
    private Spell[] firstLevelSpells;

    public MagicBook() {
        firstLevelSpells = new Spell[3];
        firstLevelSpells[0] = new Spell();
        firstLevelSpells[1] = new Spell();
        firstLevelSpells[2] = new Spell();
    }

    public void increaseLevel() {
        bookLevel++;
    }

    public int getBookLevel() {
        return bookLevel;
    }

    public void setBookLevel(int bookLevel) {
        this.bookLevel = bookLevel;
    }

    public Spell[] getFirstLevelSpells() {
        return firstLevelSpells;
    }

    public Spell getSpell(int index) {
        return firstLevelSpells[index];
    }

    public void setFirstLevelSpells(Spell[] firstLevelSpells) {
        this.firstLevelSpells = firstLevelSpells;
    }
}
