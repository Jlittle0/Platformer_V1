package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import static utilz.constants.UI.VolumeButtons.*;




public class VolumeButton extends PauseButton {
    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private static final int BUTTON_CENTER_OFFSET = VOLUME_WIDTH / 2;

    public VolumeButton(int x, int y, int width, int height) {
        // Center button onto slider
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= BUTTON_CENTER_OFFSET;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + BUTTON_CENTER_OFFSET;
        maxX = x + width - BUTTON_CENTER_OFFSET;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH , 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
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
