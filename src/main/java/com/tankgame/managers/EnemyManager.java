package com.tankgame.managers;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.MovementSystem;
import com.tankgame.systems.ai.DefaultAI;
import com.tankgame.systems.ai.RoamAI;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

public class EnemyManager {
    private final List<Enemy> enemies;
    private final MovementSystem movementSystem;
    public EnemyManager(MovementSystem movementSystem) {
        this.enemies = new ArrayList<>();
        this.movementSystem = movementSystem;
    }

    public void spawnInitialEnemies() {
        enemies.add(new Enemy(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE * 5,
                GameConfig.ENEMY_START_HEALTH, "enemy_tank", Direction.DOWN, TankColors.GREEN, DefaultAI.class, movementSystem));

        enemies.add(new Enemy(GameConfig.TILE_SIZE * 9, GameConfig.TILE_SIZE * 1,
                GameConfig.ENEMY_START_HEALTH, "enemy_tank", Direction.DOWN, TankColors.YELLOW, RoamAI.class, movementSystem));

        enemies.add(new Enemy(GameConfig.TILE_SIZE * 10, GameConfig.TILE_SIZE * 8,
                GameConfig.ENEMY_START_HEALTH, "enemy_tank", Direction.DOWN, TankColors.RED, RoamAI.class, movementSystem));
    }

    public void updateMovement() {
        for (Enemy enemy : enemies) {
            enemy.AI.update();
        }
    }

    public void removeDeadEnemies() {
        enemies.removeIf(enemy -> enemy.getHealth() <= 0);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void clear() {
        enemies.clear();
    }
}
