package com.tankgame.entities.projectile;

import com.tankgame.entities.Entity;
import com.tankgame.entities.tank.Tank;
import com.tankgame.utils.Direction;
import com.tankgame.utils.Movable;

public class Bullet extends Entity implements Movable {
    private double speed = 8.0;
    private Direction direction;
    private Tank owner;

    public Bullet(double x, double y, Direction direction, Tank owner) {
        super(x, y, "bullet_vertical");
        this.direction = direction;
        this.owner = owner;
    }

    public void update() {
        switch (direction) {
            case UP -> moveUp();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
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
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }
}
