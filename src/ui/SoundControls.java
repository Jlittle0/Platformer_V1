package ui;

import gameStates.Gamestate;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utilz.Constants.UI.PauseButtons.SB_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class SoundControls {
    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;

    public SoundControls() {
        createSoundButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int volumeX = (int)(309 * Game.SCALE);
        int volumeY = (int)(278 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SB_SIZE, SB_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SB_SIZE, SB_SIZE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

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
        }
        // Reset all the boolean conditions for buttons
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        // Make sure all the mouseOver variables are reset
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        // Checks whether the mouse is hovering over any of the buttons and if so set their
        // mouse over to true to allow them to change their index for the image.
        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }


    private boolean isIn(MouseEvent e, PauseButton b) {
        // Using pausebutton since it's the superclass of all paused buttons including music but
        // this checks whether or not the mouseEvent is occuring within the bounds of a button
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
