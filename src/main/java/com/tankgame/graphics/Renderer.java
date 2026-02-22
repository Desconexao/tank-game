package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;

import com.tankgame.entities.Entity;
import com.tankgame.entities.projectile.Bullet;
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
    public Map<String, ImageIcon> loadedSprites = new HashMap<>();

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;
        loadSprites(SpriteList.SPRITES_TO_LOAD);
    }

    // Método principal, simples e direto
    public void draw(Graphics g, GameGrid gameGridLogic, Tank player, List<Enemy> enemies, List<Bullet> bullets) {
        Graphics2D g2d = (Graphics2D) g;
        Tile[][] grid = gameGridLogic.getGridTiles();

        // CAMADA 1: Background (Tiles normais)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!(grid[i][j] instanceof Tree)) {
                    drawEntity(g2d, grid[i][j]);
                }
            }
        }

        // CAMADA 2: Entidades (Balas e Tanques)
        if (bullets != null) {
            for (Bullet bullet : bullets) {
                drawEntity(g2d, bullet);
            }
        }

        drawEntity(g2d, player);

        for (Enemy enemy : enemies) {
            drawEntity(g2d, enemy);
        }

        // CAMADA 3: Foreground (Árvores cobrem os tanques e balas)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] instanceof Tree) {
                    drawEntity(g2d, grid[i][j]);
                }
            }
        }
    }

    // Método mágico que desenha QUALQUER entidade
    private void drawEntity(Graphics2D g2d, Entity entity) {
        if (entity == null || entity.getSpriteKey() == null)
            return;

        ImageIcon icon = loadedSprites.get(entity.getSpriteKey());

        // Fallback pro player, como você já tinha feito
        if (icon == null && entity instanceof Tank) {
            icon = loadedSprites.get("player_tank");
        }

        if (icon != null) {
            g2d.drawImage(icon.getImage(), (int) entity.getX(), (int) entity.getY(), null);
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
}
