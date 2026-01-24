package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
// import java.awt.RenderingHints;
import javax.swing.ImageIcon;

import com.tankgame.entities.tank.Tank;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.Globals;

public class Renderer {
    private SpriteImport sprites;
    private ImageIcon playerUp, playerDown, playerLeft, playerRight;
    private ImageIcon brick, blackSquare;

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;
        int size = Globals.TILE_SIZE;

        playerUp = sprites.getSpriteResized("player_tank", size, size);
        playerDown = sprites.getSpriteResized("player_tank_down", size, size);
        playerLeft = sprites.getSpriteResized("player_tank_left", size, size);
        playerRight = sprites.getSpriteResized("player_tank_right", size, size);
        brick = sprites.getSpriteResized("brick", size, size);
        blackSquare = sprites.getSpriteResized("black", size, size);
    }

    public void draw(Graphics g, GameGrid gameGridLogic, Tank player) {
        Graphics2D g2d = (Graphics2D) g;

        // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        char[][] grid = gameGridLogic.getGridMatrix();
        int size = Globals.TILE_SIZE;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ImageIcon icon = (grid[i][j] == 'X') ? brick : blackSquare;
                g2d.drawImage(icon.getImage(), j * size, i * size, null);
            }
        }

        ImageIcon tankIcon = getSpriteByKey(player.getSpriteKey());
        g2d.drawImage(tankIcon.getImage(), (int) player.getX(), (int) player.getY(), null);
    }

    private ImageIcon getSpriteByKey(String spriteKey) {
        return switch (spriteKey) {
            case "player_tank_down" -> playerDown;
            case "player_tank_left" -> playerLeft;
            case "player_tank_right" -> playerRight;
            case "brick" -> brick;
            case "black" -> blackSquare;
            default -> playerUp;
        };
    }
}
