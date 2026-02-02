package com.tankgame.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankgame.entities.tank.Enemy;
import com.tankgame.entities.tank.Player;
import com.tankgame.game.GameEngine;
import com.tankgame.game.GameGrid;
import com.tankgame.game.GameManager;
import com.tankgame.graphics.Renderer;
import com.tankgame.graphics.SpriteImport;
import com.tankgame.managers.FontManager;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

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

    private List<Object[]> bulletsToRender = new ArrayList<>();

    private JLabel healthpointLabel;
    private JLabel livesLabel;
    private JLabel scoreLabel;
    private JLabel timerLabel;

    private Font pauseFont = new Font("Arial", Font.BOLD, 48);
    private Font instructionFont = new Font("Arial", Font.PLAIN, 20);

    public GameScene(Consumer<String> onAction, JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.sprites = new SpriteImport();
        this.renderer = new Renderer(sprites);
        this.gridLogic = new GameGrid(true, GameConfig.GRID_HEIGHT, GameConfig.GRID_WIDTH);

        setBackground(Color.DARK_GRAY);

        this.player = new Player(
                GameConfig.PLAYER_START_X,
                GameConfig.PLAYER_START_Y,
                GameConfig.PLAYER_START_HEALTH,
                "tank_up_gray",
                Direction.UP,
                TankColors.GREEN
            );

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
                if(!gameManager.isRunning()){
                    drawGameOverText(g);
                }
            }
        };

        int worldWidth = GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE;
        int worldHeight = GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE;
        this.gameGrid.setPreferredSize(new Dimension(worldWidth, worldHeight));
        this.gameGrid.setBackground(Color.BLACK);

        this.add(gameGrid);

        this.pixel = FontManager.getFont("pixel", 54f);

        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(0, 2));

        JLabel healthpointLabelTitle = new JLabel("HP: ");
        healthpointLabelTitle.setFont(pixel);
        healthpointLabelTitle.setForeground(Color.WHITE);
        healthpointLabel = new JLabel("000");
        healthpointLabel.setForeground(Color.WHITE);
        healthpointLabel.setFont(pixel);
        

        JLabel livesLabelTitle = new JLabel("LIVES: ");
        livesLabelTitle.setFont(pixel);
        livesLabelTitle.setForeground(Color.WHITE);
        livesLabel = new JLabel("111");
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(pixel);


        JLabel scoreLabelTitle = new JLabel("SCORE: ");
        scoreLabelTitle.setFont(pixel);
        scoreLabelTitle.setForeground(Color.WHITE);
        scoreLabel = new JLabel("222");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(pixel);

        JLabel timerLabelTitle = new JLabel("TIME: ");
        timerLabelTitle.setFont(pixel);
        timerLabelTitle.setForeground(Color.WHITE);
        timerLabel = new JLabel("333");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(pixel);

        JButton backButton = new JButton("back");
        backButton.setFont(FontManager.getFont("pixel", 25f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> onAction.accept("start"));

        
        stats.add(timerLabelTitle);
        stats.add(timerLabel);
        stats.add(scoreLabelTitle);
        stats.add(scoreLabel);
        stats.add(healthpointLabelTitle);
        stats.add(healthpointLabel);
        stats.add(livesLabelTitle);
        stats.add(livesLabel);
        stats.add(backButton);

        stats.setBackground(Color.DARK_GRAY);

        this.add(stats);

        mainWindow.add(this);

        this.gameGrid.setFocusable(true);
        this.gameGrid.requestFocusInWindow();

        this.gameEngine = new GameEngine(this);
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

        g.setFont(pixel);
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

    public JLabel getStatHPLabel() {
        return healthpointLabel;
    }

    public JLabel getStatLivesLabel() {
        return livesLabel;
    }

    public JLabel getStatScoreLabel() {
        return scoreLabel;
    }

    public JLabel getStatTimerLabel() {
        return timerLabel;
    }

    public void startGame() {
        if (gameEngine == null) {
            this.gameEngine = new GameEngine(this);
            this.gameManager = gameEngine.getGameManager();
        }
        this.gameEngine.start();
        System.out.println("GameScene started");
        this.gameGrid.requestFocusInWindow();
    }

    public void stopGame() {
        if (gameEngine == null) {
            this.gameEngine = new GameEngine(this);
            this.gameManager = gameEngine.getGameManager();
        }
        this.gameEngine.stop();
        this.gameGrid.requestFocusInWindow();
    }
}
