package main;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class gameViewer extends JFrame {
    private JFrame jframe;
    public gameViewer(gamePanel gamePanel) {
         jframe = new JFrame();
         jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
         jframe.add(gamePanel);
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
