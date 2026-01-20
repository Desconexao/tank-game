package com.tankgame.entities;

public class Player extends Tank {
    public Player(int x, int y, int health, String spriteKey) {
        super(x, y, health, spriteKey);
    }

    @Override
    public void moveUp() {
        this.y--;
    }

    @Override
    public void moveDown() {
        this.y++;
    }

    @Override
    public void moveLeft() {
        this.x--;
    }

    @Override
    public void moveRight() {
        this.x++;
    }

    public void shoot() {
        // pewpew
    }
}
