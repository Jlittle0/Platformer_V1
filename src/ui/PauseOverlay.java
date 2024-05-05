package ui;

import gameStates.Gamestate;
import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.UrmButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private UrmButton menuButton, replayButton, unpauseButton;
    private SoundControls soundControls;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        soundControls = playing.getGame().getSoundControls();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int)(313 * Game.SCALE);
        int replayX = (int)(387 * Game.SCALE);
        int unpauseX = (int)(462 * Game.SCALE);
        int buttonY = (int)(325 * Game.SCALE);
        unpauseButton = new UrmButton(unpauseX, buttonY, URM_SIZE, URM_SIZE, 0);
        replayButton = new UrmButton(replayX, buttonY, URM_SIZE, URM_SIZE, 1);
        menuButton = new UrmButton(menuX, buttonY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int)(25 * Game.SCALE);
    }

    public void update() {
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        soundControls.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        // Urm Buttons
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        soundControls.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
            soundControls.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        // Checks whether or not the mouse press event is within the bounds of any of the
        // buttons in the paused menu and if it is to set its specific MousePressed variable
        // to true
        if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
        else
            soundControls.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        // Using not to flip false to true or true to false
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        } else
            soundControls.mouseReleased(e);

        // Reset all the boolean conditions for buttons
        menuButton.resetBools();
        replayButton.resetBools();
        unpauseButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        // Make sure all the mouseOver variables are reset
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        // Checks whether the mouse is hovering over any of the buttons and if so set their
        // mouse over to true to allow them to change their index for the image.
        if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if (isIn(e, replayButton))
            replayButton.setMouseOver(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMouseOver(true);
        else
            soundControls.mouseMoved(e);
    }


    private boolean isIn(MouseEvent e, PauseButton b) {
        // Using pausebutton since it's the superclass of all paused buttons including music but
        // this checks whether or not the mouseEvent is occuring within the bounds of a button
        return b.getBounds().contains(e.getX(), e.getY());
    }





}
