package com.tankgame.entities.tank;

import com.tankgame.entities.Entity;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.settings.Globals;
import com.tankgame.utils.Movable;

public abstract class Tank extends Entity implements Movable {
    protected int health;
    protected double speed;
    protected int direction;

    public Tank(double x, double y, int health, String spriteKey, int direction) {
        super(x, y, spriteKey);
        this.health = health;
        this.speed = 4.0;
        this.direction = direction;
    }

    public void moveUp() {
        this.y -= speed;
    }

    public void moveDown() {
        this.y += speed;
    }

    public void moveLeft() {
        this.x -= speed;
    }

    public void moveRight() {
        this.x += speed;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Bullet shoot(){
        // Caution. Stay away from this mess.

        // Tank is 13x13 (for now) pixels. So its cannon tip should be located in X + 7 and Y + 0, for up direction, in example.
        // But the sprited is resized to Globals.TILE_SIZE (Which is 75 for the moment, may change whatever)
        // SO to get the middle of X in sprite it must be (x + 7)* 75 / 13 and (y +) * 75 / 13
        Bullet bullet;
        int p_OGSize = Globals.PLAYER_ORIGINAL_SPRITE_SIZE;
        // int b_OGSize = Globals.BULLET_ORIGINAL_SPRITE_SIZE; // Unused

        int p_Size = Globals.TILE_SIZE;
        int b_Size = Globals.BULLET_SIZE;

        // Correct centering logic: (Container Width - Bullet Width) / 2
        int centerOffset = (p_Size - b_Size) / 2;

        // Use float cast to ensure scaling is precise (75/13 = 5.76, not 5)
        float scaleFactor = (float) p_Size / p_OGSize;
        

        switch (direction) {

            case 0:
                // X: Centered. Y: Original tip offset scaled
                bullet = new Bullet((int) x + centerOffset, (int) (y + 1 * scaleFactor), 0, this);
                break;
            case 1:
                // X: Centered. Y: Bottom of tank + margin
                bullet = new Bullet((int) x + centerOffset, (int) (y + p_Size), 1, this);
                break;
            
            case 2:
                // X: Left of tank. Y: Centered
                bullet = new Bullet((int) (x - b_Size/2), (int) y + centerOffset, 2, this);
                break;

            case 3:
                // X: Right of tank. Y: Centered
                bullet = new Bullet((int) (x + p_Size), (int) y + centerOffset, 3, this);
                break;

            default:
                bullet = new Bullet(x, y, 0, this);
                break;
        }

        return bullet;
    }

    @Override
    public int getDirection(){
        return direction;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }
}
