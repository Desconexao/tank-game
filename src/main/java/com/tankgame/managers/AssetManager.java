package com.tankgame.managers;

import com.tankgame.graphics.SpriteImport;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private final SpriteImport spriteImporter;
    private final Map<String, ImageIcon> cachedSprites;

    public AssetManager() {
        this.spriteImporter = new SpriteImport();
        this.cachedSprites = new HashMap<>();
    }

    public ImageIcon getSprite(String key, int width, int height) {
        String cacheKey = key + "_" + width + "x" + height;

        if (!cachedSprites.containsKey(cacheKey)) {
            try {
                ImageIcon sprite = spriteImporter.getSpriteResized(key, width, height);
                cachedSprites.put(cacheKey, sprite);
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Sprite '" + key + "' not found. Using fallback.");
                return getFallbackSprite(width, height);
            }
        }

        return cachedSprites.get(cacheKey);
    }

    private ImageIcon getFallbackSprite(int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = img.createGraphics();
        g2d.setColor(java.awt.Color.MAGENTA);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(java.awt.Color.BLACK);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.dispose();
        return new ImageIcon(img);
    }

    public void preloadSprites(String[] spriteKeys, int width, int height) {
        for (String key : spriteKeys) {
            try {
                getSprite(key, width, height);
            } catch (Exception e) {
                System.err.println("Failed to preload sprite: " + key);
            }
        }
    }

    public void clearCache() {
        cachedSprites.clear();
    }
}
