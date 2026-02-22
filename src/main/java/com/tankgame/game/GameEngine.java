package com.tankgame.game;

import com.tankgame.screens.GameScene;
import com.tankgame.settings.GameConfig;

public class GameEngine implements Runnable {
    private final GameManager gameManager;
    private final GameScene scene;
    private Thread gameThread;
    private boolean running = false;

    public GameEngine(GameScene scene, int difficulty) {
        this.scene = scene;
        this.gameManager = new GameManager(scene, scene.getPlayer(), difficulty);
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

    }

    @Override
    public void run() {
        // 1 / 60 frames = 0,016 ms
        int frameDelay = 1000 / GameConfig.TPS;

        while (running) {
            gameManager.update();
            scene.update();
            try {
                Thread.sleep(frameDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("GameEngine stopped");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
