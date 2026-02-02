package com.tankgame.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import com.tankgame.entities.tile.Breakable;
import com.tankgame.entities.tile.Brick;
import com.tankgame.entities.tile.Eagle;
import com.tankgame.entities.tile.Steel;
import com.tankgame.entities.tile.Tile;
import com.tankgame.settings.GameConfig;

public class GameGrid {
    private char[][] grid;
    private final int rows, cols;
    private Tile[][] tileGrid;

    public GameGrid(boolean custom, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        this.tileGrid = new Tile[rows][cols];
        loadMapChar(custom);
        loadMapTiles();
        // Let's keep the visual representation of characters for some reason (Maybe useful (Maybe I'd miss them))
    }

    private void loadMapChar(boolean custom) {
        // This will break if we go above 9 maps.
        String fileName = custom ? "world/custommap.txt" : "world/scene_0" + (new Random().nextInt(6)) + ".txt";
        try {
            System.out.println(fileName);
            List<String> lines = Files.readAllLines(Path.of(fileName));
            for (int i = 0; i < Math.min(rows, lines.size()); i++) {
                grid[i] = lines.get(i).toCharArray();
            }
        } catch (IOException e) {
            System.err.println("Map load error: " + e.getMessage());
        }
    }

    private void loadMapTiles(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                tileGrid[i][j] = getTile(grid[i][j], j, i);
            }
        }
    }

    private Tile getTile(char tileChar, int x, int y){
        Tile tile;

        tile = switch(tileChar){
            case 'X' -> new Brick( (double) x, (double) y, "brick", GameConfig.BRICK_HP);
            case 'Y' -> new Steel( (double) x, (double) y, "steel", GameConfig.STEEL_HP); // get rekt
            case 'T' -> new Tile(x, y, "tree", false);
            case 'E' -> new Eagle(x, y, "eagle", GameConfig.EAGLE_HP);
            case 'W' -> new Tile(x, y, "water", true);
            default -> new Tile(x, y, "black", false);
        };

        return tile;
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

    public Tile[][] getGridTiles(){
        return tileGrid;
    }

    public void removeBlock(int x, int y) {
        
        if(tileGrid[y][x] instanceof Eagle)
            return;

        if (y >= 0 && y < rows && x >= 0 && x < cols) {
            if (grid[y][x] == 'X') {
                grid[y][x] = ' ';
            }
        }

        tileGrid[y][x] = new Tile(x, y, "black", false);
        
    }

     public void damageBlock(double x, double y, int damage) {
        int gridX = (int) (x / GameConfig.TILE_SIZE);
        int gridY = (int) (y / GameConfig.TILE_SIZE);

        if (gridY >= 0 && gridY < rows && gridX >= 0 && gridX < cols) {
            Tile tile = tileGrid[gridY][gridX];

            if(tile instanceof Breakable){

            
                Breakable b_tile = (Breakable) tile;


                b_tile.inflictDamage(damage);

                if(b_tile.isBroken()){
                    removeBlock(gridX, gridY);
                }
                    
            }

        }
    }


}
