package com.tankgame.entities.tile;

public class Water extends Tile {
    public Water(double x, double y, String spriteKey, boolean collidable) {
        super(x, y, spriteKey, collidable, false);
    }
}
