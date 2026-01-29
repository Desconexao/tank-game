package com.tankgame.game;

import com.tankgame.screens.GameScene;
import com.tankgame.settings.GameConfig;

public class GameEngine implements Runnable {
    private final GameManager gameManager;
    private final GameScene scene;
    private Thread gameThread;
    private boolean running = false;

    public GameEngine(GameScene scene) {
        this.scene = scene;
        this.gameManager = new GameManager(scene, scene.getPlayer());
    }

    public void start() {
        if (running)
            return;

        running = true;
        gameThread = new Thread(this, "GameEngine-Thread");
        gameThread.start();
        System.out.println("GameEngine Thread started");
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

        while (running) {
            long now = System.nanoTime();

            if (now - lastTime >= nsPerTick) {
                gameLoop();
                lastTime += nsPerTick;
                frames++;
            }
            scene.update();

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                System.out.println("FPS: " + frames + " | Enemies: " +
                        gameManager.getEnemyManager().getEnemies().size() +
                        " | Bullets: " + gameManager.getProjectileManager().getBulletCount());
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
        System.out.println("GameEngine stopped");
    }

    private void gameLoop() {
        gameManager.update();
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
