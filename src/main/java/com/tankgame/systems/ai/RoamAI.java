package com.tankgame.systems.ai;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.managers.CollisionManager;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;

public class RoamAI extends EnemyAISystem {

    public RoamAI(CollisionManager collisionManager, Enemy enemy) {
        super(collisionManager, enemy);
    }

    @Override
    public void update() {
        double speed = enemy.getSpeed();
        double newX = enemy.getX();
        double newY = enemy.getY();

        if (random.nextInt(100) < 1) {
            enemy.setDirection(directions[random.nextInt(directions.length)]);
        }

        switch (enemy.getDirection()) {
            case UP -> newY -= speed;
            case DOWN -> newY += speed;
            case LEFT -> newX -= speed;
            case RIGHT -> newX += speed;
        }

        boolean moved = false;
        if (collisionManager.canMove(newX, newY, GameConfig.TANK_SIZE)) {
            enemy.setX(newX);
            enemy.setY(newY);
            moved = true;
        }

        if (!moved) {
            Direction newDir = directions[random.nextInt(directions.length)];
            enemy.setDirection(newDir);
        }
    }
}
