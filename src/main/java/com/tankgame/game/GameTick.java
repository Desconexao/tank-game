package com.tankgame.game;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Player;
import com.tankgame.input.Keyboard;
import com.tankgame.screens.GameScene;
import com.tankgame.settings.Globals;

public class GameTick implements Runnable {
    private final int TPS = Globals.TPS;
    private final long bulletCooldown = Globals.BULLET_COOL_DOWN;

    private boolean running = true;
    private GameScene currScene;
    private Keyboard keyInput;
    private long lastMove = 0;
    private long lastPlayerShot;
    private long now;
    private Player scenePlayer;
    private List<Bullet> projectiles = new ArrayList<>();
    // private long MOVE_DELAY = 150; // in miliseconds

    public GameTick(GameScene currScene) {
        this.currScene = currScene;
        keyInput = new Keyboard();
        currScene.mainWindow.addKeyListener(keyInput);
        scenePlayer = currScene.getPlayer();
    }

    public void run() {
        long nsPerTick = 1_000_000_000L / TPS;
        long last = System.nanoTime();
        lastPlayerShot = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            if (now - last >= nsPerTick) {
                this.tick();
                last += nsPerTick;
            }
        }
    }

    private void tick() {

        checkInputs();

        // Iterate ArrayList
        for (var element : projectiles) {
            // Add bullet to renderQueue for this frame
            if(!currScene.renderer.pushRenderQueue("bullet_vertical", (int) element.getX(), (int) element.getY()))
                throw new IllegalArgumentException("Sprite name not loaded");
            element.update();
        }

        checkProjectileNextColision();

        

        
        currScene.update();
        
        

    }

    public void stop() {
        running = false;
    }

    private void checkInputs() {
        double speed = scenePlayer.getSpeed();
        

        if (keyInput.upPressed) {
            if (canMove(scenePlayer.getX(), scenePlayer.getY() - speed)) {
                scenePlayer.moveUp();
            }
        }
        if (keyInput.downPressed) {
            if (canMove(scenePlayer.getX(), scenePlayer.getY() + speed)) {
                scenePlayer.moveDown();
            }
        }
        if (keyInput.leftPressed) {
            if (canMove(scenePlayer.getX() - speed, scenePlayer.getY())) {
                scenePlayer.moveLeft();
            }
        }
        if (keyInput.rightPressed) {
            if (canMove(scenePlayer.getX() + speed, scenePlayer.getY())) {
                scenePlayer.moveRight();
            }
        }
        if (keyInput.shootPressed && canShoot(lastPlayerShot)){
            addProjectile(scenePlayer.shoot());
            lastPlayerShot = System.nanoTime();
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
        // Default to player size if not specified
        return canMove(nextX, nextY, Globals.TILE_SIZE - 1);
    }

    private boolean canMove(double nextX, double nextY, int size) {
        return !isTileBlocked(nextY, nextX) &&
                !isTileBlocked(nextY, nextX + size) &&
                !isTileBlocked(nextY + size, nextX) &&
                !isTileBlocked(nextY + size, nextX + size);
    }

    private boolean canShoot(long lastShotTime){
        if(now - lastShotTime > bulletCooldown){
            return true;
            }
        return false;
    }

    private boolean addProjectile(Bullet bullet){
        projectiles.add(bullet);
        return true;
        //???
    }

    private void checkProjectileNextColision(){

        projectiles.removeIf(e -> {
            double speed = e.getSpeed();
            // Use bullet size, otherwise bullets hit walls too early
            int size = Globals.BULLET_SIZE; 
            
            // Return TRUE to remove bullet (if !canMove)
            switch (e.getDirection()) {
                case 0: // UP
                    return !canMove(e.getX(), e.getY() - speed, size);
                case 1: // DOWN
                    return !canMove(e.getX(), e.getY() + speed, size);
                case 2: // LEFT
                    return !canMove(e.getX() - speed, e.getY(), size);
                case 3: // RIGHT
                    return !canMove(e.getX() + speed, e.getY(), size);
                default:
                    return false;
            }
        });

    }
}
