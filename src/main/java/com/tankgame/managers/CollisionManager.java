package com.tankgame.managers;

import java.util.List;

import com.tankgame.entities.collectible.PowerUp;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Player;
import com.tankgame.entities.tank.Tank;
import com.tankgame.entities.tile.Tile;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.GameConfig;

public class CollisionManager {
    private GameGrid grid;

    public CollisionManager(GameGrid grid) {
        this.grid = grid;
    }

    public boolean canMove(double x, double y, int size) {
        if (x < 0 || y < 0 ||
                x + size > GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE ||
                y + size > GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE) {
            return false;
        }

        return !isBlocked(x, y) &&
                !isBlocked(x + size - 1, y) &&
                !isBlocked(x, y + size - 1) &&
                !isBlocked(x + size - 1, y + size - 1);
    }

    private boolean isBlocked(double x, double y) {
        int gx = (int) (x / GameConfig.TILE_SIZE);
        int gy = (int) (y / GameConfig.TILE_SIZE);

        if (gx < 0 || gx >= GameConfig.GRID_WIDTH || gy < 0 || gy >= GameConfig.GRID_HEIGHT)
            return true;

        Tile tile = grid.getGridTiles()[gy][gx];

        return tile.isCollidable();

    }

    public boolean checkProjectileCollision(Bullet bullet, List<Tank> tanks) {
        double centerX = bullet.getX() + GameConfig.BULLET_SIZE / 2.0;
        double centerY = bullet.getY() + GameConfig.BULLET_SIZE / 2.0;
        int bulletDamage = bullet.getOwner().getBulletDamage();

        int gridX = (int) (centerX / GameConfig.TILE_SIZE);
        int gridY = (int) (centerY / GameConfig.TILE_SIZE);

        if (gridX >= 0 && gridX < GameConfig.GRID_WIDTH && gridY >= 0 && gridY < GameConfig.GRID_HEIGHT) {
            Tile tile = grid.getGridTiles()[gridY][gridX];

            if (tile.doesBlockProjectiles()) {
                grid.damageBlock(centerX, centerY, bulletDamage);
                return true;
            }
        }

        for (Tank tank : tanks) {
            if (tank.getTeam() != bullet.getOwner().getTeam() && checkTankCollision(bullet, tank)) {
                tank.setHealth(tank.getHealth() - bullet.getOwner().getBulletDamage());
                return true;
            }
        }

        return bullet.getX() < 0 || bullet.getX() > GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE ||
                bullet.getY() < 0 || bullet.getY() > GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE;
    }

    private boolean checkTankCollision(Bullet bullet, Tank tank) {
        return bullet.getX() < tank.getX() + GameConfig.TILE_SIZE &&
                bullet.getX() + GameConfig.BULLET_SIZE > tank.getX() &&
                bullet.getY() < tank.getY() + GameConfig.TILE_SIZE &&
                bullet.getY() + GameConfig.BULLET_SIZE > tank.getY();
    }

    public boolean checkPowerUpCollision(PowerUp powerup, Player player) {
        double powerUpSize = GameConfig.POWERUP_ENTITY_SIZE;
        double playerSize = GameConfig.TANK_SIZE;

        return powerup.getX() < player.getX() + playerSize &&
                powerup.getX() + powerUpSize > player.getX() &&
                powerup.getY() < player.getY() + playerSize &&
                powerup.getY() + powerUpSize > player.getY();
    }
}
