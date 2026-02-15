package com.tankgame.screens.online;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.tankgame.game.online.WebSocketClient;
import com.tankgame.managers.FontManager;
import com.tankgame.settings.ServerConfig;

public class OnlineLobbyScreen extends JPanel {
    private WebSocketClient webSocketClient;
    private JTextField lobbyIdField;
    private JLabel lobbyInfoLabel;
    private JLabel statusLabel;
    private Consumer<String> onAction;
    private String currentLobbyId;
    private int playerNumber = 1; // Default to player 1

    public OnlineLobbyScreen(Consumer<String> onAction) {
        this.onAction = onAction;
        this.webSocketClient = new WebSocketClient(ServerConfig.getCurrentServerUrl());
        
        // Set up message handler
        webSocketClient.setMessageHandler(getMessageHandler());
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("online shit", SwingConstants.CENTER);
        title.setFont(FontManager.getFont("pixel", 54f));
        title.setForeground(Color.WHITE);

        // Center panel with lobby join UI
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBackground(Color.BLACK);
        
        JLabel lobbyLabel = new JLabel("Lobby ID:");
        lobbyLabel.setFont(FontManager.getFont("pixel", 25f));
        lobbyLabel.setForeground(Color.WHITE);
        
        lobbyIdField = new JTextField(4);
        lobbyIdField.setFont(FontManager.getFont("pixel", 25f));
        
        JButton joinButton = new JButton("Join");
        joinButton.setFont(FontManager.getFont("pixel", 25f));
        joinButton.setForeground(Color.WHITE);
        joinButton.setBackground(Color.BLACK);
        joinButton.setFocusPainted(false);
        joinButton.setBorderPainted(false);
        joinButton.setContentAreaFilled(false);
        joinButton.addActionListener(e -> {
            String lobbyId = lobbyIdField.getText();
            if (lobbyId.length() == 4) {
                try {
                    // Create fresh WebSocketClient with current server config
                    webSocketClient = new WebSocketClient(ServerConfig.getCurrentServerUrl());
                    webSocketClient.setMessageHandler(getMessageHandler());
                    webSocketClient.connect();
                    webSocketClient.joinLobby(lobbyId);
                } catch (Exception ex) {
                    statusLabel.setText("Cannot connect to server at " + ServerConfig.getServerAddress());
                    statusLabel.setForeground(Color.RED);
                }
            }
        });
        
        JButton createButton = new JButton("Create Lobby");
        createButton.setFont(FontManager.getFont("pixel", 25f));
        createButton.setForeground(Color.WHITE);
        createButton.setBackground(Color.BLACK);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setContentAreaFilled(false);
        createButton.addActionListener(e -> {
            try {
                // Create fresh WebSocketClient with current server config
                webSocketClient = new WebSocketClient(ServerConfig.getCurrentServerUrl());
                webSocketClient.setMessageHandler(getMessageHandler());
                webSocketClient.connect();
                webSocketClient.createLobby();
            } catch (Exception ex) {
                statusLabel.setText("Cannot connect to server at " + ServerConfig.getServerAddress());
                statusLabel.setForeground(Color.RED);
            }
        });
        
        centerPanel.add(createButton);
        centerPanel.add(lobbyLabel);
        centerPanel.add(lobbyIdField);
        centerPanel.add(joinButton);
        
        // Lobby info display
        lobbyInfoLabel = new JLabel("");
        lobbyInfoLabel.setFont(FontManager.getFont("pixel", 20f));
        lobbyInfoLabel.setForeground(Color.GREEN);
        lobbyInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(FontManager.getFont("pixel", 18f));
        statusLabel.setForeground(Color.CYAN);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(lobbyInfoLabel, BorderLayout.NORTH);
        infoPanel.add(statusLabel, BorderLayout.CENTER);

        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with lobby info and back button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.add(infoPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("back");
        backButton.setFont(FontManager.getFont("pixel", 25f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> onAction.accept("start"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Transition from lobby screen to online game scene
     */
    private void startOnlineGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            OnlineGameScene gameScene = new OnlineGameScene(onAction, frame, webSocketClient, playerNumber);
            frame.setContentPane(gameScene);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Resize frame to fit content
            frame.setLocationRelativeTo(null); // Center on screen
            frame.revalidate();
            frame.repaint();
            gameScene.getGameGrid().requestFocusInWindow();
        }
    }

    private WebSocketClient.MessageHandler getMessageHandler() {
        return new WebSocketClient.MessageHandler() {
            @Override
            public void onLobbyCreated(String id, int size, int capacity, int playerNumber) {
                currentLobbyId = id;
                OnlineLobbyScreen.this.playerNumber = playerNumber;
                System.out.println("[LOBBY] You are Player " + playerNumber);
                SwingUtilities.invokeLater(() -> {
                    lobbyInfoLabel.setText(String.format("Lobby: %s | Players: %d/%d | You: P%d", id, size, capacity, playerNumber));
                    statusLabel.setText("Waiting for players...");
                    statusLabel.setForeground(Color.CYAN);
                });
            }
            
            @Override
            public void onLobbyJoined(String id, int size, int capacity, int playerNumber) {
                currentLobbyId = id;
                OnlineLobbyScreen.this.playerNumber = playerNumber;
                System.out.println("[LOBBY] You are Player " + playerNumber);
                SwingUtilities.invokeLater(() -> {
                    lobbyInfoLabel.setText(String.format("Lobby: %s | Players: %d/%d | You: P%d", id, size, capacity, playerNumber));
                    statusLabel.setText("Joined lobby! Waiting...");
                    statusLabel.setForeground(Color.YELLOW);
                });
            }
            
            @Override
            public void onReady() {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("READY! Starting game...");
                    statusLabel.setForeground(Color.GREEN);
                    
                    // Tell server to start the game
                    webSocketClient.startLobby(currentLobbyId);
                    
                    // Transition to game after a short delay
                    Timer timer = new Timer(1000, e -> startOnlineGame());
                    timer.setRepeats(false);
                    timer.start();
                });
            }
            
            @Override
            public void onGameStarted() {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Game started!");
                    statusLabel.setForeground(Color.WHITE);
                });
            }

            @Override
            public void onEnemyInput(double x, double y, com.tankgame.utils.Direction facing) {
                // Not used in lobby screen
            }

            @Override
            public void onEnemyShooting(boolean shooting) {
                // Not used in lobby screen
            }

            @Override
            public void onError(String message) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error: " + message);
                    statusLabel.setForeground(Color.RED);
                });
            }
        };
    }
}