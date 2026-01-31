package com.tankgame.systems.ai;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.systems.MovementSystem;
import com.tankgame.utils.Direction;

public class RoamAI extends EnemyAISystem{
    public RoamAI(MovementSystem movementSystem, Enemy enemy) {
        super(movementSystem, enemy);
    }
    
    @Override
    public void update() {
        double speed = enemy.getSpeed();
        double newX = enemy.getX();
        double newY = enemy.getY();
 
        // 50% percent of the next direction be the same
        int movementOportunity = random.nextInt(1000);
        if (movementOportunity < 10){
            enemy.setDirection(directions[random.nextInt(directions.length)]);
        }

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
