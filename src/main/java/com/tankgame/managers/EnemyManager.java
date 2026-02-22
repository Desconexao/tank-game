package com.tankgame.managers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

public class EnemyManager {
    private final List<Enemy> enemies;
    protected List<List<Integer>> enemySpawnsXY;
    private final CollisionManager collisionManager;
    private final Random random = new Random();

    public EnemyManager(CollisionManager collisionManager, List<List<Integer>> enemySpawnsXY) {
        this.enemies = new CopyOnWriteArrayList<>();
        this.collisionManager = collisionManager;
        this.enemySpawnsXY = enemySpawnsXY;
    }

    public void spawnInitialEnemies(int amount) {
        TankColors[] colors = { TankColors.GREEN, TankColors.YELLOW, TankColors.RED };
        Enemy.AIType[] aiTypes = { Enemy.AIType.DEFAULT, Enemy.AIType.ROAM };

        for (int i = 0; i < amount; i++) {
            int[] spawn = getRandomSpawn();

            TankColors randomColor = colors[random.nextInt(colors.length)];
            Enemy.AIType randomAI = aiTypes[random.nextInt(aiTypes.length)];

            Enemy newEnemy = new Enemy(
                    spawn[0], spawn[1],
                    GameConfig.ENEMY_START_HEALTH,
                    "enemy_tank",
                    Direction.DOWN,
                    randomColor,
                    randomAI,
                    collisionManager);

            enemies.add(newEnemy);

            new Thread(newEnemy).start();
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

    private int[] getRandomSpawn() {
        if (enemySpawnsXY == null || enemySpawnsXY.isEmpty())
            return new int[] { 0, 0 };
        var coord = enemySpawnsXY.get(random.nextInt(enemySpawnsXY.size()));
        return new int[] { coord.get(0), coord.get(1) };
    }

    public void pauseAllEnemies(boolean paused) {
        for (Enemy enemy : enemies) {
            enemy.setPaused(paused);
        }
    }

    public void stopAllEnemies() {
        for (Enemy enemy : enemies) {
            enemy.stopGame();
        }
    }
}
