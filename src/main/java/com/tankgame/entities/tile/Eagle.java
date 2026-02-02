package com.tankgame.entities.tile;

public class Eagle extends Breakable{
    public Eagle(double x, double y, String spriteKey, int health){
        super(x, y, spriteKey, health);
    }

    @Override
    public int inflictDamage(int damage) {
        super.inflictDamage(damage);

        setCurrentStateSprite();

        return health;
    }

    @Override
    public void setCurrentStateSprite() {
        if (health <= 0)
            this.spriteKey = "flag";
    }

}
