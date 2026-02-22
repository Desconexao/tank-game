package com.tankgame.entities.tank;

import com.tankgame.entities.collectible.PowerUp;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;
import com.tankgame.utils.Team;

public class Player extends Tank {
    private long bulletCooldown = GameConfig.PLAYER_BULLET_COOL_DOWN_MS;

    public Player(double x, double y, int health, String spriteKey, Direction direction, TankColors color) {
        super(x, y, health, spriteKey, direction, color, Team.PLAYER);
    }

    @Override
    public void moveUp() {
        super.moveUp();
        setDirection(Direction.UP);
    }

    @Override
    public void moveDown() {
        super.moveDown();
        setDirection(Direction.DOWN);
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        setDirection(Direction.LEFT);
    }

    @Override
    public void moveRight() {
        super.moveRight();
        setDirection(Direction.RIGHT);
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        this.spriteKey = switch (direction) {
            case UP -> "tank_up_gray";
            case DOWN -> "tank_down_gray";
            case LEFT -> "tank_left_gray";
            case RIGHT -> "tank_right_gray";
        };
    }

    public long getBulletCooldown() {
        return bulletCooldown;
    }

    public void setBulletCooldown(long cooldown_ms){
        this.bulletCooldown = cooldown_ms;
    }

    

    public void setPowerUp(PowerUp powerup){
        switch (powerup.getName()) {
            case "STAR":
                startStarPowerUp();
                break;

            case "HEALTH":
                setHealth(getHealth() + 1);
                break;

            case "HELMET":
                activateShield();
                break;
        
            default:
                return;
        }
    }

    private void startStarPowerUp(){
        setBulletCooldown(250);
    }

    private void endStarPowerUp(){
        setBulletCooldown(GameConfig.PLAYER_BULLET_COOL_DOWN_MS);
    }
}
