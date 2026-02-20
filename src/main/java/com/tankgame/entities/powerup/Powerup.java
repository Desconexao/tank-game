package com.tankgame.entities.powerup;

import com.tankgame.entities.Entity;

public abstract class Powerup extends Entity {

    private String description;
    private String name;

    public Powerup(double x, double y, String spritekey, String powerupName, String description){
        super(x, y, spritekey);
    }

    public String getDescription(){
        return description;
    }

    public String getName(){
        return name;
    }
}
