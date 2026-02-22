package com.tankgame.game.online;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.managers.ProjectileManager;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;

/**
 * Manages the opponent tank in online games.
 * Only manages a single opponent, no AI (controlled by server).
 */
public class OnlineOpponentManager {
    private Enemy opponent;
    private final ProjectileManager projectileManager;

    private boolean hasState = false;
    private double targetX;
    private double targetY;
    private Direction targetDirection;
    private boolean shooting;
    private long lastOpponentShotTime = 0;

    public OnlineOpponentManager(ProjectileManager projectileManager) {
        this.projectileManager = projectileManager;
    }

    /**
     * Set the opponent tank to manage
     */
    public void setOpponent(Enemy opponent) {
        this.opponent = opponent;
    }

    /**
     * Handle input updates from server
     */
    public void handleInput(double x, double y, Direction facing) {
        if (opponent == null) {
            return;
        }

        this.targetX = x;
        this.targetY = y;
        this.targetDirection = facing;
        this.hasState = true;
    }

    /**
     * Handle shooting updates from server
     */
    public void handleShooting(boolean shooting) {
        if (opponent == null) {
            return;
        }

        this.shooting = shooting;
    }

    /**
     * Update opponent movement based on held buttons (called every frame)
     */
    public void update() {
        if (opponent == null) {
            return;
        }

        if (hasState) {
            opponent.setX(targetX);
            opponent.setY(targetY);
            if (targetDirection != null) {
                opponent.setDirection(targetDirection);
            }
        }

        if (shooting) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastOpponentShotTime >= GameConfig.PLAYER_BULLET_COOL_DOWN_MS) {
                Bullet bullet = opponent.shoot();
                projectileManager.addBullet(bullet);
                lastOpponentShotTime = currentTime;
            }
        }
    }

    public Enemy getOpponent() {
        return opponent;
    }

    public void clear() {
        opponent = null;
        hasState = false;
        shooting = false;
        targetDirection = Direction.UP;
    }
}
