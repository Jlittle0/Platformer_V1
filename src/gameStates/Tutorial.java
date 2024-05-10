package gameStates;

import main.Game;
import org.w3c.dom.css.RGBColor;
import ui.MenuButton;
import ui.OptionsButton;
import ui.PauseButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;

public class Tutorial extends State implements Statemethods {
    private OptionsButton[] buttons = new OptionsButton[1];
    private BufferedImage backgroundImg, tutorial_platform_2;
    private BufferedImage[][] animations;
    private float airSpeed;
    private int aniTickOne, aniTickTwo, aniIndexOne, aniIndexTwo, state = FALLING;
    private float yPos = 75 * Game.SCALE;
    private boolean landing, inAir;

    public Tutorial(Game game) {
        super(game);
        loadButtons();
        loadAnimations();
        // Replace with actual bg image
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.TUTORIAL);
        tutorial_platform_2 = LoadSave.GetSpriteAtlas(LoadSave.TUTORIAL_PLATFORM_2);
    }

    private void checkPos() {
        if (yPos < 99 * Game.SCALE)
            inAir = true;
        else {
            inAir = false;
        }
    }

    private void setState() {
        if (inAir)
            state = FALLING;
        else
            state = LANDING;
    }

    private void loadButtons() {
        buttons[0] = new OptionsButton((int)(128 * Game.SCALE), (int)(375 * Game.SCALE), 0, Gamestate.MENU);
    }

    private void loadAnimations() {
            // Loads all the player animations from the atlas into a 2D array of images
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            animations = new BufferedImage[11][10];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 91, j * 19, 91, 19);
    }

    private void updateAnimationTick() {
        // Updates the animation tick and index for each animation based on the desired speed so
        // that animations can be slowed down or sped up accordingly and are equally paced.
        aniTickOne++;
        if (aniTickOne >= ANIMATION_SPEED) {
            aniTickOne = 0;
            aniIndexOne++;
            if (aniIndexOne >= GetSpriteAmount(ATTACK_3)) {
                aniIndexOne = 0;
            }
        }

        aniTickTwo++;
        if (aniTickTwo >= ANIMATION_SPEED * 2) {
            aniTickTwo= 0;
            aniIndexTwo++;
            if (aniIndexTwo >= GetSpriteAmount(state)) {
                inAir = true;
                yPos = 75 * Game.SCALE;
                aniIndexTwo = 0;
            }
        }
    }

    @Override
    public void update() {
        for (OptionsButton ob : buttons) {
            ob.update();
        }
        checkPos();
        setState();
        updatePosition();
        updateAnimationTick();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.setColor(new Color(156, 120, 109));
        g.fillRect((int)(578.5 * Game.SCALE), (int)(90.5 * Game.SCALE), (int)(97.75 * Game.SCALE * 1.72), (int)(96 * Game.SCALE * 1.72));
        g.fillRect((int)(384.5 * Game.SCALE), (int)(90.5 * Game.SCALE), (int)(97.75 * Game.SCALE * 1.72), (int)(96 * Game.SCALE * 1.72));
        g.drawImage(tutorial_platform_2, (int)(580 * Game.SCALE), (int)(200 * Game.SCALE), (int)(96 * Game.SCALE * 1.72), (int)(32 * Game.SCALE * 1.72), null);
        g.drawImage(tutorial_platform_2, (int)(386 * Game.SCALE), (int)(200 * Game.SCALE), (int)(96 * Game.SCALE * 1.72), (int)(32 * Game.SCALE * 1.72), null);
        g.drawImage(animations[ATTACK_3][aniIndexOne], (int)(590 * Game.SCALE), (int)(175 * Game.SCALE), (int)(91 * Game.SCALE * PLAYER_SCALE), (int)(19 * Game.SCALE * PLAYER_SCALE), null);
        g.drawImage(animations[state][aniIndexTwo], (int)(440 * Game.SCALE), (int)(yPos * Game.SCALE), (int)(91 * Game.SCALE * PLAYER_SCALE), (int)(19 * Game.SCALE * PLAYER_SCALE), null);
        for (OptionsButton ob : buttons) {
            ob.draw(g);
        }
    }

    public void updatePosition() {
        if (yPos < (99 * Game.SCALE)) {
            yPos += airSpeed;
            airSpeed += GRAVITY / 10;
            landing = false;
        } else {
            yPos = (99 * Game.SCALE);
            airSpeed = 0;
            landing = true;
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
