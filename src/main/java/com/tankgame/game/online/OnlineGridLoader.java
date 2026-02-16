package com.tankgame.game.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.tankgame.entities.tile.Brick;
import com.tankgame.entities.tile.Eagle;
import com.tankgame.entities.tile.Steel;
import com.tankgame.entities.tile.Tile;
import com.tankgame.entities.tile.Tree;
import com.tankgame.entities.tile.Water;
import com.tankgame.settings.GameConfig;

/**
 * Grid loader for online maps (scene_online.txt).
 * Handles 'M' tiles as player spawn points.
 */
public class OnlineGridLoader {
    private char[][] grid;
    private final int rows, cols;
    private Tile[][] tileGrid;
    private Eagle eagle;
    private List<int[]> playerSpawns = new ArrayList<>();

    public OnlineGridLoader(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        this.tileGrid = new Tile[rows][cols];

        loadMapChar();
        loadMapTiles();
    }

    private void loadMapChar() {
        String fileName = "world/scene_online.txt";
        try {
            System.out.println("Loading online map: " + fileName);
            List<String> lines = loadLinesFromResource(fileName);
            for (int i = 0; i < Math.min(rows, lines.size()); i++) {
                String line = lines.get(i);
                for (int j = 0; j < Math.min(cols, line.length()); j++) {
                    grid[i][j] = line.charAt(j);
                }
            }
        } catch (IOException e) {
            System.err.println("Online map load error: " + e.getMessage());
        }
    }

    private List<String> loadLinesFromResource(String resourcePath) throws IOException {
        List<String> lines = new ArrayList<>();
        
        // Try to load from JAR first using ClassLoader
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
        
        // Fall back to file system if not in JAR
        if (inputStream == null) {
            inputStream = Files.newInputStream(Path.of(resourcePath));
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        
        return lines;
    }

    private void loadMapTiles() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char tileChar = grid[i][j];
                Tile tile = getTile(tileChar, j * GameConfig.TILE_SIZE, i * GameConfig.TILE_SIZE);

                tileGrid[i][j] = tile;

                if (tile instanceof Eagle) {
                    eagle = (Eagle) tile;
                }

                // 'M' tiles are player spawn points
                if (tileChar == 'M') {
                    playerSpawns.add(new int[] { j * GameConfig.TILE_SIZE, i * GameConfig.TILE_SIZE });
                }
            }
        }
    }

    private Tile getTile(char tileChar, int x, int y) {
        Tile tile;

        tile = switch (tileChar) {
            case 'X' -> new Brick(x, y, "brick", GameConfig.BRICK_HP);
            case 'Y' -> new Steel(x, y, "steel", GameConfig.STEEL_HP);
            case 'T' -> new Tree(x, y, "tree", false);
            case 'E' -> new Eagle(x, y, "eagle", GameConfig.EAGLE_HP);
            case 'W' -> new Water(x, y, "water", true);
            case 'M' -> new Tile(x, y, "black", false); // Spawn point, no collision
            default -> new Tile(x, y, "black", false);
        };

        return tile;
    }

    public Tile[][] getTileGrid() {
        return tileGrid;
    }

    public char[][] getGridMatrix() {
        return grid;
    }

    public List<int[]> getPlayerSpawns() {
        return playerSpawns;
    }

    public int[] getPlayerSpawn(int index) {
        if (index < playerSpawns.size()) {
            return playerSpawns.get(index);
        }
        return new int[] { 0, 0 };
    }

    public Eagle getEagle() {
        return eagle;
    }
}
