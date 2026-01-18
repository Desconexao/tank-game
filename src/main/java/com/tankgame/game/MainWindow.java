package com.tankgame.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankgame.graphics.SpriteImport;

public class MainWindow {

    public MainWindow(){
        SpriteImport sprites = new SpriteImport();
        JFrame frame = new JFrame("Tank game");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel gameGrid = new JPanel();
        gameGrid.setPreferredSize(new Dimension(75 * 10, 75 * 10));

        gameGrid.setLayout(new GridLayout(10, 10, 0, 0));
        char[][] grid = (new GameGrid(true, 10, 10)).getGridMatrix();
        try{
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
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        frame.add(gameGrid, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
