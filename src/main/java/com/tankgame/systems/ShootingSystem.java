package com.tankgame.systems;

import com.tankgame.entities.tank.Tank;
import com.tankgame.entities.tank.Player;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.managers.ProjectileManager;
import com.tankgame.settings.GameConfig;
import java.util.List;
import java.util.Random;

public class ShootingSystem {
    private final ProjectileManager projectileManager;
    private final Random random = new Random();
    private long lastPlayerShotTime = 0;

    public ShootingSystem(ProjectileManager projectileManager) {
        this.projectileManager = projectileManager;
    }

    public void playerShoot(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPlayerShotTime < GameConfig.PLAYER_BULLET_COOL_DOWN_MS) {
            return;
        }
 
        Bullet bullet = player.shoot();
        projectileManager.addBullet(bullet);
        lastPlayerShotTime = currentTime;
    }

    public void enemyShoot(Iterable<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.canShoot() && random.nextInt(100) < 10) {
                Bullet bullet = enemy.shoot();
                projectileManager.addBullet(bullet);
                enemy.shootBullet();
            }
        }
    }

    public boolean canPlayerShoot() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastPlayerShotTime) >= GameConfig.PLAYER_BULLET_COOL_DOWN_MS;
    }
}
