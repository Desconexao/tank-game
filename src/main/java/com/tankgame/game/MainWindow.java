package com.tankgame.game;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.screens.GameScene;
import com.tankgame.screens.InstructionsScreen;
import com.tankgame.screens.MapCreatorScreen;
import com.tankgame.screens.OptionsScreen;
import com.tankgame.screens.StartScreen;
import com.tankgame.screens.online.OnlineLobbyScreen;
import com.tankgame.settings.GameConfig;
import com.tankgame.screens.RankingScreen;

public class MainWindow {
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel root;
    private GameScene gameScene;
    private RankingScreen rankingScreen;

    public MainWindow() {
        frame = new JFrame("Tank Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);

        StartScreen startScreen = new StartScreen(this::handleScreenAction);
        OptionsScreen optionsScreen = new OptionsScreen(this::handleScreenAction);
        MapCreatorScreen mapCreatorScreen = new MapCreatorScreen(this::handleScreenAction);
        InstructionsScreen instructionsScreen = new InstructionsScreen(this::handleScreenAction);
        OnlineLobbyScreen onlineScreen = new OnlineLobbyScreen(this::handleScreenAction);
        rankingScreen = new RankingScreen(this::handleScreenAction);

        root.add(rankingScreen, "ranking");
        root.add(startScreen, "start");
        root.add(optionsScreen, "options");
        root.add(mapCreatorScreen, "mapcreator");
        root.add(instructionsScreen, "instructions");
        root.add(onlineScreen, "online");

        frame.setContentPane(root);
        cardLayout.show(root, "start");

        frame.setSize(GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE,
                GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        System.out.println("Window Created");
    }

    private void handleScreenAction(String action) {
        if (action.startsWith("game:")) {
            String[] parts = action.split(":");
            String playerName = parts.length > 1 ? parts[1] : "player_1";
            int difficulty = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            int mapId = parts.length > 3 ? Integer.parseInt(parts[3]) : 3;

            System.out.println("Name: " + playerName + " | Difficulty: " + difficulty + " | Map: " + mapId);

            showGameScreen(playerName, difficulty, mapId);
            return;
        }

        switch (action) {
            case "options" -> showOptionsScreen();
            case "mapcreator" -> showMapCreatorScreen();
            case "start" -> showStartScreen();
            case "instructions" -> showInstructionScreen();
            case "online" -> showOnlineScreen();
            case "ranking" -> showRankingScreen();
            default -> {
                System.out.println(" Back to menu:" + action);
                showStartScreen();
            }
        }

        root.requestFocusInWindow();
    }

    protected void showStartScreen() {
        cardLayout.show(root, "start");
    }

    private void showGameScreen(String playerName, int difficulty, int mapId) {
        if (gameScene != null) {
            root.remove(gameScene);
        }

        gameScene = new GameScene(this::handleScreenAction, frame, difficulty, mapId);
        gameScene.getGameManager().setPlayerName(playerName);

        root.add(gameScene, "game");
        root.revalidate();
        root.repaint();

        cardLayout.show(root, "game");
        gameScene.startGame();
    }

    private void showRankingScreen() {
        rankingScreen.refreshRanking();
        cardLayout.show(root, "ranking");
    }

    private void showOptionsScreen() {
        cardLayout.show(root, "options");
    }

    private void showMapCreatorScreen() {
        cardLayout.show(root, "mapcreator");
    }

    private void showInstructionScreen() {
        cardLayout.show(root, "instructions");
    }

    private void showOnlineScreen() {
        cardLayout.show(root, "online");
    }
}
