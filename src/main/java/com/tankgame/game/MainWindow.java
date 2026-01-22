package com.tankgame.game;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.screens.GameScene;

public class MainWindow {

    public MainWindow() {
        JFrame frame = new JFrame("Tank game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        CardLayout layout = new CardLayout();
        JPanel container = new JPanel(layout);

        GameScene gameScene = new GameScene(frame);
        container.add(gameScene, "GameScene");
        frame.setFocusable(true);
        frame.add(container);
        layout.show(container, "GameScene");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*
     * // Load each screen's layout from screens/
     * // Will ignore files sta
     * private void loadScreenLayouts(JPanel container){
     * File dir = new File("screens/");
     * String fileName;
     * 
     * // Iterate through every file from screens/
     * for (File f : dir.listFiles()) {
     * if (f.isFile()) {
     * fileName = f.getName();
     * if (!fileName.startsWith("LOADOFF")){
     * container.add(new J)
     * }
     * }
     * }
     * }
     */
}
