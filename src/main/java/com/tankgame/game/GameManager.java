package com.tankgame.game;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.tank.Player;
import com.tankgame.entities.tank.Tank;
import com.tankgame.entities.tile.Eagle;
import com.tankgame.managers.AssetManager;
import com.tankgame.managers.CollisionManager;
import com.tankgame.managers.EnemyManager;
import com.tankgame.managers.PowerUpManager;
import com.tankgame.managers.ProjectileManager;
import com.tankgame.managers.StatManager;
import com.tankgame.screens.GameScene;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.InputSystem;
import com.tankgame.systems.MovementSystem;
import com.tankgame.systems.ProjectileSystem;
import com.tankgame.systems.ShootingSystem;

public class GameManager {
    // Managers
    private final EnemyManager enemyManager;
    private final ProjectileManager projectileManager;
    private final AssetManager assetManager;
    private final CollisionManager collisionManager;
    private final PowerUpManager powerUpManager;

    // Systems
    private final MovementSystem movementSystem;
    //private final EnemyAISystem enemyAISystem;
    private final ShootingSystem shootingSystem;
    private final ProjectileSystem projectileSystem;
    private final InputSystem inputSystem;
    private final StatManager statSystem;

    // Refs
    private final GameScene scene;
    private final Player player;
    private final Eagle EagleObjective;

    // State
    private boolean isPaused = false;
    private boolean isRunning = true;
    private int score = 0;
    private int level = 1;
    private int runningTime = 0;
    private int tick = 0;

    public GameManager(GameScene scene, Player player) {
        this.scene = scene;
        this.player = player;
        this.EagleObjective = scene.gridLogic.getEagleObjective();

        this.assetManager = new AssetManager();
        this.collisionManager = new CollisionManager(scene.gridLogic);
        this.statSystem = new StatManager(scene);

        this.movementSystem = new MovementSystem(collisionManager);
        this.enemyManager = new EnemyManager(movementSystem, scene.gridLogic.getEnemySpawnXY());
        //this.enemyAISystem = new RoamAI(movementSystem);
        this.projectileSystem = new ProjectileSystem(collisionManager);
        this.inputSystem = new InputSystem();
        this.projectileManager = new ProjectileManager(projectileSystem);
        this.shootingSystem = new ShootingSystem(projectileManager);
        this.powerUpManager = new PowerUpManager(scene.gridLogic, collisionManager, player);

        scene.getGameGrid().addKeyListener(inputSystem.getKeyboard());
        scene.getGameGrid().setFocusable(true);
        scene.getGameGrid().requestFocusInWindow();

        initializeEnemies();
    }

    public void update() {
        if (inputSystem.checkPauseToggle()) {
            this.togglePause();

            if (isPaused()) {
                inputSystem.resetAllInputs();
            }

        }
        if (isPaused() || !isRunning())
            return;

        inputSystem.processInput(player, movementSystem);

        if (inputSystem.isShootingPressed()) {
            shootingSystem.playerShoot(player);
        }

        enemyManager.updateMovement();

        shootingSystem.enemyShoot(enemyManager.getEnemies());

        powerUpManager.update();

        updateProjectiles();

        increaseTimer();

        statSystem.update(runningTime, score);

        cleanup();

        checkGameConditions();
    }

    public void togglePause() {
        isPaused = !isPaused;
        System.out.println("Game " + (isPaused ? "PAUSED" : "RESUMED"));
    }

    public boolean isGamePaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    private void preloadAssets() {
        // String[] spritesToLoad
        // "player_tan
        // "player_tank_dow
        // "player_tank_lef
        // "player_tank_righ
        // // "enemy_tank_u
        // // "enemy_tank_dow
        // // "enemy_tank_lef
        // // "enemy_tank_righ
        // "bric
        // "blac
        // "bullet_vertica
        // // "powerup_health"
        // };
        //
        // assetManager.preloadSprites(spritesToLoad, GameConfig.TILE_SI
        // GameConfig.TILE_SIZE);
        // assetManager.preloadSprites(new String[] { "bullet_vertical"
        // GameConfig.BULLET_SIZE, GameConfig.BULLET_SIZE);
        // FIX : idk if we should preload assets here or in AssetManager directly...
    }

    private void initializeEnemies() {
        enemyManager.spawnInitialEnemies();
    }

    private void updateProjectiles() {
        projectileManager.update(getAllTanks());
    }

    private List<Tank> getAllTanks() {
        List<Tank> allTanks = new ArrayList<Tank>();
        allTanks.add(player);
        allTanks.addAll(enemyManager.getEnemies());
        return allTanks;
    }

    private void cleanup() {
        enemyManager.removeDeadEnemies();
    }

    private void checkGameConditions() {
        if (player.getHealth() <= 0) {
            gameOver(false);
            return;
        }

        if (EagleObjective.isBroken()){
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
        enemyManager.spawnInitialEnemies();
        System.out.println("Level " + (level - 1) + " complete! Score: " + score);
    }

    private void gameOver(boolean won) {
        isRunning = false;
        if (won) {
            System.out.println("VICTORY! Score: " + score);
        } else {
            System.out.println("GAME OVER! Score: " + score);
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
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

    public void stop() {
        isRunning = false;
    }

    public MovementSystem getMovementSystem() {
        return movementSystem;
    }

    public ProjectileSystem getProjectileSystem() {
        return projectileSystem;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
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

    public void addScore(int points) {
        score += points;
    }

    private void increaseTimer(){
        tick += 1;

        if(tick >= GameConfig.TPS){
            tick = 0;
            runningTime += 1;
        }
    }

    public PowerUpManager getPowerUpManager(){
        return this.powerUpManager;
    }
}
