package com.tankgame.entities.tile;

public class Steel extends Breakable {

    public Steel(double x, double y, String spriteKey, int health) {
        super(x, y, spriteKey, health);
    }

    @Override
    public int inflictDamage(int damage) {
        return health;
    }

    @Override
    public void setCurrentStateSprite() {
        // it's always the same sprite, so do nothing...
    }
}
