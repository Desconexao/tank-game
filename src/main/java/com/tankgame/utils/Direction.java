package com.tankgame.utils;

public enum Direction {
    UP(0), DOWN(1), LEFT(2), RIGHT(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
