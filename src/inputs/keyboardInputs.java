package inputs;
import main.gamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.changeYDelta(-5);
                System.out.println("Tis W");
                break;
            case KeyEvent.VK_A:
                gamePanel.changeXDelta(-5);
                System.out.println("Tis A");
                break;
            case KeyEvent.VK_S:
                gamePanel.changeYDelta(5);
                System.out.println("Tis S");
                break;
            case KeyEvent.VK_D:
                gamePanel.changeXDelta(5);
                System.out.println("Tis D");
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
