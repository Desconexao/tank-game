package com.tankgame.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tankgame.managers.FontManager;
import com.tankgame.managers.RankingManager;

public class RankingScreen extends JPanel {
    private RankingManager rankingManager;
    private JPanel listPanel;

    public RankingScreen(Consumer<String> onAction) {
        this.rankingManager = new RankingManager();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("TOP 10 RANKING", SwingConstants.CENTER);
        title.setFont(FontManager.getFont("pixel", 54f));
        title.setForeground(Color.YELLOW);
        add(title, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

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
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshRanking() {
        listPanel.removeAll();
        listPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        List<RankingManager.PlayerScore> scores = rankingManager.getAllScores();

        if (scores.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nothing to show!");
            emptyLabel.setFont(FontManager.getFont("pixel", 30f));
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            int pos = 1;
            for (RankingManager.PlayerScore ps : scores) {
                JLabel scoreLabel = new JLabel(pos + "o - " + ps.getPlayerName() + " : " + ps.getScore());
                scoreLabel.setFont(FontManager.getFont("pixel", 35f));
                scoreLabel.setForeground(Color.WHITE);
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(scoreLabel);
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                pos++;
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}
