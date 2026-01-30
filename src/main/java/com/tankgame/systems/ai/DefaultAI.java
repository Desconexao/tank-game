package com.tankgame.systems.ai;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.systems.MovementSystem;
import com.tankgame.utils.Direction;

public class DefaultAI extends EnemyAISystem{


    public DefaultAI(MovementSystem movementSystem) {
        super(movementSystem);
    }
    
    @Override
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
