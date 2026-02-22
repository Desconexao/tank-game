package com.tankgame.entities.collectible;

import com.tankgame.entities.tank.Tank;

public class StarPowerUp extends PowerUp {
    public StarPowerUp(double x, double y){
        
        String name = "STAR";
        String description = "Decreases bullet cooldown";
        String spriteKey = "star_power_up";

        super(x, y, spriteKey, name, description, true);

        
    }

    public void apply(Tank tank){
        // It doesn't make sense in my head.
    }
}
