package com.tankgame.entities.tank;

import com.tankgame.entities.projectile.Bullet;

public class Enemy extends Tank {
    public Enemy(int x, int y, int health, String spriteKey, int direction) {
        super(x, y, health, spriteKey, direction);
    }

    @Override
    public void moveUp() {
        this.y--;
    }

    @Override
    public void moveDown() {
        this.y++;
    }

    @Override
    public void moveLeft() {
        this.x--;
    }

    @Override
    public void moveRight() {
        this.x++;
    }

    @Override
    public void setDirection(int direction){
        this.direction = direction;
    }

    public Bullet shoot() {
        return new Bullet(x, y, 0, this);
    }

    

}
