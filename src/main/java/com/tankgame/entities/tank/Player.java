package com.tankgame.entities.tank;

public class Player extends Tank {
    public Player(double x, double y, int health, String spriteKey, int direction) {
        super(x, y, health, spriteKey, direction);
    }

    @Override
    public void moveUp() {
        super.moveUp();
        setDirection(0);
    }

    @Override
    public void moveDown() {
        super.moveDown();
        setDirection(1);
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        setDirection(2);
    }

    @Override
    public void moveRight() {
        super.moveRight();
        setDirection(3);
    }

    @Override
    public void setDirection(int direction){
        this.direction = direction;

        switch (direction) {
            case 0:
                this.spriteKey = "player_tank";
                break;
            
            case 1:
                this.spriteKey = "player_tank_down";
                break;

            case 2:
                this.spriteKey = "player_tank_left";
                break;

            case 3:
                this.spriteKey = "player_tank_right";
                break;
        
            default:
                this.spriteKey = "player_tank";
                break;
        }
    }
}
