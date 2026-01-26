package com.tankgame.entities.tank;

import com.tankgame.utils.Direction;

public class Enemy extends Tank {
    public Enemy(double x, double y, int health, String spriteKey, Direction direction) {
        super(x, y, health, spriteKey, direction);
        this.speed = 10.0;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        this.spriteKey = "enemy_tank_" + direction.name().toLowerCase();
    }
}
