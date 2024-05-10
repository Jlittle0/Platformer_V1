package ui;

import gameStates.Gamestate;
import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameCompletedOverlay {
    private Playing playing;
    private BufferedImage gameCompletedImg;

    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    private void loadImgs() {
        // Switch to gameCompleted
        gameCompletedImg = LoadSave.GetSpriteAtlas(LoadSave.GAME_WON);
    }

    public void draw(Graphics g) {
        // Not much here so just make the screen darker.
        // TODO: Complete this later once Game Over image is complete
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawImage(gameCompletedImg, (int)(Game.GAME_WIDTH / 2 - 284 * Game.SCALE), (int)(150 * Game.SCALE), (int)(568 * Game.SCALE), (int)(48 * Game.SCALE), null);
        g.drawString("Congratulations! You beat the game. To replay the game or return to the main menu, please press esc.", Game.GAME_WIDTH / 2 - 300, (int)(250 * Game.SCALE));
        g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH / 2 - 100, (int)(350 * Game.SCALE));
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
