package com.tankgame.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

// import java.awt.RenderingHints;
import javax.swing.ImageIcon;

import com.tankgame.entities.tank.Tank;
import com.tankgame.game.GameGrid;
import com.tankgame.settings.Globals;

public class Renderer {
    private SpriteImport sprites;
    private int size = Globals.TILE_SIZE;
    private int bulletSize = Globals.BULLET_SIZE;

    private ImageIcon playerUp, playerDown, playerLeft, playerRight;
    private ImageIcon brick, blackSquare;
    private ImageIcon bulletVertical;
    
    public Map<String, ImageIcon> loadedSprites = new HashMap<>();

    // Render queue will store a array of sprites to render each frame, so GameTick can, dynamically, insert sprites into the scene aswell.
    // [[SpriteName, coordinate X, coordinate Y], [...]]
    private Queue<Object[]> renderQueue = new ArrayDeque<>();
    

    public Renderer(SpriteImport sprites) {
        this.sprites = sprites;

        // I suggest to deprecate this way of loading sprites.
        playerUp = sprites.getSpriteResized("player_tank", size, size);
        playerDown = sprites.getSpriteResized("player_tank_down", size, size);
        playerLeft = sprites.getSpriteResized("player_tank_left", size, size);
        playerRight = sprites.getSpriteResized("player_tank_right", size, size);
        brick = sprites.getSpriteResized("brick", size, size);
        blackSquare = sprites.getSpriteResized("black", size, size);
        bulletVertical = sprites.getSpriteResized("bullet_vertical", 30, 30);

        // Suggest using this one.
        Object[][] spritesToLoad = {
            {"player_tank",         size,           size},
            {"player_tank_down",    size,           size},
            {"player_tank_left",    size,           size},
            {"player_tank_right",   size,           size},
            {"brick",               size,           size},
            {"black",               size,           size},
            {"bullet_vertical",     bulletSize,     bulletSize}
        };

        loadSprites(spritesToLoad);
    }

    public void draw(Graphics g, GameGrid gameGridLogic, Tank player) {
        Graphics2D g2d = (Graphics2D) g;

        // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        char[][] grid = gameGridLogic.getGridMatrix();
        int size = Globals.TILE_SIZE;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ImageIcon icon = (grid[i][j] == 'X') ? brick : blackSquare;
                g2d.drawImage(icon.getImage(), j * size, i * size, null);
            }
        }

        ImageIcon tankIcon = getSpriteByKey(player.getSpriteKey());
        g2d.drawImage(tankIcon.getImage(), (int) player.getX(), (int) player.getY(), null);

        drawSpriteQueue(g2d);
    }

    private ImageIcon getSpriteByKey(String spriteKey) {
        return switch (spriteKey) {
            case "player_tank_down" -> playerDown;
            case "player_tank_left" -> playerLeft;
            case "player_tank_right" -> playerRight;
            case "brick" -> brick;
            case "black" -> blackSquare;
            default -> playerUp;
        };
    }

    private void loadSprites(Object[][] spriteSpecs){
        for (Object[] spec : spriteSpecs) {
            if (spec.length < 3) {
                continue;
            }

            String key = (String) spec[0];
            int resX = (int) spec[1];
            int resY = (int) spec[2];
            loadedSprites.put(key, sprites.getSpriteResized(key, resX, resY));
        }
    }

    public void drawSpriteQueue(Graphics2D g2d){
        while (!renderQueue.isEmpty()) {
            Object[] spriteInfo = renderQueue.poll(); // removes first
            
            ImageIcon spriteIcon = loadedSprites.get((String) spriteInfo[0]);
            int X = (int) spriteInfo[1];
            int Y = (int) spriteInfo[2];
            

            g2d.drawImage(spriteIcon.getImage(), X, Y, null);
        }
    }

    // Simple Queue PUSH method
    public boolean pushRenderQueue(String spriteName, int X, int Y){
        if (loadedSprites.containsKey(spriteName)){
            renderQueue.add(new Object[]{ spriteName, X, Y });
            return true;
        }
        return false;
    }

    // Simple queue POP
    public Object[] popRenderQueue(){
        return renderQueue.poll();
    }
}
