package com.tankgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameGrid {
    private char[][] grid;
    
    public GameGrid(boolean custom, int row, int col){

        grid = new char[row][col];

        try {

            List<String> lines = Files.readAllLines(Path.of("custommap.txt"));

            for(int i = 0; i < row; i++){
                grid[i] = lines.get(i).toCharArray();;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                System.out.print(" " + grid[i][j] + " ");
            }
            System.out.print("\n");
        }
    }


    public char[][] getGridMatrix(){
        return grid;
    }
}
