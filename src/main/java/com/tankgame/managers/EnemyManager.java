package com.tankgame.managers;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.ai.EnemyAISystem;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

public class EnemyManager {
    private final List<Enemy> enemies;

    public EnemyManager() {
        this.enemies = new ArrayList<>();
    }

    public void spawnInitialEnemies() {
        enemies.add(new Enemy(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE * 5,
                GameConfig.ENEMY_START_HEALTH, "enemy_tank", Direction.DOWN, TankColors.GREEN));

        enemies.add(new Enemy(GameConfig.TILE_SIZE * 9, GameConfig.TILE_SIZE * 1,
                GameConfig.ENEMY_START_HEALTH, "enemy_tank", Direction.DOWN, TankColors.YELLOW));

        enemies.add(new Enemy(GameConfig.TILE_SIZE * 10, GameConfig.TILE_SIZE * 8,
                GameConfig.ENEMY_START_HEALTH, "enemy_tank", Direction.DOWN, TankColors.RED));
    }

    public void updateMovement(EnemyAISystem aiSystem) {
        for (Enemy enemy : enemies) {
            aiSystem.update(enemy);
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
