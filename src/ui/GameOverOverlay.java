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
    private BufferedImage gameOverImg;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    private void loadImgs() {
        gameOverImg = LoadSave.GetSpriteAtlas(LoadSave.GAME_OVER);
    }

    public void draw(Graphics g) {
        // Not much here so just make the screen darker.
        // TODO: Complete this later once Game Over image is complete
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawImage(gameOverImg, (int)(Game.GAME_WIDTH / 2 - 203 * Game.SCALE), (int)(150 * Game.SCALE), (int)(403 * Game.SCALE), (int)(54 * Game.SCALE), null);
        g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH / 2 - 100, (int)(250 * Game.SCALE));
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
