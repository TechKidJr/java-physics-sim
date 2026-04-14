import java.awt.*;

// Imports needed for .json file reading and writing, used for checking if the user has opened the application before.
import java.io.*;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;

import org.jogamp.java3d.Canvas3D;
import javax.swing.*;
import javax.vecmath.Vector3f;

import org.jogamp.java3d.utils.universe.SimpleUniverse;


public class Main {

    @SuppressWarnings("unchecked") // I am NOT coding and importing a whole library just to make the compiler happy.
    public static void main(String[] args) throws Exception {
        // Test Print Line for Java
        // Deprecated, only uncomment for testing purposes.
        // System.out.println("TestPrint Line for Java");

        // Put this above everything else to create a blank json first.
        ObjectMapper mapper = new ObjectMapper();

        // Creating data to save. (Lowk don't understand this part but trust the process.)
        Map<String, Object> configSettings = new HashMap<>();

        if (new File("src/config.json").exists()) {
            try {
                // Just loading the data from the .json file into the configSettings map, so we can check if the user has opened the application before.
                configSettings = mapper.readValue(new File("src/config.json"), Map.class);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        System.setProperty("sun.awt.noerasebackground", "true");

        JFrame frame = new JFrame("Physics Sim"); 
        
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();     
        Canvas3D canvas = new Canvas3D(config);
        canvas.enableInputMethods(true);
        SimpleUniverse universe = new SimpleUniverse(canvas);
        Renderer render = new Renderer();
        Physics physics = new Physics(render);

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
            String value = JOptionPane.showInputDialog(null,"What do you want the gravity to be set to:  ", "-9.81");
            if (value != null){
                try {
                    float downForce = Float.parseFloat(value);
                    physics.usrCtrledGravity(downForce);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format. Please enter a number.", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        airResistanceItem.addActionListener(e -> {
            String value = JOptionPane.showInputDialog(null, "What do you want the air resistance to be set to: ", "0.3");
            if (value != null){
                try {
                    float resistance = Float.parseFloat(value);
                    physics.usrCtrledAirResistance(resistance);
                    
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Invalid number format. Please enter a number", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Setting the size of the frame.
        frame.setSize(1920, 1080);

        // Setting the bPaneackground color of the frame to Black (for now).
        Color backColor = new Color(38, 43, 51);
        frame.getContentPane().setBackground(backColor);

        Image icon = new ImageIcon(Main.class.getResource("/bionic.png")).getImage();

        frame.setIconImage(icon);

        
        universe.addBranchGraph(render.render(canvas, physics));

        universe.getViewingPlatform().setNominalViewingTransform();

        // Setting the frame to visible :shock:
        frame.setVisible(true);

        physics.update();

        canvas.requestFocus();
    
        // https://bionic.wowzatm.me/bionic.png

        // TODO: Either move this to the bottom of the main method so it renders after the window is loaded.
        // TODO: Make the ui look better, refer to Java Documentation for Swing for more info on how to do this.

        // The null tells Java that the parent component of the dialog is not specified, so it will be centered on the screen.
        // Checks if the user clicks yes first, then asks if they want a tutorial. If they click no, it skips the tutorial. If they click yes, it shows the tutorial. If they click no on the first dialog, it skips both dialogs.
        // TODO: Make this less janky, I will make the if statements better later, this is fine for now.
            if (configSettings.get("hasOpenedBefore") == null || !(Boolean) configSettings.get("hasOpenedBefore")) {
                if (JOptionPane.showConfirmDialog(null, "Welcome! This is a physics sim created by Advaith and Kidanny, published by Bionic. Is this your first time using this application?", "Welcome to PhySim!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.out.println("User Clicked Yes"); // Debugging purposes, can be removed later.
                if (JOptionPane.showConfirmDialog(null, "Would you like to see a tutorial on how to use the application?", "Tutorial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Welcome to PhySim! \n We are so glad you are trying this out! We are working hard to finish this project. \n Click OK to continue to the tutorial!", "Tutorial (0/4)", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, "This is the main interface of the application. In the top left you will see the 'Blocks' menu, inside you can spawn a Cube and Sphere! You can click on the desired shape to spawn it in and move it around!", "Tutorial (1/4)", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, "In the top right you will see the 'Configs' menu, inside you can change the gravity and air resistance settings! Click on the desired setting to change it! \n (Work in Progress, features may be limited or unavailable, check GitHub for updated versions.)", "Tutorial (2/4)", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, "In the top left you will see the 'About' menu, inside you can click on 'Info' to see a small window with info about the project, such as the version and the creators of the project!", "Tutorial (3/4)", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, "We hope you enjoy using this application! If you have any feedback or spot issues please open an issue on GitHub! \n This project is a work in progress and things may change.", "Tutorial (4/4)", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    System.out.println("User Clicked No"); // Debugging purposes, can be removed later.
                }
            }
        }

        configSettings.put("hasOpenedBefore", true);

        // Version 0.0.2 because this will be in the next update.
        configSettings.put("version", "0.0.2 ALPHA");

        try {
            // Writing the data to a .json file called "config.json" in the src folder.
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/config.json"), configSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        frame.getContentPane().setForeground(Color.WHITE);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}