package com.tankgame.systems;

import com.tankgame.entities.tank.Tank;
import com.tankgame.managers.CollisionManager;
import com.tankgame.settings.GameConfig;

public class MovementSystem {
    private CollisionManager collisionManager;

    public MovementSystem(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    public boolean tryMove(Tank tank, double newX, double newY) {
        if (collisionManager.canMove(newX, newY, GameConfig.TANK_SIZE)) {
            tank.setX(newX);
            tank.setY(newY);
            return true;
        }
        return false;
    }
}
