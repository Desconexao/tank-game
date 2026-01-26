package com.tankgame.entities.tank;

import com.tankgame.utils.Direction;

public class Player extends Tank {
    public Player(double x, double y, int health, String spriteKey, Direction direction) {
        super(x, y, health, spriteKey, direction);
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
            case UP -> "player_tank";
            case DOWN -> "player_tank_down";
            case LEFT -> "player_tank_left";
            case RIGHT -> "player_tank_right";
        };
    }
}
