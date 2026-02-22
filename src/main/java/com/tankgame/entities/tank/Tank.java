package com.tankgame.entities.tank;

import com.tankgame.entities.Entity;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;
import com.tankgame.utils.Team;

public abstract class Tank extends Entity {
    protected int health;
    protected double speed = GameConfig.PLAYER_SPEED;
    protected Direction direction;
    protected TankColors color;
    protected int bulletDamage;
    protected Team team;

    public Tank(double x, double y, int health, String spriteKey, Direction direction, TankColors color, Team team) {
        super(x, y, spriteKey);
        this.health = health;
        this.direction = direction;
        this.color = color;
        this.bulletDamage = GameConfig.DEFAULT_BULLET_DAMAGE;
        this.team = team;
        setDirection(direction);
    }

    public void moveUp() {
        this.y -= speed;
        setDirection(Direction.UP);
    }

    public void moveDown() {
        this.y += speed;
        setDirection(Direction.DOWN);
    }

    public void moveLeft() {
        this.x -= speed;
        setDirection(Direction.LEFT);
    }

    public void moveRight() {
        this.x += speed;
        setDirection(Direction.RIGHT);
    }

    public Bullet shoot() {
        int playerSize = GameConfig.TANK_SIZE;
        int bulletSize = GameConfig.BULLET_SIZE;
        int centerOffset = (playerSize - bulletSize) / 2;

        double bulletX = x + centerOffset;
        double bulletY = y + centerOffset;

        switch (direction) {
            case UP -> bulletY = y - bulletSize;
            case DOWN -> bulletY = y + playerSize;
            case LEFT -> bulletX = x - bulletSize;
            case RIGHT -> bulletX = x + playerSize;
        }
        Bullet bullet = new Bullet(bulletX, bulletY, direction, this);
        new Thread(bullet).start();
        return bullet;
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
    }

    public Team getTeam() {
        return team;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public double getSpeed() {
        return speed;
    }

    public int getBulletDamage() {
        return this.bulletDamage;
    }

    public void setBulletDamage(int newDamage) {
        this.bulletDamage = newDamage;
    }
}
