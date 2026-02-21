package com.tankgame.entities.collectible;

import com.tankgame.entities.tank.Tank;

public class ShovelPowerUp extends PowerUp{
    public ShovelPowerUp(double x, double y){
        
        String name = "SHOVEL";
        String description = "Protects your base with steel";
        String spriteKey = "shovel_power_up";

        super(x, y, spriteKey, name, description, false);

        
    }

    public void apply(Tank tank){
        // It doesn't make sense in my head.
    }
}
