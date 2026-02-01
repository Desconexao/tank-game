package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.ImageIcon;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Tank;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.GameConfig;

public class Renderer {
    private SpriteImport sprites;
    private int size = GameConfig.TILE_SIZE;
    private int bulletSize = GameConfig.BULLET_SIZE;
    private int tankSize = GameConfig.TANK_SIZE;

    public Map<String, ImageIcon> loadedSprites = new HashMap<>();
    private Queue<Object[]> renderQueue = new ArrayDeque<>();

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;

        Object[][] spritesToLoad = {
                { "tank_up_gray",       tankSize,   tankSize },
                { "tank_down_gray",   tankSize,   tankSize },
                { "tank_left_gray",   tankSize,   tankSize },
                { "tank_right_gray",  tankSize,   tankSize },
                { "brick",              size,   size },
                { "black",              size,   size },
                { "steel",              size,   size },
                { "bullet_vertical",    bulletSize, bulletSize },
                { "tank_up_yellow",     tankSize,   tankSize},
                { "tank_down_yellow",   tankSize,   tankSize},
                { "tank_left_yellow",   tankSize,   tankSize},
                { "tank_right_yellow",  tankSize,   tankSize},
                { "tank_up_green",     tankSize,   tankSize},
                { "tank_down_green",   tankSize,   tankSize},
                { "tank_left_green",   tankSize,   tankSize},
                { "tank_right_green",  tankSize,   tankSize},
                { "tank_up_red",       tankSize,   tankSize},
                { "tank_down_red",     tankSize,   tankSize},
                { "tank_left_red",     tankSize,   tankSize},
                { "tank_right_red",    tankSize,   tankSize},
                { "eagle",             size,   size},
                {"tree",               size,   size},
                {"water",              size,   size},
                {"flag",               size,   size}

        };

        loadSprites(spritesToLoad);
    }

    public void draw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies, List<Object[]> bulletQueue) {
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

    private void drawTank(Graphics2D g2d, Tank tank) {
        ImageIcon icon = loadedSprites.get(tank.getSpriteKey());

        if (icon == null) {
            icon = loadedSprites.get("player_tank");
        }

        if (icon != null) {
            g2d.drawImage(icon.getImage(), (int) tank.getX(), (int) tank.getY(), null);
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
    }

    public boolean pushRenderQueue(String spriteName, int X, int Y) {
        if (loadedSprites.containsKey(spriteName)) {
            renderQueue.add(new Object[] { spriteName, X, Y });
            return true;
        }
        return false;
    }
}
