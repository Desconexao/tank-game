package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Tank;
import com.tankgame.entities.tile.Tile;
import com.tankgame.entities.tile.Tree;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.GameConfig;
import com.tankgame.settings.SpriteList;

public class Renderer {
    private SpriteImport sprites;
    private int size = GameConfig.TILE_SIZE;
    private int bulletSize = GameConfig.BULLET_SIZE;
    private int tankSize = GameConfig.TANK_SIZE;

    public Map<String, ImageIcon> loadedSprites = new HashMap<>();
    private Queue<Object[]> renderQueue = new ArrayDeque<>();
    private Image vignetteImage;
    public boolean vignette;

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;

        

        loadSprites(SpriteList.SPRITES_TO_LOAD);

        try {
            vignetteImage = ImageIO.read(new File("assets/vignette.png"));
        } catch (IOException e) {
            System.err.println("Could not load vignette image: " + e.getMessage());
        }
    }

    public void drawVignetteImage(Graphics2D g2d) {
        if (vignetteImage != null) {
            g2d.drawImage(
                vignetteImage,
                0,
                0,
                GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE,
                GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE,
                null
            );
        }
    }

    public void OLDdraw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies, List<Object[]> bulletQueue) {
        Graphics2D g2d = (Graphics2D) g;

        char[][] grid = gameGridLogic.getGridMatrix();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                String tileKey = switch (grid[i][j]) {
                    case 'X' ->  "brick";
                    case 'Y' -> "steel";
                    case 'T' -> "tree";
                    case 'E' -> "eagle";
                    case 'W' -> "water";
                    default -> "black";
                };
                ImageIcon tileIcon = loadedSprites.get(tileKey);
                if (tileIcon != null) {
                    g2d.drawImage(tileIcon.getImage(), j * size, i * size, null);
                }
            }
        }

        drawTank(g2d, player);

        for (Enemy enemy : enemies) {
            drawTank(g2d, enemy);
        }

        if (bulletQueue != null) {
            drawSpriteQueue(g2d, bulletQueue);
        }
    }

     public void newDraw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies, List<Object[]> bulletQueue, List<Object[]> powerUpsQueue){
        Graphics2D g2d = (Graphics2D) g;

        Tile[][] grid = gameGridLogic.getGridTiles();
        
        // Draw background tiles (skip trees)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!(grid[i][j] instanceof Tree)) {
                    ImageIcon tileIcon = loadedSprites.get(grid[i][j].getSpriteKey());
                    if (tileIcon != null) {
                        g2d.drawImage(tileIcon.getImage(), j * size, i * size, null);
                    } else {
                        System.err.println("fellbackedup [PROBLEMMMMMM]");
                    }
                }
            }
        }

        drawTank(g2d, player);

        for (Enemy enemy : enemies) {
            drawTank(g2d, enemy);
        }

        // Draw foreground tiles (trees)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] instanceof Tree) {
                    ImageIcon tileIcon = loadedSprites.get(grid[i][j].getSpriteKey());
                    if (tileIcon != null) {
                        g2d.drawImage(tileIcon.getImage(), j * size, i * size, null);
                    }
                }
            }
        }

        if (bulletQueue != null) {
            drawSpriteQueue(g2d, bulletQueue);
        }

        if (powerUpsQueue != null) {
            drawSpriteQueue(g2d, powerUpsQueue);
        }
        
        if(vignette)
            drawVignetteImage(g2d);
    }
     // This is becoming a mess
     // Keeping this old newDraw method so online doesn't explode that easily (it probably will)
    public void newDraw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies, List<Object[]> bulletQueue){
        Graphics2D g2d = (Graphics2D) g;

        Tile[][] grid = gameGridLogic.getGridTiles();
        
        // Draw background tiles (skip trees)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!(grid[i][j] instanceof Tree)) {
                    ImageIcon tileIcon = loadedSprites.get(grid[i][j].getSpriteKey());
                    if (tileIcon != null) {
                        g2d.drawImage(tileIcon.getImage(), j * size, i * size, null);
                    } else {
                        System.err.println("fellbackedup [PROBLEMMMMMM]");
                    }
                }
            }
        }

        drawTank(g2d, player);

        for (Enemy enemy : enemies) {
            drawTank(g2d, enemy);
        }

        // Draw foreground tiles (trees)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] instanceof Tree) {
                    ImageIcon tileIcon = loadedSprites.get(grid[i][j].getSpriteKey());
                    if (tileIcon != null) {
                        g2d.drawImage(tileIcon.getImage(), j * size, i * size, null);
                    }
                }
            }
        }

        if (bulletQueue != null) {
            drawSpriteQueue(g2d, bulletQueue);
        }

    }

    private void drawTank(Graphics2D g2d, Tank tank) {
        ImageIcon icon = loadedSprites.get(tank.getSpriteKey());
        

        if (icon == null) {
            icon = loadedSprites.get("player_tank");
        }

        if (icon != null) {
            g2d.drawImage(icon.getImage(), (int) tank.getX(), (int) tank.getY(), null);
        }

        if (tank.isShielded()){
            ImageIcon shieldIcon = loadedSprites.get("shield");
            if (shieldIcon != null) {
                int offset = (GameConfig.TANK_SIZE - GameConfig.POWERUP_SHIELD_SIZE) / 2;
                
                g2d.drawImage(
                    shieldIcon.getImage(), 
                    (int) tank.getX() + offset, 
                    (int) tank.getY() + offset, 
                    null
                );
            }
        }
    }

    private void loadSprites(Object[][] spriteSpecs) {
        for (Object[] spec : spriteSpecs) {
            String key = (String) spec[0];
            int resX = (int) spec[1];
            int resY = (int) spec[2];
            try {
                loadedSprites.put(key, sprites.getSpriteResized(key, resX, resY));
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to load sprite: " + key + " - " + e.getMessage());
            }
        }
    }

    private void drawSpriteQueue(Graphics2D g2d, List<Object[]> bulletQueue) {
        List<Object[]> snapshot = new ArrayList<>(bulletQueue);
        for (Object[] spriteInfo : snapshot) {
            ImageIcon spriteIcon = loadedSprites.get((String) spriteInfo[0]);
            if (spriteIcon != null) {
                g2d.drawImage(spriteIcon.getImage(), (int) spriteInfo[1], (int) spriteInfo[2], null);
            }
        }



        // Well IDK who made and why bulletQueue took over the renderQueue's dequeue method, but lets keep playing along so
        // and I'm too drunk to care.

        // I think it would be more correct to pass an Entity object and pull sprite and coords from their methods
        // After all, are we dynamically rendering something that is not an Entity?
        Object[] spriteInfo;
        while ((spriteInfo = renderQueue.poll()) != null) {
            ImageIcon spriteIcon = loadedSprites.get((String) spriteInfo[0]);
            if (spriteIcon != null) {
                g2d.drawImage(
                    spriteIcon.getImage(),
                    (int) spriteInfo[1],
                    (int) spriteInfo[2],
                    null
                );
            }
        }
    }

    public boolean pushRenderQueue(String spriteName, int X, int Y) {
        if (loadedSprites.containsKey(spriteName)) {
            renderQueue.add(new Object[] { spriteName, X, Y });
            return true;
        }
        return false;
    }

    public void setVignette(boolean vignette){
        this.vignette = vignette;
    }
}
