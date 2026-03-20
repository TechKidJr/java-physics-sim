import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.picking.behaviors.PickRotateBehavior;
import org.jogamp.java3d.utils.picking.behaviors.PickTranslateBehavior;

import javax.swing.JMenuItem;

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

import Constants.CubeConstants;

public class Renderer {

    BranchGroup root;
    TransformGroup transformCubes;
    TransformGroup transformSpheres;
    PickRotateBehavior pickRotate;
    PickTranslateBehavior pickMove;
    Appearance appearance;
    /**
     * renders the objects that are going to be used in the physics sim.
     * @param canvas the drawing field of the window.
     * @return the branchgroup that holds all of the objects.
     */
    public BranchGroup render(Canvas3D canvas){

        root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        root.setCapability(BranchGroup.ALLOW_DETACH);


        Point3d boundaryCenter = new Point3d(0, 0, 0.0);
        BoundingSphere bounds = new BoundingSphere(boundaryCenter, 1000d);
        
        pickMove = new PickTranslateBehavior(root, canvas, bounds);
        pickRotate = new PickRotateBehavior(root, canvas, bounds);


        pickMove.setMode(PickTool.BOUNDS);
        pickRotate.setMode(PickTool.BOUNDS);

        //Transformations that affect the cubes
        transformCubes = new TransformGroup();
        transformCubes.setTransform(new Transform3D());
        transformCubes.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformCubes.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        transformCubes.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        transformCubes.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformCubes.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        //Transformations that affect the spheres.
        transformSpheres = new TransformGroup();
        transformSpheres.setTransform(new Transform3D());
        transformSpheres.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformSpheres.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformSpheres.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        transformSpheres.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        transformSpheres.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        //The 3d shapes and design for them
        //TODO: Create multiple cubes and spheres by clicking on the cubes and spheres in the menu bar, probably using a method that creates them.

    

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
        //TODO: Adjust lighting color.
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
        root.addChild(pickMove);
        root.addChild(pickRotate);

        root.compile();

        return root;
    }

    /**
     * Creates a new cube object for the render to use.
     * @param appearance sets the appearance of the cube.
     * @return the cube object
     */
    public BranchGroup cubeGenerate(Appearance appearance){
        BranchGroup bgC = new BranchGroup();
        bgC.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bgC.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bgC.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        TransformGroup tgC = new TransformGroup();
        tgC.setTransform(new Transform3D());
        tgC.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgC.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgC.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        Box cube = new Box(
            CubeConstants.DIMESIONS.x,
            CubeConstants.DIMESIONS.y,
            CubeConstants.DIMESIONS.z,
            Box.GENERATE_NORMALS | 
            Box.GEOMETRY_NOT_SHARED | 
            Box.ENABLE_GEOMETRY_PICKING, 
            appearance
        ); //temp the size can change to our liking
        cube.setCapability(Shape3D.ALLOW_PICKABLE_READ);
        cube.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
        cube.setPickable(true);
        tgC.addChild(cube);
        bgC.addChild(tgC);
        return bgC;
}

    
    public void rootCubeGroupAdd(){
        transformCubes.addChild(cubeGenerate(setAppearance()));
    }

    public void rootSphereGroupAdd(){
        transformSpheres.addChild(sphereGenerate(setAppearance()));
    }
    /**
     * 
     * @param appearance sets the appearance of the sphere
     * @return the sphere object
     */
    public BranchGroup sphereGenerate(Appearance appearance){
        BranchGroup bgS = new BranchGroup();
        bgS.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bgS.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bgS.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        TransformGroup tgS = new TransformGroup();
        tgS.setTransform(new Transform3D());
        tgS.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgS.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgS.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        Sphere sphere = new Sphere(0.09f, appearance); //temp the size can change to our liking.
        sphere.getShape(Sphere.BODY).setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);;
        sphere.setCapability(Sphere.ENABLE_GEOMETRY_PICKING);
        sphere.setCapability(Shape3D.ALLOW_PICKABLE_READ);
        sphere.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
        sphere.getShape(Sphere.BODY).setPickable(true);
        tgS.addChild(sphere);
        bgS.addChild(tgS);
        return bgS;
    }

    public Appearance setAppearance(){
        Appearance appearance = new Appearance();
        Material material = new Material(new Color3f(0.2f, 0.02f, 0.02f), new Color3f(0f, 0f, 0f), new Color3f(0.7f, 0.1f, 0.1f), new Color3f(1.0f, 1.0f, 1.0f), 75f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        return appearance;
    }
}


