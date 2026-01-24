package com.tankgame.game;

import com.tankgame.entities.tank.Player;
import com.tankgame.input.Keyboard;
import com.tankgame.screens.GameScene;
import com.tankgame.settings.Globals;

public class GameTick implements Runnable {
    private final int TPS = Globals.TPS;
    private boolean running = true;
    private GameScene currScene;
    private Keyboard keyInput;
    private long lastMove = 0;
    // private long MOVE_DELAY = 150; // in miliseconds

    public GameTick(GameScene currScene) {
        this.currScene = currScene;
        keyInput = new Keyboard();
        currScene.mainWindow.addKeyListener(keyInput);
    }

    public void run() {
        long nsPerTick = 1_000_000_000L / TPS;
        long last = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            if (now - last >= nsPerTick) {
                this.tick();
                last += nsPerTick;
            }
        }
    }

    private void tick() {
        checkInputs();
        currScene.update();

    }

    public void stop() {
        running = false;
    }

    private void checkInputs() {
        Player player = currScene.getPlayer();
        double speed = player.getSpeed();

        if (keyInput.upPressed) {
            if (canMove(player.getX(), player.getY() - speed)) {
                player.moveUp();
            }
        }
        if (keyInput.downPressed) {
            if (canMove(player.getX(), player.getY() + speed)) {
                player.moveDown();
            }
        }
        if (keyInput.leftPressed) {
            if (canMove(player.getX() - speed, player.getY())) {
                player.moveLeft();
            }
        }
        if (keyInput.rightPressed) {
            if (canMove(player.getX() + speed, player.getY())) {
                player.moveRight();
            }
        }
    }

    private boolean isTileBlocked(double nextY, double nextX) {
        int gridX = (int) (nextX / Globals.TILE_SIZE);
        int gridY = (int) (nextY / Globals.TILE_SIZE);

        if (gridY < 0 || gridY >= Globals.GRID_HEIGHT || gridX < 0 || gridX >= Globals.GRID_WIDTH) {
            return true;
        }

        char tile = currScene.gridLogic.getGridMatrix()[gridY][gridX];

        return tile == 'X';
    }

    private boolean canMove(double nextX, double nextY) {
        int size = Globals.TILE_SIZE - 1;

        return !isTileBlocked(nextY, nextX) &&
                !isTileBlocked(nextY, nextX + size) &&
                !isTileBlocked(nextY + size, nextX) &&
                !isTileBlocked(nextY + size, nextX + size);
    }
}
