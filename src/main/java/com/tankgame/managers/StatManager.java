package com.tankgame.managers;

import java.awt.Color;

import javax.swing.JLabel;

import com.tankgame.entities.tank.Player;
import com.tankgame.screens.GameScene;
import com.tankgame.screens.widgets.StatBoardWidget;

public class StatManager {

    JLabel timerLabel;
    JLabel healthPointLabel;
    JLabel livesLabel;
    JLabel scoreLabel;
    Player player;
    StatBoardWidget statWidget;

    public StatManager(GameScene scene){
        player = scene.getPlayer();
        statWidget = scene.getStatBoard();

        timerLabel = statWidget.getStatTimerLabel();
        healthPointLabel = statWidget.getStatHPLabel();
        livesLabel = statWidget.getStatLivesLabel();
        scoreLabel = statWidget.getStatScoreLabel();

        

    }

    public void update(int time, int score){
        healthPointLabel.setText( "" + player.getHealth());
        livesLabel.setText("?"); // Change later lol
        scoreLabel.setText("" + score);
        timerLabel.setText(String.format("%06d", time));
    }

    public void setRedTimer(boolean red){
        if(red)
            timerLabel.setForeground(Color.RED);
        else
            timerLabel.setForeground(Color.RED);
    }

}
