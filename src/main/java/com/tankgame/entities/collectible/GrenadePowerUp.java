package com.tankgame.entities.collectible;

import com.tankgame.entities.tank.Tank;

public class GrenadePowerUp extends PowerUp{
    public GrenadePowerUp(double x, double y){
        
        String name = "GRENADE";
        String description = "BOOM all the enemies";
        String spriteKey = "grenade_power_up";

        super(x, y, spriteKey, name, description);

        
    }

    public void apply(Tank tank){
        // It doesn't make sense in my head.
    }
}
