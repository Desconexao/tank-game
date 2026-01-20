package com.tankgame.screens;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tankgame.entities.Player;
import com.tankgame.game.GameGrid;
import com.tankgame.graphics.SpriteImport;
import com.tankgame.graphics.Renderer;

public class GameScene extends JPanel {
    private JPanel gameGrid;
    private GameGrid gridLogic;
    private Player player;
    private Renderer renderer;
    private SpriteImport sprites;

    public GameScene(JFrame mainWindow) {
        this.sprites = new SpriteImport();
        this.renderer = new Renderer(sprites);
        this.gridLogic = new GameGrid(true, 13, 13);
        this.player = new Player(1, 1, 3, "player_tank");

        this.gameGrid = new JPanel(new GridLayout(13, 13));
        this.gameGrid.setPreferredSize(new Dimension(975, 975));

        renderer.render(gameGrid, gridLogic, player);

        this.add(gameGrid);
        mainWindow.add(this);
    }

    public void update() {
        renderer.render(gameGrid, gridLogic, player);
    }

    public Player getPlayer() {
        return player;
    }
}
