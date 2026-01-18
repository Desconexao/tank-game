package com.tankgame.graphics;

import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.ImageIcon;

public class SpriteImport {
    /**
    *   Ferramenta para importar sprites rapidamente com o nome do arquivo.
    **/

    String spritesPath = "assets/sprites/";

    public ImageIcon getSpriteResized(String spriteName, int resX, int resY){
        /**
        *   Retorna ImageIcon do sprite com resolução customizada.
        * 
        * @spriteName nome (sem extensão) do arquivo de sprite. Lança uma exceção caso não seja encontrado.
        * @resX tamanho em pixels da nova largura do sprite retornado.
        * @resY tamanho em pixels da nova altura do sprite retornado.
        * 
        * @return ImageIcon scaled do sprite.
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
        *   Retorna ImageIcon do sprite.
        * 
        * @spriteName nome (sem extensão) do arquivo de sprite. Lança uma exceção caso não seja encontrado.
        * 
        * @return ImageIcon scaled do sprite.
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
