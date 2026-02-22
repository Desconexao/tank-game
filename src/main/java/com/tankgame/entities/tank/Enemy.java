package com.tankgame.entities.tank;

import com.tankgame.managers.CollisionManager;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.ai.DefaultAI;
import com.tankgame.systems.ai.EnemyAISystem;
import com.tankgame.systems.ai.RoamAI;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;
import com.tankgame.utils.Team;

public class Enemy extends Tank implements Runnable {
    private long lastShotTime = 0;
    public EnemyAISystem AI;

    private volatile boolean isAlive = true;
    private volatile boolean isPaused = false;
    private volatile boolean gameIsRunning = true;

    public enum AIType {
        DEFAULT, ROAM
    }

    public Enemy(double x, double y, int health, String spriteKey, Direction direction, TankColors color, AIType aiType,
            CollisionManager collisionManager) {
        super(x, y, health, spriteKey, direction, color, Team.ENEMY);
        this.speed = 2.0;

        if (aiType == AIType.ROAM) {
            this.AI = new RoamAI(collisionManager, this);
        } else {
            this.AI = new DefaultAI(collisionManager, this);
        }
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void stopGame() {
        this.gameIsRunning = false;
    }

    @Override
    public void run() {
        while (isAlive && gameIsRunning) {

            if (!isPaused) {
                if (AI != null) {
                    AI.update();
                }
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health);
        if (this.health <= 0) {
            isAlive = false;
        }
    }

    public boolean canShoot() {
        return (System.currentTimeMillis() - lastShotTime) > GameConfig.ENEMY_BULLET_COOL_DOWN_MS;
    }

    public void shootBullet() {
        lastShotTime = System.currentTimeMillis();
    }
}
