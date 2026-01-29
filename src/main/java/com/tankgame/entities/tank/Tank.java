package com.tankgame.entities.tank;

import com.tankgame.entities.Entity;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.Movable;

public abstract class Tank extends Entity implements Movable {
    protected int health;
    protected double speed = GameConfig.PLAYER_SPEED;
    protected Direction direction;

    public Tank(double x, double y, int health, String spriteKey, Direction direction) {
        super(x, y, spriteKey);
        this.health = health;
        this.direction = direction;
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

    public Bullet shoot() {
        int p_Size = GameConfig.TILE_SIZE;
        int b_Size = GameConfig.BULLET_SIZE;
        int centerOffset = (p_Size - b_Size) / 2;

        double bulletX = x + centerOffset;
        double bulletY = y + centerOffset;

        switch (direction) {
            case UP -> bulletY = y - b_Size;
            case DOWN -> bulletY = y + p_Size;
            case LEFT -> bulletX = x - b_Size;
            case RIGHT -> bulletX = x + p_Size;
        }
        return new Bullet(bulletX, bulletY, direction, this);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }
}
