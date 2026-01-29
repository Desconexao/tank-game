package com.tankgame.managers;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Tank;
import com.tankgame.systems.ProjectileSystem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectileManager {
    private final List<Bullet> activeBullets;
    private final ProjectileSystem projectileSystem;

    public ProjectileManager(ProjectileSystem projectileSystem) {
        this.activeBullets = new ArrayList<>();
        this.projectileSystem = projectileSystem;
    }

    public void update(List<Tank> allTanks) {
        Iterator<Bullet> iterator = activeBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            boolean collided = projectileSystem.update(bullet, allTanks);

            if (collided || bullet.isMarkedForRemoval()) {
                iterator.remove();
            }
        }
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
}
