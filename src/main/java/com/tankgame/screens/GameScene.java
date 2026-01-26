package com.tankgame.screens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.entities.projectile.Bullet;
import com.tankgame.game.GameGrid;
import com.tankgame.game.GameTick;
import com.tankgame.graphics.Renderer;
import com.tankgame.graphics.SpriteImport;
import com.tankgame.settings.Globals;
import com.tankgame.utils.Direction;

public class GameScene extends JPanel {
    private JPanel gameGrid;
    public GameGrid gridLogic;
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    public Renderer renderer;
    private SpriteImport sprites;
    public JFrame mainWindow;

    public GameScene(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.sprites = new SpriteImport();
        this.renderer = new Renderer(sprites);
        this.gridLogic = new GameGrid(true, Globals.GRID_HEIGHT, Globals.GRID_WIDTH);

        this.player = new Player(Globals.TILE_SIZE, Globals.TILE_SIZE, 3, "player_tank", Direction.UP);

        // Adicionando inimigos
        enemies.add(new Enemy(Globals.TILE_SIZE * 8, Globals.TILE_SIZE * 1, 1, "player_tank", Direction.DOWN));
        enemies.add(new Enemy(Globals.TILE_SIZE * 9, Globals.TILE_SIZE * 1, 1, "player_tank", Direction.DOWN));
        enemies.add(new Enemy(Globals.TILE_SIZE * 10, Globals.TILE_SIZE * 1, 1, "player_tank", Direction.DOWN));

        this.gameGrid = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                renderer.draw(g, gridLogic, player, enemies);
            }
        };

        int worldWidth = Globals.GRID_WIDTH * Globals.TILE_SIZE;
        int worldHeight = Globals.GRID_HEIGHT * Globals.TILE_SIZE;
        this.gameGrid.setPreferredSize(new Dimension(worldWidth, worldHeight));

        this.add(gameGrid);
        mainWindow.add(this);

        new Thread(new GameTick(this)).start();
    }

    public void update() {
        this.gameGrid.repaint();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
