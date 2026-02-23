package com.tankgame.systems.ai;

import java.util.Random;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.managers.CollisionManager;
import com.tankgame.utils.Direction;
import com.tankgame.utils.Movable;

public abstract class EnemyAISystem implements Movable {
    protected Direction[] directions;
    protected final CollisionManager collisionManager;
    protected final Random random = new Random();
    protected Enemy enemy;

    public EnemyAISystem(CollisionManager collisionManager, Enemy enemy) {
        this.directions = Direction.values();
        this.enemy = enemy;
        this.collisionManager = collisionManager;
    }
}
