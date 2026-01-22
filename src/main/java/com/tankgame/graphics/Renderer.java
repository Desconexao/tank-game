package com.tankgame.graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankgame.entities.Player;
import com.tankgame.game.GameGrid;

public class Renderer {
    private SpriteImport sprites;
    private ImageIcon playerUp;
    private ImageIcon playerDown;
    private ImageIcon playerLeft;
    private ImageIcon playerRight;
    private ImageIcon brick;
    private ImageIcon blackSquare;

    private char[][] grid;

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;

        playerUp = sprites.getSpriteResized("player_tank", 75, 75);
        playerDown = sprites.getSpriteResized("player_tank_down", 75, 75);
        playerLeft = sprites.getSpriteResized("player_tank_left", 75, 75);
        playerRight = sprites.getSpriteResized("player_tank_right", 75, 75);
        brick = sprites.getSpriteResized("brick", 75, 75);
        blackSquare = sprites.getSpriteResized("black", 75, 75);


    }

    public void render(JPanel container, GameGrid gameGridLogic, Player player) {
        container.removeAll();
        grid = gameGridLogic.getGridMatrix();
        
        int rows = grid.length;
        int cols = grid[0].length;
        

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ImageIcon icon;

                if (grid[i][j] == '˄') icon = playerUp;
                else if (grid[i][j] == '˅') icon = playerDown;
                else if (grid[i][j] == '<') icon = playerLeft;
                else if (grid[i][j] == '>') icon = playerRight;
                else if (grid[i][j] == 'X') icon = brick;
                else icon = blackSquare;

                container.add(new JLabel(icon));
            }
        }
            System.out.println(gameGridLogic.toString()); 

        container.revalidate();
        container.repaint();
    }
}
