package com.tankgame.screens.online;

import java.awt.BorderLayout;
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
import com.tankgame.game.GameGrid;
import com.tankgame.game.online.OnlineGameEngine;
import com.tankgame.game.online.OnlineGridLoader;
import com.tankgame.game.online.OnlinePlayerInputHandler;
import com.tankgame.game.online.WebSocketClient;
import com.tankgame.graphics.Renderer;
import com.tankgame.graphics.SpriteImport;
import com.tankgame.managers.CollisionManager;
import com.tankgame.managers.FontManager;
import com.tankgame.settings.GameConfig;
import com.tankgame.utils.Direction;
import com.tankgame.utils.TankColors;

/**
 * Online game scene for multiplayer tank battles.
 * Similar to GameScene but uses OnlineGridLoader and manages a single opponent.
 */
public class OnlineGameScene extends JPanel {
    private JPanel gameGrid;
    public OnlineGridLoader gridLoader;
    private GameGrid wrappedGrid; // Persistent GameGrid for block breaking
    private Player player;
    private Enemy opponent;
    public Renderer renderer;
    private SpriteImport sprites;
    public JFrame mainWindow;
    private OnlinePlayerInputHandler inputHandler;
    private WebSocketClient webSocketClient;
    private OnlineGameEngine gameEngine;
    private com.tankgame.game.online.OnlineOpponentManager opponentManager;

    private Font pauseFont = new Font("Arial", Font.BOLD, 48);
    private Font instructionFont = new Font("Arial", Font.PLAIN, 20);
    private boolean isPaused = false;
    private boolean isGameRunning = true;

    public OnlineGameScene(Consumer<String> onAction, JFrame mainWindow, WebSocketClient webSocketClient, int playerNumber) {
        this.mainWindow = mainWindow;
        this.webSocketClient = webSocketClient;
        this.sprites = new SpriteImport();
        this.renderer = new Renderer(sprites);

        System.out.println("[GAME] Initializing as Player " + playerNumber);

        // Load online map with player spawn points
        this.gridLoader = new OnlineGridLoader(GameConfig.GRID_HEIGHT, GameConfig.GRID_WIDTH);

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Get player spawn points
        List<int[]> spawns = gridLoader.getPlayerSpawns();
        int[] playerSpawn;
        int[] opponentSpawn;
        
        // Player 1 gets first spawn, Player 2 gets second spawn
        if (playerNumber == 1) {
            playerSpawn = spawns.size() > 0 ? spawns.get(0) : new int[] { GameConfig.PLAYER_START_X, GameConfig.PLAYER_START_Y };
            opponentSpawn = spawns.size() > 1 ? spawns.get(1) : new int[] { GameConfig.PLAYER_START_X + 300, GameConfig.PLAYER_START_Y };
            System.out.println("[GAME] Player 1 (You) at spawn 0, Opponent at spawn 1");
        } else {
            playerSpawn = spawns.size() > 1 ? spawns.get(1) : new int[] { GameConfig.PLAYER_START_X + 300, GameConfig.PLAYER_START_Y };
            opponentSpawn = spawns.size() > 0 ? spawns.get(0) : new int[] { GameConfig.PLAYER_START_X, GameConfig.PLAYER_START_Y };
            System.out.println("[GAME] Player 2 (You) at spawn 1, Opponent at spawn 0");
        }

        // Create player tank
        this.player = new Player(
                playerSpawn[0],
                playerSpawn[1],
                GameConfig.PLAYER_START_HEALTH,
                "tank_up_gray",
                Direction.UP,
                TankColors.GRAY);

        this.wrappedGrid = new GameGrid(gridLoader);
        CollisionManager collisionManager = new CollisionManager(wrappedGrid);
        
        this.opponent = new Enemy(
            opponentSpawn[0],
            opponentSpawn[1],
            GameConfig.PLAYER_START_HEALTH,
            "enemy_tank",
            Direction.UP,
            TankColors.RED,
            Enemy.AIType.DEFAULT,
            collisionManager);
        this.opponent.AI = null;

        // Create input handler
        this.inputHandler = new OnlinePlayerInputHandler();

        // Setup message handler to receive opponent actions
        webSocketClient.setMessageHandler(new WebSocketClient.MessageHandler() {
            @Override
            public void onLobbyCreated(String id, int size, int capacity, int playerNumber) {
            }

            @Override
            public void onLobbyJoined(String id, int size, int capacity, int playerNumber) {
            }

            @Override
            public void onReady() {
            }

            @Override
            public void onGameStarted() {
            }

            @Override
            public void onEnemyInput(double x, double y, Direction facing) {
                // Handled by engine
            }

            @Override
            public void onEnemyShooting(boolean shooting) {
                // Handled by engine
            }

            @Override
            public void onError(String message) {
                System.err.println("Online error: " + message);
            }
        });

        // Create game grid panel
        this.gameGrid = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                List<com.tankgame.entities.projectile.Bullet> bullets = gameEngine != null ? gameEngine.getProjectileManager().getActiveBullets() : new ArrayList<>();
                renderer.draw(g, wrappedGrid, player, new ArrayList<>(List.of(opponent)), bullets, new ArrayList<>());

                if (isPaused) {
                    drawPauseText(g);
                }
                if (!isGameRunning) {
                    drawGameOverText(g);
                }
            }
        };

        int worldWidth = GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE;
        int worldHeight = GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE;
        this.gameGrid.setPreferredSize(new Dimension(worldWidth, worldHeight));
        this.gameGrid.setMinimumSize(new Dimension(worldWidth, worldHeight));
        this.gameGrid.setSize(new Dimension(worldWidth, worldHeight));
        this.gameGrid.setBackground(Color.BLACK);

        // Add input handler
        this.gameGrid.addKeyListener(inputHandler);

        this.add(gameGrid, BorderLayout.CENTER);

        this.gameGrid.setFocusable(true);
        this.gameGrid.requestFocusInWindow();

        // Start game engine
        this.gameEngine = new OnlineGameEngine(this, webSocketClient);
        this.opponentManager = gameEngine.getOpponentManager();
        this.opponentManager.setOpponent(opponent); // Link the opponent tank
        gameEngine.start();

        // Start repaint timer
        javax.swing.Timer repaintTimer = new javax.swing.Timer(1000 / 60, e -> gameGrid.repaint());
        repaintTimer.start();

        System.out.println("OnlineGameScene initialized");
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

        String gameOverText = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        int x = (gameGrid.getWidth() - textWidth) / 2;
        int y = gameGrid.getHeight() / 2;

        g.drawString(gameOverText, x, y);

        g.setFont(instructionFont);
        g.setColor(Color.YELLOW);

        String instruction = "Click to return to menu";
        int instWidth = g.getFontMetrics().stringWidth(instruction);
        int instX = (gameGrid.getWidth() - instWidth) / 2;
        int instY = y + 50;

        g.drawString(instruction, instX, instY);

        g.setColor(originalColor);
        g.setFont(originalFont);
    }



    public void update() {
        gameGrid.repaint();
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getOpponent() {
        return opponent;
    }

    public OnlineGridLoader getGridLoader() {
        return gridLoader;
    }

    public OnlinePlayerInputHandler getInputHandler() {
        return inputHandler;
    }

    public void cleanup() {
        webSocketClient.disconnect();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setGameRunning(boolean running) {
        isGameRunning = running;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public JPanel getGameGrid() {
        return gameGrid;
    }

    public GameGrid getWrappedGrid() {
        return wrappedGrid;
    }
}
