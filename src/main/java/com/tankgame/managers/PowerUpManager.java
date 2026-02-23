package com.tankgame.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.tankgame.entities.collectible.GrenadePowerUp;
import com.tankgame.entities.collectible.HealthPack;
import com.tankgame.entities.collectible.HelmetPowerUp;
import com.tankgame.entities.collectible.PowerUp;
import com.tankgame.entities.collectible.ShovelPowerUp;
import com.tankgame.entities.collectible.StarPowerUp;
import com.tankgame.entities.collectible.TimeStopPowerUp;
import com.tankgame.entities.tank.Player;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Updatable;

public class PowerUpManager implements Updatable {

    private List<PowerUp> currentMapPowerUps;
    private List<PowerUp> activatedPowerUps = new ArrayList<>();

    private GameGrid gridlogic;
    private CollisionManager collisionManager;
    private Player player;

    private long lastPowerUpSpawn;
    private long spawnCooldown;
    private Random random;

    public PowerUpManager(GameGrid gridLogic, CollisionManager collisionManager, Player player) {
        this.gridlogic = gridLogic;
        this.collisionManager = collisionManager;
        this.player = player;

        this.lastPowerUpSpawn = System.currentTimeMillis();
        this.spawnCooldown = GameConfig.POWERUP_SPAWN_COOLDOWN_MS;
        this.random = new Random();
        this.currentMapPowerUps = new ArrayList<PowerUp>();

    }

    public void update() {
        if (System.currentTimeMillis() - lastPowerUpSpawn >= spawnCooldown) {

            // Should reset the timer so we check again in another 'spawnCooldown' cycle
            // instead of checking every frame from now on.

            int prob = random.nextInt(GameConfig.POWERUP_SPAWN_BASE_CHANCE);

            if (prob >= GameConfig.POWERUP_SPAWN_PROBABILITY) {
                lastPowerUpSpawn = System.currentTimeMillis();
                spawnNewPowerUp();
            }

        }

        activatedPowerUps.clear();
        Iterator<PowerUp> it = currentMapPowerUps.iterator();
        while (it.hasNext()) {
            PowerUp powerup = it.next();
            boolean pickedup = collisionManager.checkPowerUpCollision(powerup, player);
            if (pickedup) {
                activatedPowerUps.add(powerup);
                it.remove();
            }

        }
    }

    private void spawnNewPowerUp() {

        PowerUp selectedPowerUp;
        double randX, randY;
        boolean canMove = false;

        // randomize power up coords
        int tryCount = 0;
        do {

            int gridX = random.nextInt(GameConfig.GRID_WIDTH);
            int gridY = random.nextInt(GameConfig.GRID_HEIGHT);

            randX = gridX * GameConfig.TILE_SIZE;
            randY = gridY * GameConfig.TILE_SIZE;

            canMove = collisionManager.canMove(randX, randY, GameConfig.POWERUP_ENTITY_SIZE);

            // Just in case you get extremely unlucky
            // or for some reason the map has no space at all (would it be possible?)
            if (tryCount > 50) {
                System.err.println("50 tries and no available coord found. Not spawning powerup to avoid hang up");
                return;
            }
            tryCount++;
        } while (!canMove);

        // Randomize power up
        int rand = random.nextInt(6);
        switch (rand) {
            case 0:
                selectedPowerUp = new StarPowerUp(randX, randY);
                break;

            case 1:
                selectedPowerUp = new GrenadePowerUp(randX, randY);
                break;

            case 2:
                selectedPowerUp = new HelmetPowerUp(randX, randY);
                break;

            case 3:
                selectedPowerUp = new ShovelPowerUp(randX, randY);
                break;

            case 4:
                selectedPowerUp = new HealthPack(randX, randY);
                break;

            case 5:
                selectedPowerUp = new TimeStopPowerUp(randX, randY);
                break;

            default:
                System.err.println("must add all powerups, some lacking");
                selectedPowerUp = new StarPowerUp(randX, randY);
                break;
        }

        // remove later, debugging stuff
        // selectedPowerUp = new StarPowerUp(randX, randY);
        System.out.println("PowerUp " + selectedPowerUp.getName() + " spawned at X: " + randX + " Y: " + randY);
        // add to available powerups in map
        currentMapPowerUps.add(selectedPowerUp);
    }

    public List<PowerUp> getCurrentMapPowerUps() {
        return currentMapPowerUps;
    }

    public List<PowerUp> getActivatedPowerUps() {
        return activatedPowerUps;
    }

}
