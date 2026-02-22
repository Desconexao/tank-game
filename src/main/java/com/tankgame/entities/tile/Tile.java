package com.tankgame.entities.tile;

import com.tankgame.entities.Entity;

public class Tile extends Entity {
    protected boolean collidable;
    protected boolean blocksProjectiles;

    public Tile(double x, double y, String spriteKey, boolean collidable) {
        this(x, y, spriteKey, collidable, collidable);
    }

    public Tile(double x, double y, String spriteKey, boolean collidable, boolean blocksProjectiles) {
        super(x, y, spriteKey);
        this.collidable = collidable;
        this.blocksProjectiles = blocksProjectiles;
    }

    public boolean isCollidable() {
        return this.collidable;
    }

    public boolean doesBlockProjectiles() {
        return this.blocksProjectiles;
    }

    public void setCollision(boolean collision) {
        this.collidable = collision;
    }

    public int inflictDamage(int damage) {
        return 0;
    }

    public boolean isBroken() {
        return false;
    }

    public boolean isRemovableWhenBroken() {
        return true;
    }
}
