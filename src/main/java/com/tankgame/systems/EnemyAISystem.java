package com.tankgame.systems;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.utils.Direction;
import java.util.Random;

public class EnemyAISystem {
    private final MovementSystem movementSystem;
    private final Random random = new Random();

    public EnemyAISystem(MovementSystem movementSystem) {
        this.movementSystem = movementSystem;
    }

    public void update(Enemy enemy) {
        double speed = enemy.getSpeed();
        double newX = enemy.getX();
        double newY = enemy.getY();

        switch (enemy.getDirection()) {
            case UP -> newY -= speed;
            case DOWN -> newY += speed;
            case LEFT -> newX -= speed;
            case RIGHT -> newX += speed;
        }

        boolean moved = movementSystem.tryMove(enemy, newX, newY);

        if (!moved) {
            Direction[] directions = Direction.values();
            Direction newDir = directions[random.nextInt(directions.length)];
            enemy.setDirection(newDir);
        }
    }
}
