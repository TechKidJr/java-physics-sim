import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test Print Line for Java
        System.out.println("TestPrint Line for Java");
        JFrame frame = new JFrame("Physics Sim");
        System.out.println("Looking for resource: " + Main.class.getResource("/bionic.png"));
        javax.swing.JLabel label = new javax.swing.JLabel("Hello World!");
        JMenuBar menuBar = new JMenuBar(); 
        JMenu aboutMenu = new JMenu("About");
        JMenu blockMenu = new JMenu("Blocks");
        JMenuItem infoItem = new JMenuItem("Info");
        JMenuItem cubeItem = new JMenuItem("Cube");
        JMenuItem sphereItem = new JMenuItem("Sphere");

        // Set the font of the menus to Comfortaa, size 10.
        aboutMenu.setFont(new Font("Comfortaa", Font.PLAIN,10));
        blockMenu.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        
        // Set the font of the menu items to Comfortaa, size 10.
        infoItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        cubeItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        sphereItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));

        // Add items to their respective menus.
        aboutMenu.add(infoItem);
        blockMenu.add(cubeItem);
        blockMenu.add(sphereItem);
        
        // Add the menu to the menu bar!
        menuBar.add(aboutMenu);
        menuBar.add(blockMenu);
        frame.setJMenuBar(menuBar);

        infoItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Java Physics Sim\nVersion 0.0.0 ALPHA\n\nCreate by: Advaith and Kidanny\n Published by Bionic.");
        });

        cubeItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "A cube should spawn here, remove this once feature is implemented. (PLACEHOLDER)");
        });

        sphereItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "A sphere should spawn here, remove this once feature is implemented. (PLACEHOLDER)");
        });
        // Setting the size of the frame.
        frame.setSize(1920, 1080);

        // Setting the bPaneackground color of the frame to Black (for now).
        frame.getContentPane().setBackground(java.awt.Color.BLACK);
        frame.getContentPane().add(label);

        Image icon = new ImageIcon(Main.class.getResource("/bionic.png")).getImage();

        frame.setIconImage(icon);


        // Setting the frame to visible :shock:
        frame.setVisible(true);

        // https://bionic.wowzatm.me/bionic.png

        frame.getContentPane().setForeground(Color.WHITE);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}