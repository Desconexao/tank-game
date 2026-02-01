package com.tankgame.managers;

import javax.swing.JLabel;

import com.tankgame.entities.tank.Player;
import com.tankgame.screens.GameScene;

public class StatManager {

    JLabel timerLabel;
    JLabel healthPointLabel;
    JLabel livesLabel;
    JLabel scoreLabel;
    Player player;

    public StatManager(GameScene scene){
        player = scene.getPlayer();
        timerLabel = scene.getStatTimerLabel();
        healthPointLabel = scene.getStatHPLabel();
        livesLabel = scene.getStatLivesLabel();
        scoreLabel = scene.getStatScoreLabel();

        

    }

    public void update(int time, int score){
        healthPointLabel.setText( "" + player.getHealth());
        livesLabel.setText("1"); // Change later lol
        scoreLabel.setText("" + score);
        timerLabel.setText(String.format("%06d", time));
    }

}
