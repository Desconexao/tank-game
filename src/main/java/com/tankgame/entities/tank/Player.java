package com.tankgame.entities.tank;

import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

public class Player extends Tank {
    public Player(double x, double y, int health, String spriteKey, Direction direction, TankColors color) {
        super(x, y, health, spriteKey, direction, color);
    }

    @Override
    public void moveUp() {
        super.moveUp();
        setDirection(Direction.UP);
    }

    @Override
    public void moveDown() {
        super.moveDown();
        setDirection(Direction.DOWN);
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        setDirection(Direction.LEFT);
    }

    @Override
    public void moveRight() {
        super.moveRight();
        setDirection(Direction.RIGHT);
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        this.spriteKey = switch (direction) {
            case UP -> "tank_up_gray";
            case DOWN -> "tank_down_gray";
            case LEFT -> "tank_left_gray";
            case RIGHT -> "tank_right_gray";
        };
    }
}
