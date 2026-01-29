package com.tankgame.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.game.GameGrid;
import com.tankgame.game.GameEngine;
import com.tankgame.game.GameManager;
import com.tankgame.graphics.Renderer;
import com.tankgame.graphics.SpriteImport;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;

public class GameScene extends JPanel {
    private JPanel gameGrid;
    public GameGrid gridLogic;
    private Player player;
    public Renderer renderer;
    private SpriteImport sprites;
    public JFrame mainWindow;
    private GameEngine gameEngine;
    private GameManager gameManager;

    private List<Object[]> bulletsToRender = new ArrayList<>();

    private Font pauseFont = new Font("Arial", Font.BOLD, 48);
    private Font instructionFont = new Font("Arial", Font.PLAIN, 20);

    public GameScene(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.sprites = new SpriteImport();
        this.renderer = new Renderer(sprites);
        this.gridLogic = new GameGrid(true, GameConfig.GRID_HEIGHT, GameConfig.GRID_WIDTH);

        this.player = new Player(
                GameConfig.PLAYER_START_X,
                GameConfig.PLAYER_START_Y,
                GameConfig.PLAYER_START_HEALTH,
                "player_tank",
                Direction.UP);

        this.gameGrid = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                List<Enemy> enemies = gameManager != null ? gameManager.getEnemyManager().getEnemies()
                        : new ArrayList<>();
                renderer.draw(g, gridLogic, player, enemies, bulletsToRender);

                if (gameManager != null && gameManager.isPaused()) {
                    drawPauseText(g);
                }
            }
        };

        int worldWidth = GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE;
        int worldHeight = GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE;
        this.gameGrid.setPreferredSize(new Dimension(worldWidth, worldHeight));

        this.add(gameGrid);
        mainWindow.add(this);

        this.gameGrid.setFocusable(true);
        this.gameGrid.requestFocusInWindow();

        this.gameEngine = new GameEngine(this);
        this.gameManager = gameEngine.getGameManager();

        this.gameEngine.start();

        System.out.println("GameScene initialized");
    }

    private void drawPauseText(Graphics g) {
        Color originalColor = g.getColor();
        Font originalFont = g.getFont();

        g.setFont(pauseFont);
        g.setColor(Color.RED);

        String pauseText = "PAUSED";
        int textWidth = g.getFontMetrics().stringWidth(pauseText);
        int x = (gameGrid.getWidth() - textWidth) / 2;
        int y = gameGrid.getHeight() / 2;

        g.drawString(pauseText, x, y);

        g.setFont(instructionFont);
        g.setColor(Color.YELLOW);

        String instruction = "Press ESC to resume";
        int instWidth = g.getFontMetrics().stringWidth(instruction);
        int instX = (gameGrid.getWidth() - instWidth) / 2;
        int instY = y + 50;

        g.drawString(instruction, instX, instY);

        g.setColor(originalColor);
        g.setFont(originalFont);
    }

    public void update() {
        bulletsToRender.clear();

        if (gameManager != null && gameManager.getProjectileManager() != null) {
            for (var bullet : gameManager.getProjectileManager().getActiveBullets()) {
                bulletsToRender.add(new Object[] {
                        bullet.getSpriteKey(),
                        (int) bullet.getX(),
                        (int) bullet.getY()
                });
            }
        }

        this.gameGrid.repaint();
    }

    public JPanel getGameGrid() {
        return gameGrid;
    }

    public Player getPlayer() {
        return player;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void cleanup() {
        if (gameEngine != null) {
            gameEngine.stop();
        }
    }
}
