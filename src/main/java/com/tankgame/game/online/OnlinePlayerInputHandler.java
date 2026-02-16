package com.tankgame.game.online;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Input handler for online games.
 * Similar to KeyboardInput but designed for online gameplay.
 */
public class OnlinePlayerInputHandler extends KeyAdapter {
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean shootPressed;
    public boolean pausePressed = false;

    public OnlinePlayerInputHandler() {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleKey(e.getKeyCode(), false);
    }

    private void handleKey(int code, boolean pressed) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = pressed;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = pressed;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = pressed;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = pressed;
        }
        if (code == KeyEvent.VK_Z || code == KeyEvent.VK_SPACE) {
            shootPressed = pressed;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            pausePressed = pressed;
        }
    }

    public void resetPause() {
        pausePressed = false;
    }

    public void reset() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        shootPressed = false;
    }
}
