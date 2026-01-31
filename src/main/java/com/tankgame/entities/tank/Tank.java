package com.tankgame.entities.tank;

import com.tankgame.entities.Entity;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.Movable;
import com.tankgame.utils.TankColors;

public abstract class Tank extends Entity implements Movable {
    protected int health;
    protected double speed = GameConfig.PLAYER_SPEED;
    protected Direction direction;
    protected TankColors color;

    

    public Tank(double x, double y, int health, String spriteKey, Direction direction, TankColors color) {
        super(x, y, spriteKey);
        this.health = health;
        this.direction = direction;
        this.color = color;
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
        int p_Size = GameConfig.TANK_SIZE;
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
        String newSpriteName = "tank_";

        switch (direction) {
            case UP -> newSpriteName += "up_" + color.getValue();
            case DOWN -> newSpriteName += "down_" + color.getValue();
            case LEFT -> newSpriteName += "left_" + color.getValue();
            case RIGHT -> newSpriteName += "right_" + color.getValue();
        }

        this.spriteKey = newSpriteName;
        System.out.println(newSpriteName);
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
