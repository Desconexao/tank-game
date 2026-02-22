package com.tankgame.managers;

import com.tankgame.entities.projectile.Bullet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProjectileManager {
    private final List<Bullet> activeBullets;

    public ProjectileManager() {
        this.activeBullets = new CopyOnWriteArrayList<>();
    }

    public void addBullet(Bullet bullet) {
        activeBullets.add(bullet);
    }

    public void clearBullets() {
        activeBullets.clear();
    }

    public List<Bullet> getActiveBullets() {
        return activeBullets;
    }

    public int getBulletCount() {
        return activeBullets.size();
    }

    public void pauseAllBullets(boolean paused) {
        for (Bullet bullet : activeBullets) {
            bullet.setPaused(paused);
        }
    }

    public void stopAllBullets() {
        for (Bullet bullet : activeBullets) {
            bullet.stopGame();
        }
    }
}
