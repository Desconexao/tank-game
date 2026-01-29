package com.tankgame.graphics;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.ImageIcon;

public class SpriteImport {
    String spritesPath = "assets/sprites/";

    public ImageIcon getSpriteResized(String spriteName, int resX, int resY) {
        String filePath = spritesPath + spriteName + ".png";

        if (!Files.exists(Path.of(filePath))) {
            System.out.println("Creating placeholder for: " + spriteName + " (" + resX + "x" + resY + ")");
            return createPlaceholderSprite(spriteName, resX, resY);
        }

        try {
            ImageIcon importedIcon = new ImageIcon(filePath);
            Image scaledImage = importedIcon.getImage().getScaledInstance(resX, resY, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error loading sprite '" + spriteName + "': " + e.getMessage());
            return createPlaceholderSprite(spriteName, resX, resY);
        }
    }

    private ImageIcon createPlaceholderSprite(String spriteName, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        Color fillColor;
        Color borderColor = Color.BLACK;

        if (spriteName.contains("player")) {
            fillColor = Color.GREEN;
        } else if (spriteName.contains("enemy")) {
            fillColor = Color.RED;
        } else if (spriteName.contains("brick")) {
            fillColor = new Color(139, 69, 19);
        } else if (spriteName.contains("black")) {
            fillColor = Color.BLACK;
            borderColor = Color.GRAY;
        } else if (spriteName.contains("bullet")) {
            fillColor = Color.YELLOW;
        } else {
            fillColor = Color.MAGENTA;
        }

        g2d.setColor(fillColor);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(borderColor);
        g2d.drawRect(0, 0, width - 1, height - 1);

        if (width > 30 && height > 30) {
            g2d.setColor(Color.WHITE);
            String shortName = spriteName.length() > 5 ? spriteName.substring(0, 5) : spriteName;
            g2d.drawString(shortName, 5, height / 2);
        }

        g2d.dispose();
        return new ImageIcon(img);
    }

    public ImageIcon getSprite(String spriteName) {
        return getSpriteResized(spriteName, 64, 64);
    }
}
