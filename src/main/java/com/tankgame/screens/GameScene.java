package com.tankgame.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.game.GameEngine;
import com.tankgame.game.GameGrid;
import com.tankgame.game.GameManager;
import com.tankgame.graphics.Renderer;
import com.tankgame.graphics.SpriteImport;
import com.tankgame.managers.FontManager;
import com.tankgame.screens.widgets.StatBoardWidget;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;
import com.tankgame.entities.projectile.Bullet;

public class GameScene extends JPanel {
    private JPanel gameGrid;
    public GameGrid gridLogic;
    private Player player;
    public Renderer renderer;
    private SpriteImport sprites;
    public JFrame mainWindow;
    private GameEngine gameEngine;
    private GameManager gameManager;
    protected Font pixel;
    protected StatBoardWidget statWidget;
    private int difficulty;

    private Font pauseFont = new Font("Arial", Font.BOLD, 48);
    private Font instructionFont = new Font("Arial", Font.PLAIN, 20);

    public GameScene(Consumer<String> onAction, JFrame mainWindow, int difficulty, int mapId) {
        this.difficulty = difficulty;
        this.mainWindow = mainWindow;
        this.sprites = new SpriteImport();
        this.renderer = new Renderer(sprites);
        this.gridLogic = new GameGrid(mapId, GameConfig.GRID_HEIGHT, GameConfig.GRID_WIDTH);

        setBackground(Color.DARK_GRAY);

        // This will explode if a custom map doesn't have a 'P' on it
        int playerSpawnX = gridLogic.getPlayerSpawnXY()[0];
        int playerSpawnY = gridLogic.getPlayerSpawnXY()[1];

        this.player = new Player(
                playerSpawnX,
                playerSpawnY,
                GameConfig.PLAYER_START_HEALTH,
                "tank_up_gray",
                Direction.UP,
                TankColors.GRAY);

        this.gameGrid = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                List<Enemy> enemies = gameManager != null ? gameManager.getEnemyManager().getEnemies()
                        : new ArrayList<>();
                List<Bullet> bullets = gameManager != null ? gameManager.getProjectileManager().getActiveBullets()
                        : new ArrayList<>();

                renderer.draw(g, gridLogic, player, enemies, bullets);

                if (gameManager != null && gameManager.isPaused()) {
                    drawPauseText(g);
                }
                if (gameManager != null && !gameManager.isRunning()) {
                    drawGameOverText(g);
                }
            }
        };
        int worldWidth = GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE;
        int worldHeight = GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE;
        this.gameGrid.setPreferredSize(new Dimension(worldWidth, worldHeight));
        this.gameGrid.setBackground(Color.BLACK);

        this.add(gameGrid);

        statWidget = new StatBoardWidget(action -> {
            onAction.accept("start");
        });

        add(statWidget);

        mainWindow.add(this);

        this.gameGrid.setFocusable(true);
        this.gameGrid.requestFocusInWindow();

        this.gameEngine = new GameEngine(this, difficulty);
        this.gameManager = gameEngine.getGameManager();

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

    private void drawGameOverText(Graphics g) {
        Color originalColor = g.getColor();
        Font originalFont = g.getFont();

        g.setFont(FontManager.getFont("pixel", 52));
        g.setColor(Color.RED);

        String pauseText = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(pauseText);
        int x = (gameGrid.getWidth() - textWidth) / 2;
        int y = gameGrid.getHeight() / 2;

        g.drawString(pauseText, x, y);

        g.setFont(instructionFont);
        g.setColor(Color.YELLOW);

        String instruction = "YOU SCORED " + gameManager.getScore() + " POINTS";
        int instWidth = g.getFontMetrics().stringWidth(instruction);
        int instX = (gameGrid.getWidth() - instWidth) / 2;
        int instY = y + 50;
        g.setColor(Color.YELLOW);

        g.drawString(instruction, instX, instY);

    }

    public void update() {
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

    public void startGame() {
        if (gameEngine == null) {
            this.gameEngine = new GameEngine(this, this.difficulty);
            this.gameManager = gameEngine.getGameManager();
        }
        this.gameEngine.start();
        System.out.println("GameScene started");
        this.gameGrid.requestFocusInWindow();
    }

    public void stopGame() {
        if (gameEngine == null) {
            this.gameEngine = new GameEngine(this, this.difficulty);
            this.gameManager = gameEngine.getGameManager();
        }
        this.gameEngine.stop();
        this.gameGrid.requestFocusInWindow();
    }

    public StatBoardWidget getStatBoard() {
        return statWidget;
    }
}
