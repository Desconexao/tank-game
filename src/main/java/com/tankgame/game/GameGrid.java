package com.tankgame.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameGrid {
    private char[][] grid;
    private int row;
    private int col;

    public GameGrid(boolean custom, int row, int col) {
        /*
        Caso custom == true: lê o arquivo de texto em world/custommap.txt e converte-o em uma matrix de caracteres baseada em row em col.
        Caso false, seleciona um dos mapas pré-prontos em world/scene_*.txt
         */

        grid = new char[row][col];
        this.row = row;
        this.col = col;

        if(custom){
            try {
                /*
                Lê cada linha do arquivo custommap.txt e converte cada linha em um array de caracteres.
                Por exemplo: "X_X
                              _X_"
                
                grid[3][3] = [["X", "_", "X"],
                              ["_", "X", "_"]];
                */

                List<String> lines = Files.readAllLines(Path.of("world/custommap.txt"));
                for (int i = 0; i < row; i++) {
                    grid[i] = lines.get(i).toCharArray();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Print totalmente desnecessário da matriz
            System.out.println(this.toString());
        }
        else{
            return;
        }
    }

    // Retorna a matriz
    public char[][] getGridMatrix() {
        return grid;
    }

    // Retorna a grid em String.
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
