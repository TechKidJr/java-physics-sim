import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.picking.behaviors.PickRotateBehavior;
import org.jogamp.java3d.utils.picking.behaviors.PickTranslateBehavior;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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
import org.jogamp.vecmath.Matrix3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;

import Constants.CubeConstants;
import Constants.WorldConstants;

public class Renderer {

    private BranchGroup root;
    private TransformGroup transformCubes;
    private TransformGroup transformSpheres;
    private List<Vector3f> objectSpawns;
    private List<PhysicsShape> objects;
    private PickRotateBehavior pickRotate;
    private PickTranslateBehavior pickMove;
    private Physics physics;
    private Appearance appearance;
    /**
     * renders the objects that are going to be used in the physics sim.
     * @param canvas the drawing field of the window.
     * @return the branchgroup that holds all of the objects.
     */
    public BranchGroup render(Canvas3D canvas, Physics physics){

        root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        root.setCapability(BranchGroup.ALLOW_DETACH);

        this.physics = physics;

        Point3d boundaryCenter = new Point3d(0, 0, 0.0);
        BoundingSphere bounds = new BoundingSphere(boundaryCenter, 1000d);
        
        pickMove = new PickTranslateBehavior(root, canvas, bounds);
        pickRotate = new PickRotateBehavior(root, canvas, bounds);


        pickMove.setMode(PickTool.BOUNDS);
        pickRotate.setMode(PickTool.BOUNDS);

        transformCubes = new TransformGroup();
        objectTransformConfigs(transformCubes);

        transformSpheres = new TransformGroup();
        objectTransformConfigs(transformSpheres);
        objects = new ArrayList<>();
        objectSpawns = new ArrayList<>();

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
     * @return the Branchgroup which holds the transform group of the cube.
     */
    private BranchGroup cubeGenerate(Appearance appearance){
        BranchGroup bgC = new BranchGroup();
        bgC.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bgC.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bgC.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        TransformGroup tgC = new TransformGroup();
        tgC.setTransform(new Transform3D());
        tgC.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgC.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgC.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        Transform3D rotate = new Transform3D();
        double rotX = randomCubeDegree()[0];
        double rotY = randomCubeDegree()[1];
        rotate.rotX(rotX); // rotates the cube by 45 degrees
        rotate.rotY(rotY);
        Matrix3f rM = new Matrix3f();
        rotate.get(rM);
        float[] rotateArray = new float[]{rM.m00, rM.m01, rM.m02, rM.m10, rM.m11, rM.m12, rM.m20, rM.m21, rM.m22};
        Transform3D cubeTranslation= new Transform3D();
        Vector3f spawn = randomSpawnPoint();
        float[] rigidSpawn = new float[]{spawn.x, spawn.y, spawn.z};
        cubeTranslation.setTranslation(spawn);
        cubeTranslation.mul(rotate);
        tgC.setTransform(cubeTranslation);
        Box cube = new Box(
            CubeConstants.DIMESIONS.x,
            CubeConstants.DIMESIONS.y,
            CubeConstants.DIMESIONS.z,
            Box.GENERATE_NORMALS | 
            Box.GEOMETRY_NOT_SHARED | 
            Box.ENABLE_GEOMETRY_PICKING, 
            appearance
        ); //temp the size can change to our liking
        RigidBody boxRigidBody = physics.createRigidBox(rigidSpawn, rotateArray);
        cube.setCapability(Shape3D.ALLOW_PICKABLE_READ);
        cube.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
        cube.setPickable(true);
        tgC.addChild(cube);
        tgC.setUserData(boxRigidBody);
        bgC.addChild(tgC);
        objectSpawns.add(spawn);
        PhysicsShape shape = new PhysicsShape(tgC, boxRigidBody);
        objects.add(shape);
        return bgC;
    }

    /**
     * adds the cube branch group to the transform group
     */
    public void rootCubeGroupAdd(){
        transformCubes.addChild(cubeGenerate(setAppearance()));
    }

    /**
     * Adds the sphere branch group to the transform group
     */
    public void rootSphereGroupAdd(){
        transformSpheres.addChild(sphereGenerate(setAppearance()));
    }
    /**
     * 
     * @param appearance sets the appearance of the sphere
     * @return the sphere object
     */
    private BranchGroup sphereGenerate(Appearance appearance){
        BranchGroup bgS = new BranchGroup();
        bgS.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bgS.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bgS.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        TransformGroup tgS = new TransformGroup();
        tgS.setTransform(new Transform3D());
        tgS.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgS.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgS.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        Transform3D sphereTranslation = new Transform3D();
        Vector3f spawn = randomSpawnPoint();
        float[] spawnBody = {spawn.x, spawn.y, spawn.z};
        sphereTranslation.setTranslation(spawn);
        tgS.setTransform(sphereTranslation); 
        Sphere sphere = new Sphere(0.09f, appearance); //temp the size can change to our liking.
        sphere.getShape(Sphere.BODY).setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);;
        sphere.setCapability(Sphere.ENABLE_GEOMETRY_PICKING);
        sphere.setCapability(Shape3D.ALLOW_PICKABLE_READ);
        sphere.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
        sphere.getShape(Sphere.BODY).setPickable(true);
        RigidBody sphereRigidBody = physics.createRigidSphere(spawnBody);
        tgS.addChild(sphere);
        tgS.setUserData(sphereRigidBody);
        bgS.addChild(tgS);
        objectSpawns.add(spawn);
        return bgS;
    }

    //TODO: This is a mock test method until @TechKidJr figures out a way to spawn objects using the mouse (DELETE MESSAGE ONCE DONE)
    public Vector3f randomSpawnPoint(){
        int attempts = 0;
        Random random = new Random();
        Vector3f position = null;
        boolean isClose = false;
        do {
            isClose = false;
            float xPos = random.nextFloat(-0.73f, 0.55f);
            float yPos = random.nextFloat(-0.35f, 0.4f);
            position = new Vector3f(xPos, yPos, 0);

            for (Vector3f objectSpawn:objectSpawns){
                Vector3f distanceVector = new Vector3f();
                distanceVector.sub(objectSpawn, position);
                if (distanceVector.lengthSquared() <= WorldConstants.MIN_DISTANCE){
                    attempts++;
                    isClose = true;
                    position = new Vector3f();
                    break;
                }
            }
        } while (isClose && attempts <= 100);

        return isClose ? null : position;
    }

    public double[] randomCubeDegree() {
        Random random = new Random();
        double xRot = random.nextDouble(45f, 135f);
        double yRot = random.nextDouble(45f, 135f);
        double[] rotation = {xRot, yRot};
        return rotation;
    }

    private void objectTransformConfigs(TransformGroup transformGroup){
        transformGroup.setTransform(new Transform3D());
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
    }

    public void updatePositions(){
        for (PhysicsShape object:objects){
            object.setPosition();
        }
    }

    private Appearance setAppearance(){
        appearance = new Appearance();
        Material material = new Material(new Color3f(0.2f, 0.02f, 0.02f), new Color3f(0f, 0f, 0f), new Color3f(0.7f, 0.1f, 0.1f), new Color3f(1.0f, 1.0f, 1.0f), 75f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        return appearance;
    }
}


