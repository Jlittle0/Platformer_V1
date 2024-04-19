package ui;

import gameStates.Gamestate;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.Buttons.*;

public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = MB_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;


    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        // Initializes the bounds of each button by using its position so that mouse movements
        // can be tracked and to know whether or not the button is used/should change
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, MB_WIDTH, MB_HEIGHT);
    }

    private void loadImgs() {
        // Loads the button images into imgs
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * MB_DEFAULT_WIDTH, rowIndex * MB_DEFAULT_HEIGHT, MB_DEFAULT_WIDTH, MB_DEFAULT_HEIGHT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, MB_WIDTH, MB_HEIGHT, null);
    }

    public void update() {
        // Same as paused buttons but just uses an index to determine which index to display
        // and uses the two boolean values to swap between indicies when prompted
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
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

    public void applyGamestate() {
        Gamestate.state = state;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
