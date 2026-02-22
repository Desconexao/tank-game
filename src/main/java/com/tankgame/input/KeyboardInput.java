package com.tankgame.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardInput extends KeyAdapter {
    public boolean upPressed, downPressed, leftPressed, rightPressed, shootPressed;
    public boolean pausePressed = false;
    private boolean pauseWasPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        handleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleKey(e.getKeyCode(), false);
    }

    private void handleKey(int code, boolean pressed) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)
            upPressed = pressed;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
            downPressed = pressed;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT)
            leftPressed = pressed;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
            rightPressed = pressed;
        if (code == KeyEvent.VK_Z || code == KeyEvent.VK_SPACE)
            shootPressed = pressed;
        if (code == KeyEvent.VK_ESCAPE)
            pausePressed = pressed;
    }

    public boolean checkPauseToggle() {
        if (pausePressed && !pauseWasPressed) {
            pauseWasPressed = true;
            return true;
        }
        if (!pausePressed) {
            pauseWasPressed = false;
        }
        return false;
    }

    public void resetAllInputs() {
        upPressed = downPressed = leftPressed = rightPressed = shootPressed = pausePressed = pauseWasPressed = false;
    }
}
