package com.tankgame.utils;

public interface Movable {
    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void setDirection(Direction direction);

    Direction getDirection();
}
