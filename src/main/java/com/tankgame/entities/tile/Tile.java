package com.tankgame.entities.tile;
import com.tankgame.entities.Entity;

public class Tile extends Entity{
    protected boolean collidable;

    public Tile(double x, double y, String spriteKey, boolean collidable){
        super(x, y, spriteKey);
        this.collidable = collidable;
    }

    public boolean isCollidable(){
        return this.collidable;
    }

    public void setCollision(boolean collision){
        this.collidable = collision;
    }

}
