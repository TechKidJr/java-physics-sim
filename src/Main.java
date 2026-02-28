import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test Print Line for Java
        System.out.println("TestPrint Line for Java");
        JFrame frame = new JFrame("Physics Sim");
        System.out.println("Looking for resource: " + Main.class.getResource("/bionic.png"));


        // Setting the size of the frame.
        frame.setSize(1920, 1080);

        // Setting the bPaneackground color of the frame to Black (for now).
        frame.getContentPane().setBackground(java.awt.Color.BLACK);

        Image icon = new ImageIcon(Main.class.getResource("/bionic.png")).getImage();

        frame.setIconImage(icon);


        // Setting the frame to visible :shock:
        frame.setVisible(true);

        // https://bionic.wowzatm.me/bionic.png

        frame.getContentPane().setForeground(Color.WHITE);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}