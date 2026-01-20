package com.tankgame.entities;

public abstract class Entity {
    protected int x, y;
    protected String spriteKey;

    public Entity(int x, int y, String spriteKey) {
        this.x = x;
        this.y = y;
        this.spriteKey = spriteKey;
    }

    public String getSpriteKey() {
        return spriteKey;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
