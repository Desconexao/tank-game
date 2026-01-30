package com.tankgame.systems.ai;

import java.util.Random;

import com.tankgame.systems.MovementSystem;
import com.tankgame.utils.MovementAI;

public abstract class EnemyAISystem implements MovementAI{
    protected final MovementSystem movementSystem;
    protected final Random random = new Random();

    public EnemyAISystem(MovementSystem movementSystem) {
        this.movementSystem = movementSystem;
    }

    
}
