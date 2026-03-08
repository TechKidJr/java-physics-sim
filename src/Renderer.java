import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Sphere;


import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.DirectionalLight;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class Renderer {

    BranchGroup root;

    /**
     * renders the objects that are going to be used in the physics sim.
     * @return the branchgroup that holds all of the objects.
     */
    public BranchGroup render(){

        root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        
        TransformGroup transformCubes = new TransformGroup();
        transformCubes.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup transformSpheres = new TransformGroup();
        transformSpheres.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //The 3d shapes and design for them
        //TODO: Create multiple cubes and spheres by clicking on the cubes and spheres in the menu bar, probably using a method that creates them.

        Appearance appearance = new Appearance();
        Material material = new Material(new Color3f(0.2f, 0.02f, 0.02f), new Color3f(0f, 0f, 0f), new Color3f(0.7f, 0.1f, 0.1f), new Color3f(1.0f, 1.0f, 1.0f), 75f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);

    
        transformCubes.addChild(cubeGenerate(appearance));
        transformSpheres.addChild(sphereGenerate(appearance));

        Vector3f vectorCube = new Vector3f(-1f, 0, 0); //temp
        Transform3D cubeTranslation= new Transform3D();
        cubeTranslation.setTranslation(vectorCube);

        Vector3f vectorSphere = new Vector3f(1f, 0, 0); //temp
        Transform3D sphereTranslation = new Transform3D();
        transformCubes.setTransform(sphereTranslation); 




        //Lights
        Point3d boundaryCenter = new Point3d(0, 0, -1.0);
        BoundingSphere bounds = new BoundingSphere(boundaryCenter, 1000d);
        Color3f lightColor = new Color3f(0.2f, 0.3f, 0.2f);

        AmbientLight aLight = new AmbientLight(lightColor);
        aLight.setInfluencingBounds(bounds);

        Vector3f direction = new Vector3f(1f, -1f, -1f);
        DirectionalLight dLight = new DirectionalLight(lightColor, direction);
        dLight.setInfluencingBounds(bounds);

        //Adding all of it to the branch group root as a child:
        root.addChild(transformCubes);
        root.addChild(transformSpheres);
        root.addChild(aLight);
        root.addChild(dLight);

        root.compile();

        return root;
    }

    /**
     * Creates a new cube object for the render to use.
     * @param appearance sets the appearance of the cube.
     * @return the cube object
     */
    public ColorCube cubeGenerate(Appearance appearance){
        ColorCube cube = new ColorCube(10.0);
        cube.setAppearance(appearance);
        return cube;
    }

    /**
     * 
     * @param appearance sets the appearance of the sphere
     * @return the sphere object
     */
    public Sphere sphereGenerate(Appearance appearance){
        Sphere sphere = new Sphere(5f, appearance);
        return sphere;

    }
    
}
