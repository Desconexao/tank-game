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
    private WebSocketClient webSocketClient;

    public OnlinePlayerInputHandler(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
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
        String state = pressed ? "pressed" : "released";
        
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (pressed != upPressed) {
                webSocketClient.sendInteraction("up", state);
            }
            upPressed = pressed;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (pressed != downPressed) {
                webSocketClient.sendInteraction("down", state);
            }
            downPressed = pressed;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (pressed != leftPressed) {
                webSocketClient.sendInteraction("left", state);
            }
            leftPressed = pressed;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (pressed != rightPressed) {
                webSocketClient.sendInteraction("right", state);
            }
            rightPressed = pressed;
        }
        if (code == KeyEvent.VK_Z || code == KeyEvent.VK_SPACE) {
            if (pressed != shootPressed) {
                webSocketClient.sendInteraction("shoot", state);
            }
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
