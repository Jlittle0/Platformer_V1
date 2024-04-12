package inputs;
import gameStates.Gamestate;
import main.gamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static utilz.constants.Directions.*;

public class keyboardInputs implements KeyListener {

    private gamePanel gamePanel;

    public keyboardInputs(gamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            default:
                break;
        }
    }
}
