import java.awt.*;
import java.sql.Time;

import org.jogamp.java3d.Canvas3D;
import javax.swing.*;

import org.jogamp.java3d.utils.universe.SimpleUniverse;

import Constants.WorldConstants;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test Print Line for Java
        System.out.println("TestPrint Line for Java");

        
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        System.setProperty("sun.awt.noerasebackground", "true");

        JFrame frame = new JFrame("Physics Sim"); 
        
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();     
        Canvas3D canvas = new Canvas3D(config);
        canvas.enableInputMethods(true);
        SimpleUniverse universe = new SimpleUniverse(canvas);
        Renderer render = new Renderer();
        Physics physics = new Physics();

        frame.add(canvas);

        JMenuBar menuBar = new JMenuBar(); 
        JMenu aboutMenu = new JMenu("About");
        JMenu blockMenu = new JMenu("Blocks");
        JMenu configMenu = new JMenu("Configs");
        JMenuItem infoItem = new JMenuItem("Info");
        JMenuItem cubeItem = new JMenuItem("Cube");
        JMenuItem sphereItem = new JMenuItem("Sphere");
        JMenuItem gravityItem = new JMenuItem("Gravity");
        JMenuItem airResistanceItem = new JMenuItem("Air Resistance");

        // Set the font of the menus to Comfortaa, size 10.
        aboutMenu.setFont(new Font("Comfortaa", Font.PLAIN,10));
        blockMenu.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        configMenu.setFont(new Font("Confortaa", Font.PLAIN, 10));
        
        // Set the font of the menu items to Comfortaa, size 10.
        infoItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        cubeItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        sphereItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        gravityItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));
        airResistanceItem.setFont(new Font("Comfortaa", Font.PLAIN, 10));

        // Add items to their respective menus.
        aboutMenu.add(infoItem);
        blockMenu.add(cubeItem);
        blockMenu.add(sphereItem);
        configMenu.add(gravityItem);
        configMenu.add(airResistanceItem);
        
        // Add the menu to the menu bar!
        menuBar.add(aboutMenu);
        menuBar.add(blockMenu);
        menuBar.add(configMenu);
        frame.setJMenuBar(menuBar);

        infoItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Java Physics Sim\nVersion 0.0.1 ALPHA\n\nCreated by: Advaith and Kidanny\nPublished by Bionic.");
        });

        cubeItem.addActionListener(e -> {
            render.rootCubeGroupAdd();
        });

        sphereItem.addActionListener(e -> {
            render.rootSphereGroupAdd();
        });

        gravityItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Settings to change gravity should be here (PLACEHOLDER)");
        });

        airResistanceItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Settings to change air resistance should be here (PLACEHOLDER)");
        });
        
        // Setting the size of the frame.
        frame.setSize(1920, 1080);

        // Setting the bPaneackground color of the frame to Black (for now).
        frame.getContentPane().setBackground(java.awt.Color.BLACK);

        Image icon = new ImageIcon(Main.class.getResource("/bionic.png")).getImage();

        frame.setIconImage(icon);

        
        universe.addBranchGraph(render.render(canvas));

        universe.getViewingPlatform().setNominalViewingTransform();

        physics.init();
        // Setting the frame to visible :shock:
        frame.setVisible(true);

        final long[] previousTime = {System.nanoTime()};

        new Timer(16, e -> {
            long[] currentTime = {System.nanoTime()};
            float deltaTimeInSeconds = ((float)(currentTime[0] - previousTime[0]))/WorldConstants.NANO_IN_SECONDS;
            if (deltaTimeInSeconds > 1f/6f){
                deltaTimeInSeconds = 0.12f;
                physics.update(deltaTimeInSeconds);
            }
            else {
                physics.update(deltaTimeInSeconds);
            }
            previousTime[0] = currentTime[0];
        }).start();

        canvas.requestFocus();

        // https://bionic.wowzatm.me/bionic.png

        frame.getContentPane().setForeground(Color.WHITE);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}