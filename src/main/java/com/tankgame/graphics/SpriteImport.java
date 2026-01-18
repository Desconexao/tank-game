package com.tankgame.graphics;

import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.ImageIcon;

public class SpriteImport {
    /**
    *   Tool to quickly import sprites by file name.
    **/

    String spritesPath = "assets/sprites/";

    public ImageIcon getSpriteResized(String spriteName, int resX, int resY){
        /**
        *   Returns an ImageIcon of the sprite with custom resolution.
        * 
        * @spriteName name (without extension) of the sprite file. Throws an exception if not found.
        * @resX size in pixels of the new width of the returned sprite.
        * @resY size in pixels of the new height of the returned sprite.
        * 
        * @return Scaled ImageIcon of the sprite.
        * 
        * @throws IllegalArgumentException("Sprite file not found")
        *
        **/
        String filePath = spritesPath + spriteName + ".png";
        if (!Files.exists(Path.of(filePath))) {
            throw new IllegalArgumentException("Sprite file not found");
        }

        ImageIcon importedIcon = new ImageIcon(filePath);
        Image scaledImage = importedIcon.getImage().getScaledInstance(resX, resY, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }


    public ImageIcon getSprite(String spriteName){
        /**
        *   Returns an ImageIcon of the sprite.
        * 
        * @spriteName name (without extension) of the sprite file. Throws an exception if not found.
        * 
        * @return ImageIcon of the sprite.
        * 
        * @throws IllegalArgumentException("Sprite file not found")
        *
        **/
        String filePath = spritesPath + spriteName + ".png";
        if (!Files.exists(Path.of(filePath))) {
            throw new IllegalArgumentException("Sprite file not found");
        }

        ImageIcon importedIcon = new ImageIcon(filePath);
        return importedIcon;
    }
}
