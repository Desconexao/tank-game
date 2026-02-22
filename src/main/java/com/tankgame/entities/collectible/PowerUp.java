package com.tankgame.entities.collectible;

import com.tankgame.entities.Entity;
import com.tankgame.entities.tank.Tank;

public abstract class PowerUp extends Entity {
    private String description;
    private String name;
    private boolean isPlayerAffected;

    public PowerUp(double x, double y, String spriteKey, String name, String description, boolean isPlayerAffected) {
        super(x, y, spriteKey);
        this.name = name;
        this.description = description;
        this.isPlayerAffected = isPlayerAffected;
    }

    public abstract void apply(Tank tank);

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }

    public boolean isPlayerAffected(){
        return isPlayerAffected;
    }
}
