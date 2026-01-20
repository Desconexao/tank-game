package com.tankgame.entities;

import com.tankgame.utils.Movable;

public abstract class Tank extends Entity implements Movable {
    protected int health;
    protected int speed;

    public Tank(int x, int y, int health, String spriteKey) {
        super(x, y, spriteKey);
        this.health = health;
        this.speed = 1;
    }

    @Override
    public void moveUp() {
        this.y -= speed;
    }

    @Override
    public void moveDown() {
        this.y += speed;
    }

    @Override
    public void moveLeft() {
        this.x -= speed;
    }

    @Override
    public void moveRight() {
        this.x += speed;
    }

    public abstract void shoot();
}
