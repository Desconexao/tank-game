package com.tankgame.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class GameGrid {
    private char[][] grid;
    private int row;
    private int col;

    public GameGrid(boolean custom, int row, int col) {
        /*
        If custom == true: reads the text file at world/custommap.txt and converts it to a character matrix based on row and col.
        If false, selects one of the pre-made maps in world/scene_*.txt
         */

        grid = new char[row][col];
        this.row = row;
        this.col = col;

        String sceneFileName;

        if(custom){
            sceneFileName = "world/custommap.txt";
        }
        else{
            // Randomizes a map from world/
            int n = (new Random()).nextInt(5) + 1;
            sceneFileName = "world/scene_0" + n + ".txt";
        }

        /*
                Reads each line from the scene layout file and converts each line to a character array.
                For example: "X_X
                              _X_"
                
                grid[3][3] = [["X", "_", "X"],
                              ["_", "X", "_"]];
        */

        try {
            List<String> lines = Files.readAllLines(Path.of(sceneFileName));
            for (int i = 0; i < row; i++) {
                grid[i] = lines.get(i).toCharArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Completely unnecessary print of the matrix
        System.out.println(this.toString());
    }

    // Returns the matrix
    public char[][] getGridMatrix() {
        return grid;
    }

    // Returns the grid as a String.
    @Override
    public String toString(){
        String gridString = "";

        for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    gridString += " " + grid[i][j] + " ";
                }
                gridString += "\n";
            }
        return gridString;
    }
}
