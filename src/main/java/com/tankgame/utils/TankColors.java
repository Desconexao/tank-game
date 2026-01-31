package com.tankgame.utils;

public enum TankColors {
    YELLOW("yellow"), GRAY("gray"), GREEN("green"), RED("red");

    private final String color;

    TankColors(String color) {
        this.color = color;
    }

    public String getValue() {
        return color;
    }
}
