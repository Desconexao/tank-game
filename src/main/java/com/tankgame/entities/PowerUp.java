package com.tankgame.entities;

public class PowerUp extends Entity {
    private String type;

    public PowerUp(int x, int y, String type, String spriteKey) {
        super(x, y, spriteKey);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
