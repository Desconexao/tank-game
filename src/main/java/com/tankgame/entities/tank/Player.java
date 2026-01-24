package com.tankgame.entities.tank;

public class Player extends Tank {
    public Player(double x, double y, int health, String spriteKey) {
        super(x, y, health, spriteKey);
    }

    @Override
    public void moveUp() {
        super.moveUp();
        this.spriteKey = "player_tank";
    }

    @Override
    public void moveDown() {
        super.moveDown();
        this.spriteKey = "player_tank_down";
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        this.spriteKey = "player_tank_left";
    }

    @Override
    public void moveRight() {
        super.moveRight();
        this.spriteKey = "player_tank_right";
    }

    @Override
    public void shoot() {
        // kabum pow pow
    }
}
