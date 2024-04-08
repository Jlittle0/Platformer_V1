package main;
import javax.swing.JFrame;

public class gameViewer extends JFrame {
    private JFrame jframe;
    public gameViewer(gamePanel gamePanel) {
         jframe = new JFrame();
         jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
         jframe.add(gamePanel);
         jframe.setLocationRelativeTo(null);
         jframe.setResizable(false);
         jframe.pack();
         jframe.setVisible(true);
    }
}
