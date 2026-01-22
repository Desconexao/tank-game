package com.tankgame.game;

import com.tankgame.entities.Player;
import com.tankgame.input.Keyboard;
import com.tankgame.screens.GameScene;
import com.tankgame.settings.Globals;

public class GameTick implements Runnable {
    private final int TPS = Globals.TPS;
    private boolean running = true;
    private GameScene currScene;
    private Keyboard keyInput;
    private long lastMove = 0;
    private long MOVE_DELAY = 150; // in miliseconds


    public GameTick(GameScene currScene){
        this.currScene = currScene;
        keyInput = new Keyboard();
        currScene.mainWindow.addKeyListener(keyInput);
    }

    public void run() {
        long nsPerTick = 1_000_000_000L / TPS;
        long last = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            if (now - last >= nsPerTick) {
                tick();
                last += nsPerTick;
            }
        }
    }

    private void tick() {
        checkInputs();
        //currScene.repaint();

    }

    public void stop() {
        running = false;
    }

    private void checkInputs(){
        long now = System.currentTimeMillis();
        if (now - lastMove < MOVE_DELAY) return;
        Player player = currScene.getPlayer();

        if (keyInput.upPressed){
            if(!isTileBlocked(player.getY() - 1, player.getX())){
                
                currScene.gridLogic.insertPosition('˄', player.getY() - 1, player.getX());
                currScene.gridLogic.insertPosition('_', player.getY(), player.getX());

                player.moveUp();
                currScene.update();
                lastMove = now;
            }
        }
        if (keyInput.downPressed){
            if(!isTileBlocked(player.getY() + 1, player.getX())){
                
                currScene.gridLogic.insertPosition('˅', player.getY() + 1, player.getX());
                currScene.gridLogic.insertPosition('_', player.getY(), player.getX());

                player.moveDown();
                currScene.update();
                lastMove = now;
            }
        }
        if (keyInput.leftPressed){
            if(!isTileBlocked(player.getY(), player.getX() - 1)){
                currScene.gridLogic.insertPosition('<', player.getY(), player.getX() - 1);
                currScene.gridLogic.insertPosition('_', player.getY(), player.getX());

                player.moveLeft();
                currScene.update();
                lastMove = now;
            }
        }
        if (keyInput.rightPressed){
            if(!isTileBlocked(player.getY(), player.getX() + 1)){
                
                currScene.gridLogic.insertPosition('>', player.getY(), player.getX() + 1);
                currScene.gridLogic.insertPosition('_', player.getY(), player.getX());

                player.moveRight();
                currScene.update();
                lastMove = now;
            }
        }
    }

    private boolean isTileBlocked(int Y, int X){
        char[][] currSceneGrid = currScene.gridLogic.getGridMatrix();

        if (currSceneGrid[Y][X] == '_'){
            return false;
        }

        return true;
    }

}
