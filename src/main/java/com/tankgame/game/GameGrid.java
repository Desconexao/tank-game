package com.tankgame.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tankgame.entities.tile.Brick;
import com.tankgame.entities.tile.Eagle;
import com.tankgame.entities.tile.Steel;
import com.tankgame.entities.tile.Tile;
import com.tankgame.entities.tile.Tree;
import com.tankgame.entities.tile.Water;
import com.tankgame.game.online.OnlineGridLoader;
import com.tankgame.settings.GameConfig;

public class GameGrid {
    private final int rows, cols;
    private int mapId;
    private Tile[][] tileGrid;
    private Eagle EagleObjective;
    private int[] eagleCoord;
    private int[] playerSpawnXY;
    private List<List<Integer>> enemySpawnXY = new ArrayList<>();

    public GameGrid(int mapId, int rows, int cols) {
        this.mapId = mapId;
        this.rows = rows;
        this.cols = cols;
        this.tileGrid = new Tile[rows][cols];

        loadMap(mapId);
    }

    public GameGrid(OnlineGridLoader loader) {
        this.rows = GameConfig.GRID_HEIGHT;
        this.cols = GameConfig.GRID_WIDTH;
        this.tileGrid = loader.getTileGrid();
        this.EagleObjective = loader.getEagle();
        this.playerSpawnXY = new int[] { 0, 0 };
    }

    private void loadMap(int mapId) {
        if (mapId == 3) {
            mapId = new Random().nextInt(3);
        }
        String fileName = "world/scene_0" + mapId + ".txt";
        try {
            System.out.println("Loading map: " + fileName);
            List<String> lines = loadLinesFromResource(fileName);
            for (int i = 0; i < Math.min(rows, lines.size()); i++) {
                char[] rowChars = lines.get(i).toCharArray();
                for (int j = 0; j < Math.min(cols, rowChars.length); j++) {

                    char tileChar = rowChars[j];
                    Tile tile = createTileFromChar(tileChar, j * GameConfig.TILE_SIZE, i * GameConfig.TILE_SIZE);

                    tileGrid[i][j] = tile;
                    if (tileChar == 'E') {
                        setEagleObjective((Eagle) tile, new int[]{i, j});
                    }
                    if (tileChar == 'P') {
                        playerSpawnXY = new int[] { j * GameConfig.TILE_SIZE, i * GameConfig.TILE_SIZE };
                    }
                    if (tileChar == 'S') {
                        enemySpawnXY.add(new ArrayList<>(List.of(j * GameConfig.TILE_SIZE, i * GameConfig.TILE_SIZE)));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Map load error: " + e.getMessage());
        }
    }

    private List<String> loadLinesFromResource(String resourcePath) throws IOException {
        List<String> lines = new ArrayList<>();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);

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

    private Tile createTileFromChar(char tileChar, int x, int y) {
        return switch (tileChar) {
            case 'X' -> new Brick(x, y, "brick", GameConfig.BRICK_HP);
            case 'Y' -> new Steel(x, y, "steel", GameConfig.STEEL_HP);
            case 'T' -> new Tree(x, y, "tree", false);
            case 'E' -> new Eagle(x, y, "eagle", GameConfig.EAGLE_HP);
            case 'W' -> new Water(x, y, "water", true);
            default -> new Tile(x, y, "black", false);
        };
    }

    public Tile[][] getGridTiles() {
        return tileGrid;
    }

    public void removeBlock(int x, int y) {
        tileGrid[y][x] = new Tile(x * GameConfig.TILE_SIZE, y * GameConfig.TILE_SIZE, "black", false);
    }

    public void damageBlock(double x, double y, int damage) {
        int gridX = (int) (x / GameConfig.TILE_SIZE);
        int gridY = (int) (y / GameConfig.TILE_SIZE);

        if (gridY >= 0 && gridY < rows && gridX >= 0 && gridX < cols) {
            Tile tile = tileGrid[gridY][gridX];

            tile.inflictDamage(damage);

            if (tile.isBroken() && tile.isRemovableWhenBroken()) {
                removeBlock(gridX, gridY);
            }
        }
    }

    public Eagle getEagleObjective() {
        return this.EagleObjective;
    }

    public void setEagleObjective(Eagle eagleObjective, int[] coords) {
        EagleObjective = eagleObjective;
        int x = coords[0];
        int y = coords[1];
        this.eagleCoord = new int[]{x, y};
    }

    public int[] getEagleCoords(){
        return eagleCoord;
    }

    public int[] getPlayerSpawnXY(){
        return this.playerSpawnXY;
    }

    public List<List<Integer>> getEnemySpawnXY() {
        return this.enemySpawnXY;
    }

    public void setPlayerSpawn(int x, int y) {
        this.playerSpawnXY = new int[] { x, y };
    }

    public void protectEagleTile() {
        int[] pos = getEagleCoords();
        int row = pos[0];
        int col = pos[1];

        int[][] tilesToProtect = {
                {row, col + 1},
                {row - 1, col},
                {row, col - 1},
                {row + 1, col}
        };

        for (int i = 0; i < 4; i++) {
            int r = tilesToProtect[i][0];
            int c = tilesToProtect[i][1];
            Steel steelBlock = new Steel(c * GameConfig.TILE_SIZE, r * GameConfig.TILE_SIZE, "steel", GameConfig.STEEL_HP);
            replaceTile(steelBlock, r, c);
        }
    }

    public boolean replaceTile(Tile newTile, int x, int y) {
        if (x >= rows || x < 0 || y >= cols || y < 0) {
            return false;
        }

        tileGrid[x][y] = newTile;

        return true;
    }

}
