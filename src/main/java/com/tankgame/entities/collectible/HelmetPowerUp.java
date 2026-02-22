package com.tankgame.entities.collectible;

import com.tankgame.entities.tank.Tank;

public class HelmetPowerUp extends PowerUp{
    public HelmetPowerUp(double x, double y){
        
        String name = "HELMET";
        String description = "Makes you invulnerable";
        String spriteKey = "helmet_power_up";

        super(x, y, spriteKey, name, description, true);

        
    }

    public void apply(Tank tank){
        // It doesn't make sense in my head.
    }
}
