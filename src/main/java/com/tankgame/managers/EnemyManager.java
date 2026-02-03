package com.tankgame.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.MovementSystem;
import com.tankgame.systems.ai.DefaultAI;
import com.tankgame.systems.ai.RoamAI;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

public class EnemyManager {
    private final List<Enemy> enemies;
    protected List<List<Integer>> enemySpawnsXY;
    private final MovementSystem movementSystem;
    public EnemyManager(MovementSystem movementSystem, List<List<Integer>> enemySpawnsXY) {
        this.enemies = new ArrayList<>();
        this.movementSystem = movementSystem;
        this.enemySpawnsXY = enemySpawnsXY;
    }

    public void spawnInitialEnemies() {
        int[] spawn;
        int x, y;

        spawn = getRandomSpawn();
        x = spawn[0]; y = spawn[1];

        enemies.add(new Enemy(x, y,
                GameConfig.ENEMY_START_HEALTH,
                "enemy_tank",
                Direction.DOWN,
                TankColors.GREEN,
                DefaultAI.class,
                movementSystem));

        spawn = getRandomSpawn();
        x = spawn[0]; y = spawn[1];

        enemies.add(new Enemy(x, y,
            GameConfig.ENEMY_START_HEALTH,
            "enemy_tank",
            Direction.DOWN,
            TankColors.YELLOW,
            RoamAI.class,
            movementSystem));

        spawn = getRandomSpawn();
        x = spawn[0]; y = spawn[1];

        enemies.add(new Enemy(x, y,
            GameConfig.ENEMY_START_HEALTH,
            "enemy_tank",
            Direction.DOWN,
            TankColors.RED,
            RoamAI.class,
            movementSystem));
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

    private int[] getRandomSpawn(){
        var coord = enemySpawnsXY.get(new Random().nextInt(enemySpawnsXY.size()));

        return new int[] {coord.get(0), coord.get(1)};
    }
}
