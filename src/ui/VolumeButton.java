package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {
    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    // Whole point of this offset is so that the center of the button is drawn where the
    // mouse cursor is and follows it rather than the edge of the button
    private static final int BUTTON_CENTER_OFFSET = VOLUME_WIDTH / 2;

    public VolumeButton(int x, int y, int width, int height) {
        // Center button onto slider
        super(x + width / 2, y, VOLUME_WIDTH, height);
        // Bounds used to determine whether or not mouse is over/pressing the button
        bounds.x -= BUTTON_CENTER_OFFSET;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + BUTTON_CENTER_OFFSET;
        maxX = x + width - BUTTON_CENTER_OFFSET;
        loadImgs();
    }

    private void loadImgs() {
        // Puts all the stages of the volume button into the imgs array
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH , 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        // Check whether or not the mouse is hovering over button or is pressed to change
        // image by changing the index used to determine which img in imgs to show.
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    public void draw(Graphics g) {
        // Width and height passed in match slider
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - BUTTON_CENTER_OFFSET, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        // Changes the location of the button (x-value)
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;

        bounds.x = buttonX - BUTTON_CENTER_OFFSET;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
