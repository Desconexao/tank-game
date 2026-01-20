package com.tankgame.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.tankgame.entities.Player;
import com.tankgame.screens.GameScene;

public class Keyboard extends KeyAdapter {
    private Player player;
    private GameScene scene;

    public Keyboard(GameScene scene) {
        this.scene = scene;
        this.player = scene.getPlayer();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            player.moveUp();
        } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            player.moveDown();
        } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            player.moveLeft();
        } else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            player.moveRight();
        }
        scene.update();
    }
}
