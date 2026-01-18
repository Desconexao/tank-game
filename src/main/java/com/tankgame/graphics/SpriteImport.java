package com.tankgame.graphics;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.ImageIcon;

public class SpriteImport {

    String spritesPath = "assets/sprites/";

    public ImageIcon getSpriteResized(String spriteName, int resX, int resY) throws FileNotFoundException {
        String filePath = spritesPath + spriteName + ".png";
        if (!Files.exists(Path.of(filePath))) {
            throw new FileNotFoundException();
        }

        ImageIcon importedIcon = new ImageIcon(filePath);
        Image scaledImage = importedIcon.getImage().getScaledInstance(resX, resY, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
