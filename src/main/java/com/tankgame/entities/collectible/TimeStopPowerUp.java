package com.tankgame.entities.collectible;

import com.tankgame.entities.tank.Tank;

public class TimeStopPowerUp extends PowerUp{
    public TimeStopPowerUp(double x, double y){
        
        String name = "STOPWATCH";
        String description = "ZA WARUDO!";
        String spriteKey = "timestop_power_up";

        super(x, y, spriteKey, name, description, false);

        
    }

    public void apply(Tank tank){
        // It doesn't make sense in my head.
    }
}
