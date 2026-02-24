package com.tankgame.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.tankgame.managers.FontManager;

public class StartScreen extends JPanel {

    private JTextField nameField;
    private JComboBox<String> difficultyBox;
    private JComboBox<String> mapBox;

    public StartScreen(Consumer<String> onAction) {

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("TANK GAME", SwingConstants.CENTER);
        title.setFont(FontManager.getFont("pixel", 54f));
        title.setForeground(Color.YELLOW);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(FontManager.getFont("pixel", 25f));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(10);
        nameField.setFont(FontManager.getFont("pixel", 25f));
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setFont(FontManager.getFont("pixel", 25f));
        formPanel.add(diffLabel, gbc);

        gbc.gridx = 1;
        difficultyBox = new JComboBox<>(new String[] { "Easy", "Medium", "Hard" });
        difficultyBox.setFont(FontManager.getFont("pixel", 20f));
        formPanel.add(difficultyBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel mapLabel = new JLabel("Map:");
        mapLabel.setForeground(Color.WHITE);
        mapLabel.setFont(FontManager.getFont("pixel", 25f));
        formPanel.add(mapLabel, gbc);

        gbc.gridx = 1;
        mapBox = new JComboBox<>(new String[] { "Start Map", "Random" });
        mapBox.setFont(FontManager.getFont("pixel", 20f));
        formPanel.add(mapBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("START");
        startButton.setFont(FontManager.getFont("pixel", 20f));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty())
                name = "player_1";

            int diff = difficultyBox.getSelectedIndex();
            int map = mapBox.getSelectedIndex();

            onAction.accept("game:" + name + ":" + diff + ":" + map);
        });

        JButton rankingButton = new JButton("RANKING");
        rankingButton.setFont(FontManager.getFont("pixel", 20f));
        rankingButton.setForeground(Color.WHITE);
        rankingButton.setBackground(Color.BLACK);
        rankingButton.setFocusPainted(false);
        rankingButton.setBorderPainted(false);
        rankingButton.setContentAreaFilled(false);
        rankingButton.addActionListener(e -> onAction.accept("ranking"));

        JButton optionsButton = new JButton("OPTIONS");
        optionsButton.setFont(FontManager.getFont("pixel", 20f));
        optionsButton.setForeground(Color.WHITE);
        optionsButton.setBackground(Color.BLACK);
        optionsButton.setFocusPainted(false);
        optionsButton.setBorderPainted(false);
        optionsButton.setContentAreaFilled(false);
        optionsButton.addActionListener(e -> onAction.accept("options"));

        JButton createMapButton = new JButton("MAP CREATOR");
        createMapButton.setFont(FontManager.getFont("pixel", 20f));
        createMapButton.setForeground(Color.WHITE);
        createMapButton.setBackground(Color.BLACK);
        createMapButton.setFocusPainted(false);
        createMapButton.setBorderPainted(false);
        createMapButton.setContentAreaFilled(false);
        createMapButton.addActionListener(e -> onAction.accept("mapcreator"));

        JButton instructionsButton = new JButton("INSTRUCTIONS");
        instructionsButton.setFont(FontManager.getFont("pixel", 20f));
        instructionsButton.setForeground(Color.WHITE);
        instructionsButton.setBackground(Color.BLACK);
        instructionsButton.setFocusPainted(false);
        instructionsButton.setBorderPainted(false);
        instructionsButton.setContentAreaFilled(false);
        instructionsButton.addActionListener(e -> onAction.accept("instructions"));

        JButton onlineLobbyButton = new JButton("ONLINE");
        onlineLobbyButton.setFont(FontManager.getFont("pixel", 20f));
        onlineLobbyButton.setForeground(Color.WHITE);
        onlineLobbyButton.setBackground(Color.BLACK);
        onlineLobbyButton.setFocusPainted(false);
        onlineLobbyButton.setBorderPainted(false);
        onlineLobbyButton.setContentAreaFilled(false);
        onlineLobbyButton.addActionListener(e -> onAction.accept("online"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(startButton);
        buttonPanel.add(rankingButton);
        buttonPanel.add(optionsButton);
        // buttonPanel.add(createMapButton); it would be cool but i dont have time to
        // make it work properly
        buttonPanel.add(instructionsButton);
        buttonPanel.add(onlineLobbyButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
