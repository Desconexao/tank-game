package com.tankgame.entities;

public abstract class Entity {
    protected double x, y;
    protected String spriteKey;

    public Entity(double x, double y, String spriteKey) {
        this.x = x;
        this.y = y;
        this.spriteKey = spriteKey;
    }

    public String getSpriteKey() {
        return spriteKey;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
