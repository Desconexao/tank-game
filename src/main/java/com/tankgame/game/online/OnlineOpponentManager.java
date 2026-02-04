package com.tankgame.game.online;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.managers.ProjectileManager;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.MovementSystem;
import com.tankgame.utils.Direction;

/**
 * Manages the opponent tank in online games.
 * Only manages a single opponent, no AI (controlled by server).
 */
public class OnlineOpponentManager {
    private Enemy opponent;
    private final MovementSystem movementSystem;
    private final ProjectileManager projectileManager;
    
    // Track opponent button states
    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean shootPressed = false;

    public OnlineOpponentManager(MovementSystem movementSystem, ProjectileManager projectileManager) {
        this.movementSystem = movementSystem;
        this.projectileManager = projectileManager;
    }

    /**
     * Set the opponent tank to manage
     */
    public void setOpponent(Enemy opponent) {
        this.opponent = opponent;
    }

    /**
     * Handle button press/release from server
     */
    public void handleButtonInput(String button, String state) {
        if (opponent == null) {
            System.err.println("[OPPONENT] Cannot handle input - opponent is null");
            return;
        }
        
        boolean pressed = "pressed".equals(state);
        System.out.println("[OPPONENT] Button input: " + button + " " + state);
        
        switch (button) {
            case "up":
                upPressed = pressed;
                break;
            case "down":
                downPressed = pressed;
                break;
            case "left":
                leftPressed = pressed;
                break;
            case "right":
                rightPressed = pressed;
                break;
            case "shoot":
                shootPressed = pressed;
                break;
        }
    }

    /**
     * Update opponent movement based on held buttons (called every frame)
     */
    public void update() {
        if (opponent == null) return;
        
        // Apply movement based on held buttons - use else-if for exclusive directions
        if (upPressed) {
            if (movementSystem.tryMove(opponent, opponent.getX(), opponent.getY() - GameConfig.ENEMY_SPEED)) {
                opponent.setDirection(Direction.UP);
            }
        } else if (downPressed) {
            if (movementSystem.tryMove(opponent, opponent.getX(), opponent.getY() + GameConfig.ENEMY_SPEED)) {
                opponent.setDirection(Direction.DOWN);
            }
        } else if (leftPressed) {
            if (movementSystem.tryMove(opponent, opponent.getX() - GameConfig.ENEMY_SPEED, opponent.getY())) {
                opponent.setDirection(Direction.LEFT);
            }
        } else if (rightPressed) {
            if (movementSystem.tryMove(opponent, opponent.getX() + GameConfig.ENEMY_SPEED, opponent.getY())) {
                opponent.setDirection(Direction.RIGHT);
            }
        }
        
        // Handle shooting - separate from movement
        if (shootPressed && opponent.canShoot()) {
            Bullet bullet = opponent.shoot();
            projectileManager.addBullet(bullet);
            opponent.shootBullet();
        }
    }

    public Enemy getOpponent() {
        return opponent;
    }

    public void clear() {
        opponent = null;
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        shootPressed = false;
    }
}
