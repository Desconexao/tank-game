package com.tankgame.game.online;

import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Tank;
import com.tankgame.managers.CollisionManager;
import com.tankgame.systems.ProjectileSystem;

public class OnlineProjectileUpdater {
    private final ProjectileSystem projectileSystem;

    public OnlineProjectileUpdater(CollisionManager collisionManager) {
        this.projectileSystem = new ProjectileSystem(collisionManager);
    }

    public void update(List<Bullet> bullets, List<Tank> targets) {
        if (bullets == null || bullets.isEmpty()) {
            return;
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            boolean remove = projectileSystem.update(bullet, targets) || bullet.isMarkedForRemoval();
            if (remove) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }
}
