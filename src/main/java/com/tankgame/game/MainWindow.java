package com.tankgame.game;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.screens.GameScene;
import com.tankgame.screens.InstructionsScreen;
import com.tankgame.screens.MapCreatorScreen;
import com.tankgame.screens.OnlineLobbyScreen;
import com.tankgame.screens.OptionsScreen;
import com.tankgame.screens.StartScreen;
import com.tankgame.settings.GameConfig;

public class MainWindow {
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel root;
    private GameScene gameScene; // Keep the field, but don't initialize it here

    public MainWindow() {
        frame = new JFrame("Tank Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);

        // Don't create GameScene here anymore
        // gameScene = new GameScene(this::handleScreenAction, frame);

        StartScreen startScreen = new StartScreen(this::handleScreenAction);
        OptionsScreen optionsScreen = new OptionsScreen(this::handleScreenAction);
        MapCreatorScreen mapCreatorScreen = new MapCreatorScreen(this::handleScreenAction);
        InstructionsScreen instructionsScreen = new InstructionsScreen(this::handleScreenAction);
        OnlineLobbyScreen onlineScreen = new OnlineLobbyScreen(this::handleScreenAction);


        // inserting new screens is painful
        root.add(startScreen, "start");
        // root.add(gameScene, "game"); // Don't add it here
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
        switch (action) {
            case "game" -> showGameScreen();
            case "options" -> showOptionsScreen();
            case "mapcreator" -> showMapCreatorScreen();
            case "start" -> showStartScreen();
            case "instructions" -> showInstructionScreen();
            case "online" -> showOnlineScreen();
            default -> System.out.println("Unknown action: " + action);
        }
    }

    protected void showStartScreen() {
        cardLayout.show(root, "start");
    }

    private void showGameScreen() {
        // Create a new GameScene instance every time
        gameScene = new GameScene(this::handleScreenAction, frame);
        root.add(gameScene, "game"); // Add the new scene to the layout
        cardLayout.show(root, "game");
        gameScene.startGame(); // Start the new game
    }

    private void showOptionsScreen() {
        cardLayout.show(root, "options");
    }

    private void showMapCreatorScreen(){
        cardLayout.show(root, "mapcreator");
    }

    private void showInstructionScreen(){
        cardLayout.show(root, "instructions");
    }

    private void showOnlineScreen(){
        cardLayout.show(root, "online");
    }
}
