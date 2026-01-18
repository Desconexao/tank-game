package com.tankgame.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankgame.game.GameGrid;
import com.tankgame.graphics.SpriteImport;

public class GameScene extends JPanel {
    
    private JFrame mainWindow;
    SpriteImport sprites = new SpriteImport();

    public GameScene(JFrame mainWindow){

        this.mainWindow = mainWindow;

        // Create a 10x10 grid.
        JPanel gameGrid = new JPanel();
        gameGrid.setPreferredSize(new Dimension(75 * 10, 75 * 10));
        gameGrid.setLayout(new GridLayout(10, 10, 0, 0));

        // Fill grid spaces with icon sprites based on custom map layout.
        char[][] grid = (new GameGrid(false, 10, 10)).getGridMatrix();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                char currChar = grid[i][j];

                if(currChar == 'X'){
                    JLabel brick = new JLabel(sprites.getSpriteResized("brick", 75, 75));
                    gameGrid.add(brick);
                }
                else{
                    JLabel blackSquare = new JLabel(sprites.getSpriteResized("black", 75, 75));
                    gameGrid.add(blackSquare);
                }
            }
        }

        // Add created grid to the window
        // Grid is packed and centered so no theres no blank space between tiles.
        mainWindow.add(gameGrid, BorderLayout.CENTER);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}
