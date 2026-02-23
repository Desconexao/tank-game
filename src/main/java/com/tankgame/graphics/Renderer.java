package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.tankgame.entities.Entity;
import com.tankgame.entities.collectible.PowerUp;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Tank;
import com.tankgame.entities.tile.Tile;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.GameConfig;
import com.tankgame.settings.SpriteList;

public class Renderer {
    private SpriteImport sprites;
    private int size = GameConfig.TILE_SIZE;
    public Map<String, ImageIcon> loadedSprites = new HashMap<>();
    private Image vignetteImage;
    public boolean vignette;

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;
        loadSprites(SpriteList.SPRITES_TO_LOAD);

        try {
            InputStream vignetteStream = getClass().getClassLoader().getResourceAsStream("assets/vignette.png");
            if (vignetteStream != null) {
                vignetteImage = ImageIO.read(vignetteStream);
                vignetteStream.close();
            } else {
                System.err.println("Could not find vignette image resource");
            }
        } catch (IOException e) {
            System.err.println("Could not load vignette image: " + e.getMessage());
        }
    }

    public void drawVignetteImage(Graphics2D g2d) {
        if (vignetteImage != null) {
            g2d.drawImage(
                    vignetteImage,
                    0, 0,
                    GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE,
                    GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE,
                    null);
        }
    }

    public void draw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies, List<Bullet> bullets,
            List<PowerUp> powerups) {
        Graphics2D g2d = (Graphics2D) g;
        Tile[][] grid = gameGridLogic.getGridTiles();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!"tree".equals(grid[i][j].getSpriteKey())) {
                    drawEntity(g2d, grid[i][j]);
                }
            }
        }

        if (powerups != null) {
            for (PowerUp powerup : powerups) {
                drawEntity(g2d, powerup);
            }
        }
        if (bullets != null) {
            for (Bullet bullet : bullets) {
                drawEntity(g2d, bullet);
            }
        }

        drawTank(g2d, player);

        if (enemies != null) {
            for (Enemy enemy : enemies) {
                drawTank(g2d, enemy);
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if ("tree".equals(grid[i][j].getSpriteKey())) {
                    drawEntity(g2d, grid[i][j]);
                }
            }
        }

        if (vignette) {
            drawVignetteImage(g2d);
        }
    }

    private void drawEntity(Graphics2D g2d, Entity entity) {
        if (entity == null || entity.getSpriteKey() == null)
            return;

        ImageIcon icon = loadedSprites.get(entity.getSpriteKey());
        if (icon != null) {
            g2d.drawImage(icon.getImage(), (int) entity.getX(), (int) entity.getY(), null);
        }
    }

    private void drawTank(Graphics2D g2d, Tank tank) {
        if (tank == null)
            return;

        drawEntity(g2d, tank);

        if (tank.isShielded()) {
            ImageIcon shieldIcon = loadedSprites.get("shield");
            if (shieldIcon != null) {
                int offset = (GameConfig.TANK_SIZE - GameConfig.POWERUP_SHIELD_SIZE) / 2;
                g2d.drawImage(
                        shieldIcon.getImage(),
                        (int) tank.getX() + offset,
                        (int) tank.getY() + offset,
                        null);
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

    public void setVignette(boolean vignette) {
        this.vignette = vignette;
    }
}
