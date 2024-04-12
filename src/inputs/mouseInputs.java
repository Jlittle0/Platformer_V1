package inputs;
import gameStates.Gamestate;
import main.gamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class mouseInputs implements MouseListener, MouseMotionListener {
    private gamePanel gamePanel;

    public mouseInputs(gamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
            switch(Gamestate.state) {
                case PLAYING:
                    gamePanel.getGame().getPlaying().mouseClicked(e);
                    break;
                default:
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            default:
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            default:
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            default:
        }
    }
}
