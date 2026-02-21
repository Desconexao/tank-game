package com.tankgame.managers;

import java.util.ArrayList;
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
        this.collisionManager = collisionManager;
        this.lastPowerUpSpawn = System.currentTimeMillis();
        this.spawnCooldown = GameConfig.POWERUP_SPAWN_COOLDOWN_MS;
        this.random = new Random();
        this.currentMapPowerUps = new ArrayList<PowerUp>();

    }

    public void update(){
        if (System.currentTimeMillis() - lastPowerUpSpawn >= spawnCooldown){
            
            // Should reset the timer so we check again in another 'spawnCooldown' cycle
            // instead of checking every frame from now on.
            

            int prob = random.nextInt(GameConfig.POWERUP_SPAWN_BASE_CHANCE);

            if (prob >= GameConfig.POWERUP_SPAWN_PROBABILITY){
                lastPowerUpSpawn = System.currentTimeMillis(); 
                spawnNewPowerUp();
            }
        }
    }

    private void spawnNewPowerUp(){
        
        int rand = random.nextInt(6);
        PowerUp selectedPowerUp;
        double randX, randY;
        boolean canMove = false;

        // randomize power up coords
        do{
            
            int gridX = random.nextInt(GameConfig.GRID_WIDTH);
            int gridY = random.nextInt(GameConfig.GRID_HEIGHT);
            
            randX = gridX * GameConfig.TILE_SIZE;
            randY = gridY * GameConfig.TILE_SIZE;

            //System.out.println("it's happening");

            canMove = collisionManager.canMove(randX, randY, GameConfig.POWERUP_ENTITY_SIZE);
        }while(!canMove);

        

        // Randomize power up
        switch (rand) {
            case 0:
                selectedPowerUp = new StarPowerUp(randX, randY);
                break;
        
            default:
                System.err.println("must add all powerups, some lacking");
                selectedPowerUp = new StarPowerUp(randX, randY);
                break;
        }

        
        System.out.println("PowerUp " + selectedPowerUp.getName() + " spawned at X: " + randX + " Y: " + randY);
        // add to available powerups in map
        currentMapPowerUps.add(selectedPowerUp);
    }

    public List<PowerUp> getCurrentMapPowerUps(){
        return currentMapPowerUps;
    }


}
