package com.tankgame.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tankgame.managers.FontManager;

public class InstructionsScreen extends JPanel {

    public InstructionsScreen(Consumer<String> onAction) {

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Shoot with Z or SPACE MOVE WASD/ARROWS", SwingConstants.CENTER);
        title.setFont(FontManager.getFont("pixel", 54f));
        title.setForeground(Color.WHITE);

        JButton backButton = new JButton("back");
        backButton.setFont(FontManager.getFont("pixel", 25f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> onAction.accept("start"));

        add(title, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
