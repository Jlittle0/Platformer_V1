package gameStates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import ui.*;
import utilz.LoadSave;

import static utilz.Constants.Difficulties.*;

public class OptionsState extends State implements Statemethods {
    private OptionsButton[] buttons = new OptionsButton[4];
    private SoundControls soundControls;
    private BufferedImage backgroundImg;

    public OptionsState(Game game) {
        super(game);
        loadImages();
        loadButtons();
        soundControls = game.getSoundControls();
    }

    private void loadButtons() {
        buttons[0] = new OptionsButton((int)(168.5 * Game.SCALE), (int)(354 * Game.SCALE), 0, Gamestate.MENU);
        buttons[1] = new OptionsButton((int)(330.5 * Game.SCALE), (int)(297 * Game.SCALE), 1, EASY);
        buttons[2] = new OptionsButton((int)(494.5 * Game.SCALE), (int)(297 * Game.SCALE), 1, HARD);
        buttons[3] = new OptionsButton((int)(659 * Game.SCALE), (int)(297 * Game.SCALE), 1, HELL);
    }

    private void loadImages() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND);
    }

    @Override
    public void update() {
        soundControls.update();
        for (OptionsButton ob : buttons) {
            ob.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        soundControls.draw(g);
        for (OptionsButton ob : buttons) {
            ob.draw(g);
        }
    }

    public void mouseDragged(MouseEvent e) {
        soundControls.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (OptionsButton ob : buttons) {
            if (isIn(e, ob)) {
                ob.setMousePressed(true);
                return;
            }
        }
        soundControls.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (OptionsButton ob : buttons) {
            if (isIn(e, ob)) {
                if (ob.isMousePressed()) {
                    ob.applyAttribute();
                }
                resetButtons();
                return;
            }
        }
        soundControls.mouseReleased(e);
    }

    private void resetButtons() {
        for (OptionsButton ob : buttons)
            ob.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (OptionsButton ob : buttons)
            ob.setMouseOver(false);

        for (OptionsButton ob : buttons)
            if (isIn(e, ob)) {
                ob.setMouseOver(true);
                return;
            }
        soundControls.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private boolean isIn(MouseEvent e, OptionsButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
