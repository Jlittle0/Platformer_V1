package gameStates;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Tutorial extends State implements Statemethods {
    private MenuButton[] buttons = new MenuButton[1];
    private BufferedImage backgroundImg;

    public Tutorial(Game game) {
        super(game);
        loadButtons();
        // Replace with actual bg image
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(100, Game.GAME_HEIGHT - 100, 3, Gamestate.MENU);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
