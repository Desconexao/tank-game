package com.tankgame.entities.collectible;

import com.tankgame.entities.tank.Tank;

public class HealthPack extends PowerUp {
    public HealthPack(double x, double y) {
        super(x, y, "healthpack", "HEALTH", "powerup_health");
    }

    @Override
    public void apply(Tank tank) {
        tank.setHealth(tank.getHealth() + 1);
    }
}
