package com.tankgame.entities.tank;

import com.tankgame.utils.Direction;
import com.tankgame.settings.GameConfig;

public class Enemy extends Tank {
    private long lastShotTime = 0;

    public Enemy(double x, double y, int health, String spriteKey, Direction direction) {
        super(x, y, health, spriteKey, direction);
        this.speed = 2.0;
        setDirection(direction);
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        this.spriteKey = "enemy_tank";
    }

    public boolean canShoot() {
        return (System.currentTimeMillis() - lastShotTime) > GameConfig.ENEMY_BULLET_COOL_DOWN_MS;
    }

    public void shootBullet() {
        lastShotTime = System.currentTimeMillis();
    }
}
