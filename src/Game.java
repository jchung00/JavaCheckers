
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Checkers Board");
        frame.setVisible(true);
        frame.setTitle("Checkers Board");
        
        CheckersPanel board =
                new CheckersPanel();

        frame.add(board.getGui());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);

        frame.pack();
        frame.setMinimumSize(frame.getSize());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}