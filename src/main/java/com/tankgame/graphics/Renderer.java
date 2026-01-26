package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.ImageIcon;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Tank;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.Globals;

public class Renderer {
    private SpriteImport sprites;
    private int size = Globals.TILE_SIZE;
    private int bulletSize = Globals.BULLET_SIZE;

    public Map<String, ImageIcon> loadedSprites = new HashMap<>();
    private Queue<Object[]> renderQueue = new ArrayDeque<>();

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;

        Object[][] spritesToLoad = {
                { "player_tank", size, size },
                { "player_tank_down", size, size },
                { "player_tank_left", size, size },
                { "player_tank_right", size, size },
                { "player_tank", size, size }, // enemy
                { "brick", size, size },
                { "black", size, size },
                { "bullet_vertical", bulletSize, bulletSize }
        };

        loadSprites(spritesToLoad);
    }

    public void draw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies) {
        Graphics2D g2d = (Graphics2D) g;

        // Grid
        char[][] grid = gameGridLogic.getGridMatrix();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                String tileKey = (grid[i][j] == 'X') ? "brick" : "black";
                g2d.drawImage(loadedSprites.get(tileKey).getImage(), j * size, i * size, null);
            }
        }

        // Player
        drawTank(g2d, player);

        // Enemies
        for (Enemy enemy : enemies) {
            drawTank(g2d, enemy);
        }

        // Bullets
        drawSpriteQueue(g2d);
    }

    private void drawTank(Graphics2D g2d, Tank tank) {
        ImageIcon icon = loadedSprites.get(tank.getSpriteKey());

        if (icon == null) {
            icon = loadedSprites.get("player_tank");
        }

        g2d.drawImage(icon.getImage(), (int) tank.getX(), (int) tank.getY(), null);
    }

    private void loadSprites(Object[][] spriteSpecs) {
        for (Object[] spec : spriteSpecs) {
            String key = (String) spec[0];
            int resX = (int) spec[1];
            int resY = (int) spec[2];
            loadedSprites.put(key, sprites.getSpriteResized(key, resX, resY));
        }
    }

    public void drawSpriteQueue(Graphics2D g2d) {
        while (!renderQueue.isEmpty()) {
            Object[] spriteInfo = renderQueue.poll();
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
