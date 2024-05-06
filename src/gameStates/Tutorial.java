package gameStates;

import main.Game;
import ui.MenuButton;
import ui.OptionsButton;
import ui.PauseButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Tutorial extends State implements Statemethods {
    private OptionsButton[] buttons = new OptionsButton[1];
    private BufferedImage backgroundImg;

    public Tutorial(Game game) {
        super(game);
        loadButtons();
        // Replace with actual bg image
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.TUTORIAL);
    }

    private void loadButtons() {
        buttons[0] = new OptionsButton((int)(128 * Game.SCALE), (int)(375 * Game.SCALE), 0, Gamestate.MENU);
    }

    @Override
    public void update() {
        for (OptionsButton ob : buttons) {
            ob.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        for (OptionsButton ob : buttons) {
            ob.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (OptionsButton ob : buttons) {
            if (isIn(e, ob)) {
                ob.setMousePressed(true);
                return;
            }
        }
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private boolean isIn(MouseEvent e, OptionsButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    private void resetButtons() {
        for (OptionsButton ob : buttons)
            ob.resetBools();
    }
}
