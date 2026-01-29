package com.tankgame.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import com.tankgame.settings.Globals;

public class GameGrid {
    private char[][] grid;
    private final int rows, cols;
    private final int numberOfPremadeMaps = Globals.PREMADE_WORLDS_COUNT;

    public GameGrid(boolean custom, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        loadMap(custom);
    }

    private void loadMap(boolean custom) {

        // This is ugly af, don't look.
        String mapNumberStr;
        int mapNumber = (new Random().nextInt(numberOfPremadeMaps) + 1);

        if (mapNumber < 10){
            mapNumberStr = "0" + mapNumber;
        }
        else{
            mapNumberStr = "" + mapNumber;
        }
            
        String fileName = custom ? "world/custommap.txt" : "world/scene_" + mapNumberStr + ".txt";
        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            for (int i = 0; i < Math.min(rows, lines.size()); i++) {
                grid[i] = lines.get(i).toCharArray();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar mapa: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            for (char c : row)
                sb.append(" ").append(c).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }

    public char[][] getGridMatrix() {
        return grid;
    }

    public void removeBlock(double x, double y) {
        int gridX = (int) (x / Globals.TILE_SIZE);
        int gridY = (int) (y / Globals.TILE_SIZE);

        if (gridY >= 0 && gridY < rows && gridX >= 0 && gridX < cols) {
            if (grid[gridY][gridX] == 'X') {
                grid[gridY][gridX] = ' ';
            }
        }
    }
}
