package com.tankgame.entities.tile;

public class Steel extends Breakable{
    public Steel(double x, double y, String spriteKey, int health){
        super(x, y, spriteKey, health);
    }

    @Override
    public int inflictDamage(int damage){
        super.inflictDamage(damage);


        return health;
    }
}