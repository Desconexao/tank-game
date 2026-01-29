package com.tankgame.game;

import com.tankgame.screens.GameScene;
import javax.swing.JFrame;

public class MainWindow {
    private final JFrame frame;

    public MainWindow() {
        frame = new JFrame("Tank Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GameScene gameScene = new GameScene(frame);
        frame.add(gameScene);

        frame.pack();
        frame.setSize(975, 975);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("Window Created");
    }
}
