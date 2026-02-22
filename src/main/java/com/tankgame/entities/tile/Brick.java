package com.tankgame.entities.tile;

public class Brick extends Breakable {

    public Brick(double x, double y, String spriteKey, int health) {
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
        double healthPercentage = (double) this.health / this.initialHealth;

        if (healthPercentage > 0.75) {
            this.spriteKey = "brick";
        } else if (healthPercentage > 0.5) {
            this.spriteKey = "brick75";
        } else if (healthPercentage > 0.25) {
            this.spriteKey = "brick50";
        } else if (healthPercentage > 0) {
            this.spriteKey = "brick25";
        }
    }
}
