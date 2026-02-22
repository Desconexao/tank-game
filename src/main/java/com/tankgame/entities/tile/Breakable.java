package com.tankgame.entities.tile;

public abstract class Breakable extends Tile {
    protected int health;
    protected int initialHealth;
    protected boolean isBroken = false;

    public Breakable(double x, double y, String spriteKey, int health) {
        super(x, y, spriteKey, true, true);
        this.health = health;
        this.initialHealth = health;
    }

    public int getHealth() {
        return this.health;
    }

    public int inflictDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            isBroken = true;
        }
        return health;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public abstract void setCurrentStateSprite();
}
