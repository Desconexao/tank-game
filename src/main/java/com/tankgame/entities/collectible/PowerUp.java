package com.tankgame.entities.collectible;

import com.tankgame.entities.Entity;
import com.tankgame.entities.tank.Tank;

public abstract class PowerUp extends Entity {
    protected String type;

    public PowerUp(double x, double y, String type, String spriteKey) {
        super(x, y, spriteKey);
        this.type = type;
    }

    public abstract void apply(Tank tank);

    public String getType() {
        return type;
    }
}
