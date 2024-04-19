package ui;

import gameStates.Gamestate;
import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.GetSpriteAmount;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage[] animation;
    private int aniTick, aniIndex, aniSpeed = 25;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        // Not much here so just make the screen darker.
        // TODO: Complete this later once Game Over image is complete
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH / 2, 300);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }

    private void loadAnimations() {
        // Loads all the player animations from the atlas into a 2D array of images
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.STUMP_ATLAS);
        animation = new BufferedImage[7];
        for (int i = 0; i < animation.length; i++)
                animation[i] = img.getSubimage(i * 180, 0, 180, 180);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 6) {
                aniIndex = 6;
            }
        }
    }
}
