package com.tankgame.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tankgame.managers.FontManager;

public class StartScreen extends JPanel {

    public StartScreen(Consumer<String> onAction) {

        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("tank game (online build)", SwingConstants.CENTER);
        title.setFont(FontManager.getFont("pixel", 54f));
        title.setForeground(Color.WHITE);

        JButton startButton = new JButton("Start");
        startButton.setFont(FontManager.getFont("pixel", 25f));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(e -> onAction.accept("game"));

        JButton optionsButton = new JButton("OPTIONS");
        optionsButton.setFont(FontManager.getFont("pixel", 25f));
        optionsButton.setForeground(Color.WHITE);
        optionsButton.setBackground(Color.BLACK);
        optionsButton.setFocusPainted(false);
        optionsButton.setBorderPainted(false);
        optionsButton.setContentAreaFilled(false);
        optionsButton.addActionListener(e -> onAction.accept("options"));

        JButton createMapButton = new JButton("MAP CREATOR");
        createMapButton.setFont(FontManager.getFont("pixel", 25f));
        createMapButton.setForeground(Color.WHITE);
        createMapButton.setBackground(Color.BLACK);
        createMapButton.setFocusPainted(false);
        createMapButton.setBorderPainted(false);
        createMapButton.setContentAreaFilled(false);
        createMapButton.addActionListener(e -> onAction.accept("mapcreator"));

        JButton instructionsButton = new JButton("instructions");
        instructionsButton.setFont(FontManager.getFont("pixel", 25f));
        instructionsButton.setForeground(Color.WHITE);
        instructionsButton.setBackground(Color.BLACK);
        instructionsButton.setFocusPainted(false);
        instructionsButton.setBorderPainted(false);
        instructionsButton.setContentAreaFilled(false);
        instructionsButton.addActionListener(e -> onAction.accept("instructions"));

        JButton onlineLobbyButton = new JButton("online");
        onlineLobbyButton.setFont(FontManager.getFont("pixel", 25f));
        onlineLobbyButton.setForeground(Color.WHITE);
        onlineLobbyButton.setBackground(Color.BLACK);
        onlineLobbyButton.setFocusPainted(false);
        onlineLobbyButton.setBorderPainted(false);
        onlineLobbyButton.setContentAreaFilled(false);
        onlineLobbyButton.addActionListener(e -> onAction.accept("online"));

        


        add(title, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(startButton);
        buttonPanel.add(optionsButton);
        buttonPanel.add(createMapButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(onlineLobbyButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}