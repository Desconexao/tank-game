package com.tankgame.entities;

public class Player extends Tank {
    public Player(int x, int y, int health, String spriteKey) {
        super(x, y, health, spriteKey);
    }

    @Override
    public void moveUp() {
        this.y--;
        spriteKey = "player_tank";
    }

    @Override
    public void moveDown() {
        this.y++;
        spriteKey = "player_tank_down";
    }

    @Override
    public void moveLeft() {
        this.x--;
        spriteKey = "player_tank_left";
    }

    @Override
    public void moveRight() {
        this.x++;
        spriteKey = "player_tank_right";
    }

    public void shoot() {
        // pewpew
    }
}
