package com.tankgame.entities.projectile;

import com.tankgame.entities.Entity;
import com.tankgame.entities.tank.Tank;
import com.tankgame.utils.Movable;

public class Bullet extends Entity implements Movable {
    private double speed = 8.0;
    private int direction;
    private boolean active = true;
    private Tank owner;

    public Bullet(double x, double y, int direction, Tank owner) {
        super(x, y, "bullet_sprite");
        this.owner = owner;
        this.direction = direction;
    }

    public void update() {
        switch (direction) {
            case 0 -> moveUp();
            case 1 -> moveDown();
            case 2 -> moveLeft();
            case 3 -> moveRight();
        }
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

    @Override
    public void setDirection(int direction){
        this.direction = direction;
    }

    @Override
    public int getDirection(){
        return direction;
    }

    public boolean isActive() {
        return active;
    }

    public void destroy() {
        this.active = false;
    }

    public double getSpeed(){
        return speed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }
}
