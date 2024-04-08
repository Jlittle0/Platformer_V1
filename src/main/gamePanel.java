package main;
import inputs.keyboardInputs;
import inputs.mouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.constants.PlayerConstants.*;
import static utilz.constants.Directions.*;

public class gamePanel extends JPanel {
    private mouseInputs mouseInputs;
    private Game game;

    public gamePanel(Game game) {
        mouseInputs = new mouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new keyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }



    public void updateGame() {
    }

    public void paintComponent(Graphics g) {
            super.paintComponent(g);
            game.render(g);
    }

    public Game getGame() {
        return game;
    }

}
