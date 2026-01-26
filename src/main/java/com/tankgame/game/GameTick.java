package com.tankgame.game;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.entities.tank.Tank;
import com.tankgame.input.Keyboard;
import com.tankgame.screens.GameScene;
import com.tankgame.settings.Globals;
import com.tankgame.utils.Direction;
import java.util.ArrayList;
import java.util.List;

public class GameTick implements Runnable {
    private final GameScene scene;
    private final Keyboard keys;
    private final List<Bullet> bullets = new ArrayList<>();
    private long lastShot;

    public GameTick(GameScene scene) {
        this.scene = scene;
        this.keys = new Keyboard();
        this.scene.mainWindow.addKeyListener(keys);
    }

    @Override
    public void run() {
        long nsPerTick = 1_000_000_000L / Globals.TPS;
        long last = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            if (now - last >= nsPerTick) {
                update();
                last += nsPerTick;
            }
        }
    }

    private void update() {
        handlePlayerInput();
        updateProjectiles();
        updateEnemies();
        scene.getEnemies().removeIf(enemy -> enemy.getHealth() <= 0);
        scene.update();
    }

    private void updateProjectiles() {
        bullets.removeIf(b -> {
            double currentX = b.getX();
            double currentY = b.getY();

            double nextX = currentX;
            double nextY = currentY;

            double s = b.getSpeed();
            int bSize = Globals.BULLET_SIZE;

            switch (b.getDirection()) {
                case UP -> nextY -= s;
                case DOWN -> nextY += s;
                case LEFT -> nextX -= s;
                case RIGHT -> nextX += s;
            }

            double centerX = nextX + bSize / 2.0;
            double centerY = nextY + bSize / 2.0;

            boolean hitWall = isBlocked(centerX, centerY);
            if (hitWall) {
                scene.gridLogic.removeBlock(
                        nextX + bSize / 2.0,
                        nextY + bSize / 2.0);
                return true;
            }

            b.update();
            for (Enemy e : scene.getEnemies()) {
                if (checkHit(b, e)) {
                    e.setHealth(e.getHealth() - 1);
                    return true;
                }
            }

            if (b.getX() < 0 || b.getX() > Globals.GRID_WIDTH * Globals.TILE_SIZE ||
                    b.getY() < 0 || b.getY() > Globals.GRID_HEIGHT * Globals.TILE_SIZE) {
                return true;
            }

            scene.renderer.pushRenderQueue(b.getSpriteKey(), (int) b.getX(), (int) b.getY());
            return false;
        });
    }

    private boolean checkHit(Bullet b, Tank t) {
        return b.getX() < t.getX() + Globals.TILE_SIZE &&
                b.getX() + Globals.BULLET_SIZE > t.getX() &&
                b.getY() < t.getY() + Globals.TILE_SIZE &&
                b.getY() + Globals.BULLET_SIZE > t.getY();
    }

    private void handlePlayerInput() {
        Player p = scene.getPlayer();
        double s = p.getSpeed();
        if (keys.upPressed && canMove(p.getX(), p.getY() - s)) {
            p.moveUp();
            p.setDirection(Direction.UP);
        } else if (keys.downPressed && canMove(p.getX(), p.getY() + s)) {
            p.moveDown();
            p.setDirection(Direction.DOWN);
        } else if (keys.leftPressed && canMove(p.getX() - s, p.getY())) {
            p.moveLeft();
            p.setDirection(Direction.LEFT);
        } else if (keys.rightPressed && canMove(p.getX() + s, p.getY())) {
            p.moveRight();
            p.setDirection(Direction.RIGHT);
        }

        if (keys.shootPressed && (System.nanoTime() - lastShot > Globals.BULLET_COOL_DOWN)) {
            bullets.add(p.shoot());
            lastShot = System.nanoTime();
        }
    }

    private void updateEnemies() {
        for (Enemy e : scene.getEnemies()) {
            double s = e.getSpeed();
            boolean moved = false;

            switch (e.getDirection()) {
                case UP -> {
                    if (canMove(e.getX(), e.getY() - s)) {
                        e.moveUp();
                        moved = true;
                    }
                }
                case DOWN -> {
                    if (canMove(e.getX(), e.getY() + s)) {
                        e.moveDown();
                        moved = true;
                    }
                }
                case LEFT -> {
                    if (canMove(e.getX() - s, e.getY())) {
                        e.moveLeft();
                        moved = true;
                    }
                }
                case RIGHT -> {
                    if (canMove(e.getX() + s, e.getY())) {
                        e.moveRight();
                        moved = true;
                    }
                }
            }

            // Se ele não conseguiu se mover (bateu na parede), ele sorteia uma nova direção
            if (!moved) {
                Direction[] directions = Direction.values();
                e.setDirection(directions[new java.util.Random().nextInt(directions.length)]);
            }
        }
    }

    private boolean canMove(double x, double y) {
        return canMove(x, y, Globals.TILE_SIZE - 2);
    }

    private boolean canMove(double x, double y, int size) {
        return !isBlocked(x, y) && !isBlocked(x + size, y) && !isBlocked(x, y + size) && !isBlocked(x + size, y + size);
    }

    private boolean isBlocked(double x, double y) {
        int gx = (int) (x / Globals.TILE_SIZE);
        int gy = (int) (y / Globals.TILE_SIZE);
        if (gx < 0 || gx >= Globals.GRID_WIDTH || gy < 0 || gy >= Globals.GRID_HEIGHT)
            return true;
        return scene.gridLogic.getGridMatrix()[gy][gx] == 'X';
    }
}
