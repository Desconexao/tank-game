package com.tankgame.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public Keyboard() {
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
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)
            upPressed = pressed;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
            downPressed = pressed;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT)
            leftPressed = pressed;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
            rightPressed = pressed;
    }
}
