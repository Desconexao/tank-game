package com.tankgame.entities.tile;

import com.tankgame.managers.BreakableManager;

public abstract class Breakable extends Tile implements BreakableManager{
    protected int health;
    protected int initialHealth;
    protected boolean isBroken = false;

    public Breakable(double x, double y, String spriteKey, int health){
        super(x, y, spriteKey, true);

        this.health = health;
        this.initialHealth = health;

    }

    @Override
    public int getHealth(){
        return this.health;
    }

    @Override
    public int inflictDamage(int damage){
        
        /**
         * @returns: new health
         */

        health -= damage;
        if (health <= 0){
            setBroken();
        }

        //System.out.println(health);
        //System.out.println(isBroken);
        return health;
    }

    @Override
    public void setCurrentStateSprite() {
        // who knows
        
    }

    // FOR SOME REASON
    @Override
    public void setHealth(int health) {
        this.health = health;
        
    }

    @Override
    public void setBroken() {
        isBroken = true;
    }

    @Override
    public void setUnbroken() {
        isBroken = false;
    }

    @Override
    public boolean isBroken() {
        return isBroken;
    }





    



}
