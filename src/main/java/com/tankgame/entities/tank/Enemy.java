package com.tankgame.entities.tank;

import com.tankgame.settings.GameConfig;
import com.tankgame.systems.MovementSystem;
import com.tankgame.systems.ai.EnemyAISystem;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

public class Enemy extends Tank {
    private long lastShotTime = 0;
    public EnemyAISystem AI;
    

    public Enemy(double x, double y, int health, String spriteKey, Direction direction, TankColors color, Class<? extends EnemyAISystem> AIClass, MovementSystem movementSystem) {
        super(x, y, health, spriteKey, direction, color);
        this.speed = 2.0;
        setDirection(direction);
        try {
            this.AI = AIClass.getConstructor(MovementSystem.class, Enemy.class).newInstance(movementSystem, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;

        String newSpriteName = "tank_";

        this.spriteKey = switch (direction) {
            case UP -> newSpriteName + "up_" + color.getValue();
            case DOWN -> newSpriteName + "down_" + color.getValue();
            case LEFT -> newSpriteName + "left_" + color.getValue();
            case RIGHT -> newSpriteName + "right_" + color.getValue();
        };
    }

    public boolean canShoot() {
        return (System.currentTimeMillis() - lastShotTime) > GameConfig.ENEMY_BULLET_COOL_DOWN_MS;
    }

    public void shootBullet() {
        lastShotTime = System.currentTimeMillis();
    }
}
