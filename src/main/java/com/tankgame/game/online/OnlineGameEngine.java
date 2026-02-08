package com.tankgame.game.online;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.game.GameGrid;
import com.tankgame.managers.CollisionManager;
import com.tankgame.managers.ProjectileManager;
import com.tankgame.screens.online.OnlineGameScene;
import com.tankgame.settings.GameConfig;
import com.tankgame.systems.MovementSystem;
import com.tankgame.systems.ProjectileSystem;
import com.tankgame.systems.ShootingSystem;

/**
 * Game engine for online multiplayer games.
 * Manages game loop and systems similar to GameEngine but for online gameplay.
 */
public class OnlineGameEngine implements Runnable {
    private final OnlineGameScene scene;
    private final WebSocketClient webSocketClient;
    private Thread gameThread;
    private boolean running = false;
    private boolean gameStarted = true; // Start immediately for testing/local play

    // Systems
    private final MovementSystem movementSystem;
    private final ShootingSystem shootingSystem;
    private final ProjectileSystem projectileSystem;
    private final CollisionManager collisionManager;
    private final ProjectileManager projectileManager;
    private final OnlineOpponentManager opponentManager;

    public OnlineGameEngine(OnlineGameScene scene, WebSocketClient webSocketClient) {
        this.scene = scene;
        this.webSocketClient = webSocketClient;

        // Get the persistent GameGrid from scene to preserve block breaking
        GameGrid gameGrid = scene.getWrappedGrid();
        this.collisionManager = new CollisionManager(gameGrid);

        // Initialize systems first
        this.movementSystem = new MovementSystem(collisionManager);
        this.projectileSystem = new ProjectileSystem(collisionManager);
        
        // Initialize managers with required systems
        this.projectileManager = new ProjectileManager(projectileSystem);
        this.shootingSystem = new ShootingSystem(projectileManager);
        this.opponentManager = new OnlineOpponentManager(projectileManager);

        // Setup message handler for opponent actions
        webSocketClient.setMessageHandler(new WebSocketClient.MessageHandler() {
            @Override
            public void onLobbyCreated(String id, int size, int capacity, int playerNumber) {
            }

            @Override
            public void onLobbyJoined(String id, int size, int capacity, int playerNumber) {
            }

            @Override
            public void onReady() {
            }

            @Override
            public void onGameStarted() {
                gameStarted = true;
            }

            @Override
            public void onEnemyState(double x, double y, com.tankgame.utils.Direction facing, boolean shooting) {
                opponentManager.handleState(x, y, facing, shooting);
            }

            @Override
            public void onError(String message) {
                System.err.println("Game error: " + message);
            }
        });
    }

    public void start() {
        if (running)
            return;

        running = true;
        gameThread = new Thread(this, "OnlineGameEngine-Thread");
        gameThread.start();
        System.out.println("OnlineGameEngine Thread started");
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        long nsPerTick = 1_000_000_000L / GameConfig.TPS;
        long lastTime = System.nanoTime();
        int frames = 0;
        long lastTimer = System.currentTimeMillis();

        while (running && gameStarted) {
            long now = System.nanoTime();

            if (now - lastTime >= nsPerTick) {
                gameLoop();
                lastTime += nsPerTick;
                frames++;
            }
            scene.update();

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                System.out.println("FPS: " + frames + " | Projectiles: " + projectileManager.getBulletCount());
                frames = 0;
                lastTimer += 1000;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("OnlineGameEngine stopped");
    }

    private void gameLoop() {
        if (scene.isPaused()) {
            return;
        }

        Player player = scene.getPlayer();
        Enemy opponent = scene.getOpponent();
        OnlinePlayerInputHandler inputHandler = scene.getInputHandler();

        // Handle player movement - set direction only on successful move
        if (inputHandler.upPressed) {
            if (movementSystem.tryMove(player, player.getX(), player.getY() - GameConfig.PLAYER_SPEED)) {
                player.setDirection(com.tankgame.utils.Direction.UP);
            }
        } else if (inputHandler.downPressed) {
            if (movementSystem.tryMove(player, player.getX(), player.getY() + GameConfig.PLAYER_SPEED)) {
                player.setDirection(com.tankgame.utils.Direction.DOWN);
            }
        } else if (inputHandler.leftPressed) {
            if (movementSystem.tryMove(player, player.getX() - GameConfig.PLAYER_SPEED, player.getY())) {
                player.setDirection(com.tankgame.utils.Direction.LEFT);
            }
        } else if (inputHandler.rightPressed) {
            if (movementSystem.tryMove(player, player.getX() + GameConfig.PLAYER_SPEED, player.getY())) {
                player.setDirection(com.tankgame.utils.Direction.RIGHT);
            }
        }

        // Update opponent movement based on button states
        opponentManager.update();

        // Handle player shooting - separate from movement
        if (inputHandler.shootPressed) {
            shootingSystem.playerShoot(player);
        }

        if (webSocketClient != null && webSocketClient.isConnected()) {
            webSocketClient.sendPlayerState(player.getX(), player.getY(), player.getDirection(), inputHandler.shootPressed);
        }

        // Handle pause
        if (inputHandler.pausePressed) {
            scene.setPaused(!scene.isPaused());
            inputHandler.resetPause();
        }

        // Update projectiles
        for (com.tankgame.entities.projectile.Bullet bullet : projectileManager.getActiveBullets()) {
            List<com.tankgame.entities.tank.Tank> targets = new ArrayList<>();
            targets.add(player);
            if (opponent != null) {
                targets.add(opponent);
            }
            projectileSystem.update(bullet, targets);
        }
        List<com.tankgame.entities.tank.Tank> allTanks = new ArrayList<>();
        allTanks.add(player);
        if (opponent != null) {
            allTanks.add(opponent);
        }
        projectileManager.update(allTanks);

        // Check collisions
        List<Enemy> enemies = new ArrayList<>();
        if (opponent != null) {
            enemies.add(opponent);
        }

        // Game over if player dies
        if (player.getHealth() <= 0) {
            scene.setGameRunning(false);
        }

        // Game over if opponent dies
        if (opponent != null && opponent.getHealth() <= 0) {
            scene.setGameRunning(false);
        }
    }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public OnlineOpponentManager getOpponentManager() {
        return opponentManager;
    }
}
