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
public class OnlineGameEngine implements Runnable {
    private final OnlineGameScene scene;
    private final WebSocketClient webSocketClient;
    private Thread gameThread;
    private boolean running = false;
    private boolean gameStarted = true;

    private double lastSentX = -1;
    private double lastSentY = -1;
    private com.tankgame.utils.Direction lastSentFacing = null;
    private boolean lastSentShooting = false;

    private final CollisionManager collisionManager;
    private final ProjectileManager projectileManager;
    private final OnlineOpponentManager opponentManager;
    private long lastPlayerShotTime = 0;

    public OnlineGameEngine(OnlineGameScene scene, WebSocketClient webSocketClient) {
        this.scene = scene;
        this.webSocketClient = webSocketClient;

        GameGrid gameGrid = scene.getWrappedGrid();
        this.collisionManager = new CollisionManager(gameGrid);
        this.projectileManager = new ProjectileManager();
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
            public void onEnemyInput(double x, double y, com.tankgame.utils.Direction facing) {
                opponentManager.handleInput(x, y, facing);
            }

            @Override
            public void onEnemyShooting(boolean shooting) {
                opponentManager.handleShooting(shooting);
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

        if (inputHandler.upPressed) {
            double newY = player.getY() - GameConfig.PLAYER_SPEED;
            if (collisionManager.canMove(player.getX(), newY, GameConfig.TANK_SIZE)) {
                player.setY(newY);
                player.setDirection(com.tankgame.utils.Direction.UP);
            }
        } else if (inputHandler.downPressed) {
            double newY = player.getY() + GameConfig.PLAYER_SPEED;
            if (collisionManager.canMove(player.getX(), newY, GameConfig.TANK_SIZE)) {
                player.setY(newY);
                player.setDirection(com.tankgame.utils.Direction.DOWN);
            }
        } else if (inputHandler.leftPressed) {
            double newX = player.getX() - GameConfig.PLAYER_SPEED;
            if (collisionManager.canMove(newX, player.getY(), GameConfig.TANK_SIZE)) {
                player.setX(newX);
                player.setDirection(com.tankgame.utils.Direction.LEFT);
            }
        } else if (inputHandler.rightPressed) {
            double newX = player.getX() + GameConfig.PLAYER_SPEED;
            if (collisionManager.canMove(newX, player.getY(), GameConfig.TANK_SIZE)) {
                player.setX(newX);
                player.setDirection(com.tankgame.utils.Direction.RIGHT);
            }
        }

        opponentManager.update();

        if (inputHandler.shootPressed) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayerShotTime >= player.getBulletCooldown()) {
                projectileManager.addBullet(player.shoot());
                lastPlayerShotTime = currentTime;
            }
        }

        if (webSocketClient != null && webSocketClient.isConnected()) {
            boolean posChanged = Math.abs(player.getX() - lastSentX) > 0.001 ||
                                 Math.abs(player.getY() - lastSentY) > 0.001 ||
                                 player.getDirection() != lastSentFacing;

            if (posChanged) {
                webSocketClient.sendInteraction(player.getX(), player.getY(), player.getDirection());
                lastSentX = player.getX();
                lastSentY = player.getY();
                lastSentFacing = player.getDirection();
            }

            if (inputHandler.shootPressed != lastSentShooting) {
                webSocketClient.sendShooting(inputHandler.shootPressed);
                lastSentShooting = inputHandler.shootPressed;
            }
        }

        if (inputHandler.pausePressed) {
            scene.setPaused(!scene.isPaused());
            inputHandler.resetPause();
        }

        updateProjectiles();

        if (player.getHealth() <= 0) {
            scene.setGameRunning(false);
        }

        if (opponent != null && opponent.getHealth() <= 0) {
            scene.setGameRunning(false);
        }
    }

    private void updateProjectiles() {
        List<com.tankgame.entities.projectile.Bullet> bullets = projectileManager.getActiveBullets();
        List<com.tankgame.entities.projectile.Bullet> toRemove = new ArrayList<>();
        List<com.tankgame.entities.tank.Tank> allTanks = new ArrayList<>();

        allTanks.add(scene.getPlayer());
        if (scene.getOpponent() != null) {
            allTanks.add(scene.getOpponent());
        }

        for (com.tankgame.entities.projectile.Bullet bullet : bullets) {
            bullet.update();
            if (collisionManager.checkProjectileCollision(bullet, allTanks) || bullet.isMarkedForRemoval()) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public OnlineOpponentManager getOpponentManager() {
        return opponentManager;
    }
}
