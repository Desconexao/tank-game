package com.tankgame.game;

import javax.swing.JFrame;

import com.tankgame.screens.GameScene;
import com.tankgame.settings.GameConfig;

public class MainWindow {
    private final JFrame frame;

    public MainWindow() {
        frame = new JFrame("Tank Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GameScene gameScene = new GameScene(frame);
        frame.add(gameScene);

        frame.pack();
        frame.setSize(GameConfig.GRID_WIDTH * GameConfig.TILE_SIZE, GameConfig.GRID_HEIGHT * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("Window Created");
    }
}
