package com.tankgame.entities.projectile;

import com.tankgame.entities.Entity;
import com.tankgame.entities.tank.Tank;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;

public class Bullet extends Entity implements Runnable {
    private double speed = GameConfig.BULLET_SPEED;
    private Direction direction;
    private volatile boolean markedForRemoval = false;
    private Tank owner;

    private volatile boolean isPaused = false;
    private volatile boolean gameIsRunning = true;

    public Bullet(double x, double y, Direction direction, Tank owner) {
        super(x, y, "bullet_vertical");
        this.direction = direction;
        this.owner = owner;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void stopGame() {
        this.gameIsRunning = false;
    }

    @Override
    public void run() {
        while (!markedForRemoval && gameIsRunning) {

            if (!isPaused) {
                update();
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void update() {
        switch (direction) {
            case UP -> this.y -= speed;
            case DOWN -> this.y += speed;
            case LEFT -> this.x -= speed;
            case RIGHT -> this.x += speed;
        }
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public Tank getOwner() {
        return owner;
    }

    public Direction getDirection() {
        return direction;
    }
}
