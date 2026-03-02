import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Color3f;
import javax.media.j3d.BranchGroup;

public class Renderer {
    public void render(){
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        SimpleUniverse universe = new SimpleUniverse(canvas);

        BranchGroup root = new BranchGroup();

        //The 3d shapes
        ColorCube cube = new ColorCube(10.0);
        Sphere sphere = new Sphere();

        root.addChild(cube);
        root.addChild(sphere);


        TransformGroup transform = new TransformGroup();
        transform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        

        transform.addChild(root);

        Transform3D transform3D = new Transform3D();


        //Lights
        AmbientLight light = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));


    }
    
}
