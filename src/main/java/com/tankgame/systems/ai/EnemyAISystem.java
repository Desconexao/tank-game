package com.tankgame.systems.ai;

import java.util.Random;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.systems.MovementSystem;
import com.tankgame.utils.Direction;
import com.tankgame.utils.MovementAI;

public abstract class EnemyAISystem implements MovementAI{
    protected Direction[] directions;
    protected final MovementSystem movementSystem;
    protected final Random random = new Random();
    protected Enemy enemy;

    public EnemyAISystem(MovementSystem movementSystem, Enemy enemy) {
        this.directions = Direction.values();
        this.enemy = enemy;
        this.movementSystem = movementSystem;
    }

    
}
