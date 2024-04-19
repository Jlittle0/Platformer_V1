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
    private SoundButton musicButton, sfxButton;
    private UrmButton menuButton, replayButton, unpauseButton;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int volumeX = (int)(309 * Game.SCALE);
        int volumeY = (int)(278 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT);
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

    private void createSoundButtons() {
        int soundX = (int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SB_SIZE, SB_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SB_SIZE, SB_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int)(25 * Game.SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        // Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Urm Buttons
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);

        // Volume Slider
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        // Checks whether or not the mouse press event is within the bounds of any of the
        // buttons in the paused menu and if it is to set its specific MousePressed variable
        // to true
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        // Using not to flip false to true or true to false
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
        } else if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed())
                // Not currently implemented so just prints string for now
                System.out.println("Replay level");
        } else if (isIn(e, unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        }
        // Reset all the boolean conditions for buttons
        musicButton.resetBools();
        sfxButton.resetBools();
        menuButton.resetBools();
        replayButton.resetBools();
        unpauseButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        // Make sure all the mouseOver variables are reset
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        // Checks whether the mouse is hovering over any of the buttons and if so set their
        // mouse over to true to allow them to change their index for the image.
        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if (isIn(e, replayButton))
            replayButton.setMouseOver(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }


    private boolean isIn(MouseEvent e, PauseButton b) {
        // Using pausebutton since it's the superclass of all paused buttons including music but
        // this checks whether or not the mouseEvent is occuring within the bounds of a button
        return b.getBounds().contains(e.getX(), e.getY());
    }





}
