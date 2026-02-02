package com.tankgame.managers;

public interface BreakableManager {
    public int getHealth();
    public void setHealth(int health);
    public void setBroken();
    public void setUnbroken();
    public int inflictDamage(int damage);
    public void setCurrentStateSprite();
    public boolean isBroken();

}
