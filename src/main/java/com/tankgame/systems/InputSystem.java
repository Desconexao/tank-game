package com.tankgame.systems;

import com.tankgame.entities.tank.Player;
import com.tankgame.input.KeyboardInput;
import com.tankgame.utils.Direction;

public class InputSystem {
    private final KeyboardInput keyboard;

    private boolean pauseWasPressed = false;

    public InputSystem() {
        this.keyboard = new KeyboardInput();
    }

    public void processInput(Player player, MovementSystem movementSystem) {
        double speed = player.getSpeed();

        if (keyboard.upPressed) {
            tryMovePlayer(player, movementSystem, player.getX(), player.getY() - speed, Direction.UP);
        } else if (keyboard.downPressed) {
            tryMovePlayer(player, movementSystem, player.getX(), player.getY() + speed, Direction.DOWN);
        } else if (keyboard.leftPressed) {
            tryMovePlayer(player, movementSystem, player.getX() - speed, player.getY(), Direction.LEFT);
        } else if (keyboard.rightPressed) {
            tryMovePlayer(player, movementSystem, player.getX() + speed, player.getY(), Direction.RIGHT);
        }
    }

    public boolean processPauseInput() {
        if (keyboard.pausePressed && !pauseWasPressed) {
            pauseWasPressed = true;
            return true;
        }

        if (!keyboard.pausePressed) {
            pauseWasPressed = false;
        }

        return false;
    }

    public boolean checkPauseToggle() {
        boolean shouldToggle = processPauseInput();

        if (shouldToggle) {
            keyboard.resetPause();
        }

        return shouldToggle;
    }

    private void tryMovePlayer(Player player, MovementSystem movementSystem,
            double newX, double newY, Direction direction) {
        if (movementSystem.tryMove(player, newX, newY)) {
            player.setDirection(direction);
        }
    }

    public KeyboardInput getKeyboard() {
        return keyboard;
    }

    public boolean isShootingPressed() {
        return keyboard.shootPressed;
    }

    public void resetAllInputs() {
        keyboard.upPressed = false;
        keyboard.downPressed = false;
        keyboard.leftPressed = false;
        keyboard.rightPressed = false;
        keyboard.shootPressed = false;
        keyboard.pausePressed = false;
        pauseWasPressed = false;
    }
}
