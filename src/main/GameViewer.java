package main;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameViewer extends JFrame {
    private JFrame jframe;
    public GameViewer(GamePanel gamePanel) {
         jframe = new JFrame();
         jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
         jframe.add(gamePanel);
         // Centers screen with setting relative location
         jframe.pack();
         jframe.setLocationRelativeTo(null);
         jframe.setResizable(false);
         jframe.setVisible(true);
         jframe.addWindowFocusListener(new WindowFocusListener() {
             @Override
             public void windowGainedFocus(WindowEvent e) {

             }

             @Override
             public void windowLostFocus(WindowEvent e) {
                    gamePanel.getGame().windowFocusLost();
             }
         });
    }
}
