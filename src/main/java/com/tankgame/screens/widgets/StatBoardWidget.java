package com.tankgame.screens.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tankgame.managers.FontManager;

public class StatBoardWidget extends JPanel{
    protected JLabel healthpointLabel;
    protected JLabel livesLabel;
    protected JLabel scoreLabel;
    protected JLabel timerLabel;
    protected Font pixel;

    public StatBoardWidget(Consumer<String> onAction){
        this.pixel = FontManager.getFont("pixel", 54f);

        
        setLayout(new GridLayout(0, 2));

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

        add(timerLabelTitle);
        add(timerLabel);
        add(scoreLabelTitle);
        add(scoreLabel);
        add(healthpointLabelTitle);
        add(healthpointLabel);
        add(livesLabelTitle);
        add(livesLabel);
        add(backButton);
        setBackground(Color.DARK_GRAY);
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
}
