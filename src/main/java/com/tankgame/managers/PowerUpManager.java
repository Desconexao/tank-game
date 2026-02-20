package com.tankgame.managers;

import java.util.List;
import java.util.Random;

import com.tankgame.entities.collectible.PowerUp;
import com.tankgame.entities.collectible.StarPowerUp;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.GameConfig;

public class PowerUpManager {

    private List<PowerUp> currentMapPowerUps;

    private GameGrid gridlogic;
    private CollisionManager collisionManager;

    private long lastPowerUpSpawn;
    private long spawnCooldown;
    private Random random;

    public PowerUpManager(GameGrid gridLogic, CollisionManager collisionManager){
        this.gridlogic = gridLogic;
        this.lastPowerUpSpawn = System.currentTimeMillis();
        this.spawnCooldown = GameConfig.POWERUP_SPAWN_COOLDOWN_MS;
        this.random = new Random();

    }

    public void update(){
        if (System.currentTimeMillis() - lastPowerUpSpawn >= spawnCooldown){
            int prob = random.nextInt(100);

            if (prob >= 75){
                spawnNewPowerUp();
                lastPowerUpSpawn = System.currentTimeMillis();
            }
        }
    }

    private void spawnNewPowerUp(){
        Class<? extends PowerUp> powerup = rouletteRandomPowerUp();

        currentMapPowerUps.add(null);
    }

    private Class<? extends PowerUp> rouletteRandomPowerUp(){
        int rand = random.nextInt(6);

        switch (rand) {
            case 0:
                return StarPowerUp.class;
        
            default:
                System.out.println("Must add other classes to not fall under here.");
                return StarPowerUp.class;
        }
    }
}
