package com.tankgame.entities.tank;

import com.tankgame.entities.Entity;
import com.tankgame.utils.Movable;

public abstract class Tank extends Entity implements Movable {
    protected int health;
    protected double speed;

    public Tank(double x, double y, int health, String spriteKey) {
        super(x, y, spriteKey);
        this.health = health;
        this.speed = 4.0;
    }

    public void moveUp() {
        this.y -= speed;
    }

    public void moveDown() {
        this.y += speed;
    }

    public void moveLeft() {
        this.x -= speed;
    }

    public void moveRight() {
        this.x += speed;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public abstract void shoot();
}
