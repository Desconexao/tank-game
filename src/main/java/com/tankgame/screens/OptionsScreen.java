package com.tankgame.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.tankgame.managers.FontManager;
import com.tankgame.settings.ServerConfig;

public class OptionsScreen extends JPanel {

    public OptionsScreen(Consumer<String> onAction) {

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("options stuff", SwingConstants.CENTER);
        title.setFont(FontManager.getFont("pixel", 54f));
        title.setForeground(Color.WHITE);

        JPanel serverPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        serverPanel.setBackground(Color.BLACK);

        JLabel serverLabel = new JLabel("Server:");
        serverLabel.setFont(FontManager.getFont("pixel", 20f));
        serverLabel.setForeground(Color.WHITE);

        JTextField serverField = new JTextField(ServerConfig.getServerAddress(), 20);
        serverField.setFont(FontManager.getFont("pixel", 18f));
        serverField.setForeground(Color.BLACK);
        serverField.setBackground(Color.WHITE);

        JButton testConnectionButton = new JButton("Test Connection");
        testConnectionButton.setFont(FontManager.getFont("pixel", 18f));
        testConnectionButton.setForeground(Color.WHITE);
        testConnectionButton.setBackground(Color.DARK_GRAY);
        testConnectionButton.setFocusPainted(false);
        testConnectionButton.setBorderPainted(false);

        JLabel connectionStatusLabel = new JLabel("Status: Not tested");
        connectionStatusLabel.setFont(FontManager.getFont("pixel", 16f));
        connectionStatusLabel.setForeground(Color.YELLOW);

        testConnectionButton.addActionListener(e -> {
            connectionStatusLabel.setText("Status: Testing...");
            connectionStatusLabel.setForeground(Color.YELLOW);
            new Thread(() -> {
                try {
                    String serverUrl = "ws://" + serverField.getText().trim();
                    testConnection(serverUrl, connectionStatusLabel);
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        connectionStatusLabel.setText("Status: \u2717 Connection Failed");
                        connectionStatusLabel.setForeground(Color.RED);
                    });
                }
            }).start();
        });

        JButton setServerButton = new JButton("Set Server");
        setServerButton.setFont(FontManager.getFont("pixel", 18f));
        setServerButton.setForeground(Color.WHITE);
        setServerButton.setBackground(Color.DARK_GRAY);
        setServerButton.setFocusPainted(false);
        setServerButton.setBorderPainted(false);
        setServerButton.addActionListener(e -> {
            String newServer = serverField.getText().trim();
            if (!newServer.isEmpty()) {
                ServerConfig.setServerAddress(newServer);
                serverField.setText(ServerConfig.getServerAddress());
            }
        });

        JButton resetServerButton = new JButton("Reset to Default");
        resetServerButton.setFont(FontManager.getFont("pixel", 16f));
        resetServerButton.setForeground(Color.WHITE);
        resetServerButton.setBackground(Color.DARK_GRAY);
        resetServerButton.setFocusPainted(false);
        resetServerButton.setBorderPainted(false);
        resetServerButton.addActionListener(e -> {
            ServerConfig.resetToDefault();
            serverField.setText(ServerConfig.getServerAddress());
            connectionStatusLabel.setText("Status: Not tested");
            connectionStatusLabel.setForeground(Color.YELLOW);
        });

        serverPanel.add(serverLabel);
        serverPanel.add(serverField);
        serverPanel.add(testConnectionButton);
        serverPanel.add(setServerButton);
        serverPanel.add(resetServerButton);

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.BLACK);
        statusPanel.add(connectionStatusLabel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(serverPanel, BorderLayout.NORTH);
        centerPanel.add(statusPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("back");
        backButton.setFont(FontManager.getFont("pixel", 25f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> onAction.accept("start"));

        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void testConnection(String serverUrl, JLabel statusLabel) {
        try {
            String hostPort = serverUrl.replace("ws://", "").replace("wss://", "");
            String[] parts = hostPort.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            java.net.Socket socket = new java.net.Socket();
            socket.connect(new java.net.InetSocketAddress(host, port), 3000);
            socket.close();

            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: \u2713 Connected Successfully");
                statusLabel.setForeground(new Color(0, 200, 0));
            });
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: \u2717 Connection Failed (" + ex.getClass().getSimpleName() + ")");
                statusLabel.setForeground(Color.RED);
            });
        }
    }
}
