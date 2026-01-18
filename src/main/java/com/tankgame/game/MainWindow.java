package com.tankgame.game;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.screens.GameScene;

public class MainWindow {

    public MainWindow(){

        // Create basic Window.
        JFrame frame = new JFrame("Tank game");
        frame.setSize(750, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Create container of screens.
        CardLayout layout = new CardLayout();
        JPanel container = new JPanel(layout);

        // Load screens to container
        container.add(new GameScene(frame), "GameScene");
        
        frame.add(container);

        // Select and show GameScene screen
        layout.show(container, "GameScene");

        
    }

    /*
    // Load each screen's layout from screens/
    // Will ignore files starting with "LOADOFF"
    private void loadScreenLayouts(JPanel container){
        File dir = new File("screens/");
        String fileName;

        // Iterate through every file from screens/
        for (File f : dir.listFiles()) {
            if (f.isFile()) { 
                fileName = f.getName();
                if (!fileName.startsWith("LOADOFF")){
                    container.add(new J)
                }
            }
        }
    }
     */
}
