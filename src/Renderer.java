import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.picking.behaviors.PickRotateBehavior;
import org.jogamp.java3d.utils.picking.behaviors.PickTranslateBehavior;


import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.DirectionalLight;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class Renderer {

    BranchGroup root;
    PickRotateBehavior pickRotate;
    PickTranslateBehavior pickMove;
    /**
     * renders the objects that are going to be used in the physics sim.
     * @return the branchgroup that holds all of the objects.
     */
    public BranchGroup render(Canvas3D canvas){

        root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        root.setCapability(BranchGroup.ALLOW_DETACH);


        Point3d boundaryCenter = new Point3d(0, 0, 0.0);
        BoundingSphere bounds = new BoundingSphere(boundaryCenter, 1000d);
        
        pickMove = new PickTranslateBehavior(root, canvas, bounds);
        pickRotate = new PickRotateBehavior(root, canvas, bounds);

        pickMove.setMode(PickTool.BOUNDS);

        //Transformations that affect the cubes
        TransformGroup transformCubes = new TransformGroup();
        transformCubes.setTransform(new Transform3D());
        transformCubes.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformCubes.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformCubes.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        //Transformations that affect the spheres.
        TransformGroup transformSpheres = new TransformGroup();
        transformSpheres.setTransform(new Transform3D());
        transformSpheres.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformSpheres.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformSpheres.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        //The 3d shapes and design for them
        //TODO: Create multiple cubes and spheres by clicking on the cubes and spheres in the menu bar, probably using a method that creates them.

        Appearance appearance = new Appearance();
        Material material = new Material(new Color3f(0.2f, 0.02f, 0.02f), new Color3f(0f, 0f, 0f), new Color3f(0.7f, 0.1f, 0.1f), new Color3f(1.0f, 1.0f, 1.0f), 75f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);

    
        transformCubes.addChild(cubeGenerate(appearance));
        transformSpheres.addChild(sphereGenerate(appearance));

        Vector3f vectorCube = new Vector3f(-0.5f, 0, 0); //temp
        Transform3D rotate = new Transform3D();
        rotate.rotX(Math.PI/4.0); // rotates the cube by 45 degrees
        rotate.rotY(Math.PI/4.0);
        Transform3D cubeTranslation= new Transform3D();
        cubeTranslation.setTranslation(vectorCube);
        cubeTranslation.mul(rotate);
        transformCubes.setTransform(cubeTranslation);


        Vector3f vectorSphere = new Vector3f(0.5f, 0, 0); //temp
        Transform3D sphereTranslation = new Transform3D();
        sphereTranslation.setTranslation(vectorSphere);
        transformSpheres.setTransform(sphereTranslation); 


        //Lights
        Color3f lightColor = new Color3f(0.2f, 0.3f, 0.2f);

        AmbientLight aLight = new AmbientLight(lightColor);
        aLight.setInfluencingBounds(bounds);

        Vector3f direction = new Vector3f(45f, -45f, -45f);
        Color3f directionalColor = new Color3f(1f, 1f, 1f);
        DirectionalLight dLight = new DirectionalLight(directionalColor, direction);
        dLight.setInfluencingBounds(bounds);

        pickRotate.setSchedulingBounds(bounds);
        pickMove.setSchedulingBounds(bounds);


        //Adding all of it to the branch group root as a child:
        root.addChild(transformCubes);
        root.addChild(transformSpheres);
        root.addChild(aLight);
        root.addChild(dLight);
        root.addChild(pickRotate);
        root.addChild(pickMove);


        root.compile();

        return root;
    }

    /**
     * Creates a new cube object for the render to use.
     * @param appearance sets the appearance of the cube.
     * @return the cube object
     */
    public Box cubeGenerate(Appearance appearance){
        Box cube = new Box(0.09f, 0.09f, 0.09f, Box.GENERATE_NORMALS, appearance);
        cube.setCapability(Box.ENABLE_GEOMETRY_PICKING);
        cube.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        return cube;
    }

    /**
     * 
     * @param appearance sets the appearance of the sphere
     * @return the sphere object
     */
    public Sphere sphereGenerate(Appearance appearance){
        Sphere sphere = new Sphere(0.09f, appearance);
        sphere.getShape(Sphere.BODY);
        sphere.setCapability(Sphere.ENABLE_GEOMETRY_PICKING);
        sphere.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        return sphere;
    }
    
}
