package com.tankgame.systems;

import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Tank;
import com.tankgame.managers.CollisionManager;
import java.util.List;

public class ProjectileSystem {
    private CollisionManager collisionManager;

    public ProjectileSystem(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    public boolean update(Bullet bullet, List<Tank> targets) {
        bullet.update();

        boolean collided = collisionManager.checkProjectileCollision(bullet, targets);
        if (collided) {
            bullet.markForRemoval();
            return true;
        }
        return false;
    }

}
