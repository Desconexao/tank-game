package com.tankgame.game;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.entities.tank.Tank;
import com.tankgame.entities.tile.Eagle;
import com.tankgame.input.KeyboardInput;
import com.tankgame.managers.CollisionManager;
import com.tankgame.managers.EnemyManager;
import com.tankgame.managers.ProjectileManager;
import com.tankgame.managers.StatManager;
import com.tankgame.screens.GameScene;
import com.tankgame.settings.GameConfig;
import com.tankgame.managers.RankingManager;
import com.tankgame.utils.Direction;

public class GameManager {
    private final EnemyManager enemyManager;
    private final ProjectileManager projectileManager;
    private final CollisionManager collisionManager;
    private final StatManager statSystem;
    private final KeyboardInput keyboard;

    private final GameScene scene;
    private final Player player;
    private final Eagle EagleObjective;

    private boolean isPaused = false;
    private boolean isRunning = true;
    private int score = 0;
    private int level = 1;
    private int runningTime = 0;
    private int tick = 0;
    private long lastPlayerShotTime = 0;
    private String playerName = "player_1";
    private RankingManager rankingManager = new RankingManager();
    private int difficulty;

    public GameManager(GameScene scene, Player player, int difficulty) {
        this.difficulty = difficulty;
        this.scene = scene;
        this.player = player;
        this.EagleObjective = scene.gridLogic.getEagleObjective();

        this.collisionManager = new CollisionManager(scene.gridLogic);
        this.statSystem = new StatManager(scene);
        this.keyboard = new KeyboardInput();

        this.projectileManager = new ProjectileManager();
        this.enemyManager = new EnemyManager(collisionManager, scene.gridLogic.getEnemySpawnXY());

        scene.getGameGrid().addKeyListener(keyboard);
        scene.getGameGrid().setFocusable(true);
        scene.getGameGrid().requestFocusInWindow();

        initializeEnemies();
    }

    public void update() {
        if (keyboard.checkPauseToggle()) {
            this.togglePause();
            if (isPaused())
                keyboard.resetAllInputs();
        }

        if (isPaused() || !isRunning())
            return;

        handlePlayerInput();
        handlePlayerShooting();

        handleEnemyShooting();

        updateProjectiles();

        increaseTimer();
        statSystem.update(runningTime, score);
        cleanup();
        checkGameConditions();
    }

    private void handlePlayerInput() {
        double speed = player.getSpeed();
        double newX = player.getX();
        double newY = player.getY();
        Direction newDir = player.getDirection();
        boolean moved = false;

        if (keyboard.upPressed) {
            newY -= speed;
            newDir = Direction.UP;
            moved = true;
        } else if (keyboard.downPressed) {
            newY += speed;
            newDir = Direction.DOWN;
            moved = true;
        } else if (keyboard.leftPressed) {
            newX -= speed;
            newDir = Direction.LEFT;
            moved = true;
        } else if (keyboard.rightPressed) {
            newX += speed;
            newDir = Direction.RIGHT;
            moved = true;
        }

        if (moved && collisionManager.canMove(newX, newY, GameConfig.TANK_SIZE)) {
            player.setX(newX);
            player.setY(newY);
        }
        if (moved)
            player.setDirection(newDir);
    }

    private void handlePlayerShooting() {
        if (keyboard.shootPressed) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayerShotTime >= GameConfig.PLAYER_BULLET_COOL_DOWN_MS) {
                projectileManager.addBullet(player.shoot());
                lastPlayerShotTime = currentTime;
            }
        }
    }

    private void handleEnemyShooting() {
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (enemy.canShoot() && new java.util.Random().nextInt(100) < 5) {
                projectileManager.addBullet(enemy.shoot());
                enemy.shootBullet();
            }
        }
    }

    private void updateProjectiles() {
        List<Bullet> bullets = projectileManager.getActiveBullets();
        List<Bullet> toRemove = new ArrayList<>();
        List<Tank> allTanks = getAllTanks();

        for (Bullet bullet : bullets) {

            if (collisionManager.checkProjectileCollision(bullet, allTanks) || bullet.isMarkedForRemoval()) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }

    private List<Tank> getAllTanks() {
        List<Tank> allTanks = new ArrayList<>();
        allTanks.add(player);
        allTanks.addAll(enemyManager.getEnemies());
        return allTanks;
    }

    private void cleanup() {
        enemyManager.removeDeadEnemies();
    }

    private void checkGameConditions() {
        if (player.getHealth() <= 0 || EagleObjective.isBroken()) {
            gameOver(false);
            return;
        }

        if (enemyManager.getEnemies().isEmpty()) {
            levelComplete();
        }
    }

    private void levelComplete() {
        score += 100 * level;
        level++;
        initializeEnemies();
        System.out.println("Level " + (level - 1) + " complete! Score: " + score);
    }

    private void gameOver(boolean won) {
        isRunning = false;
        enemyManager.stopAllEnemies();
        projectileManager.stopAllBullets();

        rankingManager.addScore(playerName, score);

        keyboard.resetAllInputs();

        if (won) {
            System.out.println("VICTORY! Score: " + score);
        } else {
            System.out.println("GAME OVER! Score: " + score);
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
        enemyManager.pauseAllEnemies(isPaused);
        projectileManager.pauseAllBullets(isPaused);
        System.out.println("Game " + (isPaused ? "PAUSED" : "RESUMED"));
    }

    private void increaseTimer() {

        if (tick >= GameConfig.TPS) {
            tick = 0;
            runningTime += 1;
        }
    }

    private void initializeEnemies() {
        int baseAmount = switch (difficulty) {
            case 0 -> 3;
            case 1 -> 5;
            case 2 -> 7;
            default -> 5;
        };

        int amountToSpawn = (int) (baseAmount * Math.pow(1.3, level - 1));

        enemyManager.spawnInitialEnemies(amountToSpawn);
    }

    public boolean isGamePaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public KeyboardInput getKeyboard() {
        return keyboard;
    }

    public void addScore(int points) {
        score += points;
    }

    public void stop() {
        isRunning = false;
    }

    public void restart() {
        score = 0;
        level = 1;
        isRunning = true;
        isPaused = false;
        player.setHealth(GameConfig.PLAYER_START_HEALTH);
        player.setX(GameConfig.PLAYER_START_X);
        player.setY(GameConfig.PLAYER_START_Y);
        enemyManager.clear();
        projectileManager.clearBullets();
        initializeEnemies();
    }

}
