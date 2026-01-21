package com.tankgame.graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankgame.entities.Player;
import com.tankgame.game.GameGrid;

public class Renderer {
    private SpriteImport sprites;

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;
    }

    public void render(JPanel container, GameGrid gameGridLogic, Player player) {
        container.removeAll();

        char[][] grid = gameGridLogic.getGridMatrix();
        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JLabel label;

                if (j == player.getX() && i == player.getY()) {
                    label = new JLabel(sprites.getSpriteResized(player.getSpriteKey(), 75, 75));
                } else if (grid[i][j] == 'X') {
                    label = new JLabel(sprites.getSpriteResized("brick", 75, 75));
                } else {
                    label = new JLabel(sprites.getSpriteResized("black", 75, 75));
                }

                container.add(label);
            }
        }

        container.revalidate();
        container.repaint();
    }
}
