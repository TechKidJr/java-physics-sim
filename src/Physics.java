import java.util.List;

import javax.swing.Timer;
import javax.vecmath.Vector3f;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import Constants.WorldConstants;


public class Physics{
    public DiscreteDynamicsWorld dWorld;
    private Renderer render;
    private CollisionShape floor;
    private CollisionShape ceiling;
    private CollisionShape leftWall;
    private CollisionShape rightwall;
    private List<PhysicsShape> shapes;

    public Physics(Renderer render){

        this.render = render;
        // World configs
        DefaultCollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatch = new CollisionDispatcher(collisionConfig);
        BroadphaseInterface phasePair = new DbvtBroadphase();
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        //Dynamics world object
        dWorld = new DiscreteDynamicsWorld(dispatch , phasePair, solver, collisionConfig);

        //Setting the gravity
        Vector3f gravity = new Vector3f(0f, WorldConstants.G_FORCE, 0f);
        dWorld.setGravity(gravity);

        
        floor = new BoxShape(new Vector3f(2f, 0.1f, 2f));
        ceiling = new BoxShape(new Vector3f(2f, 0.1f,2f));
        leftWall = new BoxShape(new Vector3f(0.1f,1f,2f));
        rightwall = new BoxShape(new Vector3f(0.1f, 1f, 2f));

        //floor configs
        Transform floorTransform = new Transform();
        floorTransform.setIdentity();
        floorTransform.origin.set(0, -0.6f, 0);
        MotionState fMotionState = new DefaultMotionState(floorTransform);
        RigidBodyConstructionInfo floorConfig = new RigidBodyConstructionInfo(0f, fMotionState, floor);
        RigidBody floorRigidBody = new RigidBody(floorConfig);

        //ceiling configs
        Transform ceilingTransform = new Transform();
        ceilingTransform.setIdentity();
        ceilingTransform.origin.set(0, 0.6f, 0);
        MotionState cMotionState = new DefaultMotionState(ceilingTransform);
        RigidBodyConstructionInfo ceilingInfo = new RigidBodyConstructionInfo(0f, cMotionState, ceiling);
        RigidBody ceilingRigidBody = new RigidBody(ceilingInfo);

        //left wall configs
        Transform lWallTransform = new Transform();
        lWallTransform.setIdentity();
        lWallTransform.origin.set(-1f, 0, 0);
        MotionState lMotionState = new DefaultMotionState(lWallTransform);
        RigidBodyConstructionInfo lWallConfigs = new RigidBodyConstructionInfo(0f, lMotionState, leftWall, new Vector3f(0, 0, 0));
        RigidBody leftWallBody = new RigidBody(lWallConfigs);

        //right wall configs
        Transform rWallTransform = new Transform();
        rWallTransform.setIdentity();
        rWallTransform.origin.set(1f, 0, 0);
        MotionState rMotionState = new DefaultMotionState(rWallTransform);
        RigidBodyConstructionInfo rWallConfig = new RigidBodyConstructionInfo(0f, rMotionState, rightwall);
        RigidBody rightWallBody = new RigidBody(rWallConfig);

        //Adding objects to the dynamics world
        dWorld.addRigidBody(floorRigidBody);
        dWorld.addRigidBody(ceilingRigidBody);
        dWorld.addRigidBody(leftWallBody);
        dWorld.addRigidBody(rightWallBody);        
    }

    /**
     * Updates the physics in the game loop.
     */
    public void update(){
        final long[] previousTime = {System.nanoTime()};
        new Timer(16, e -> {
            long[] currentTime = {System.nanoTime()};
            float deltaTimeInSeconds = ((float)(currentTime[0] - previousTime[0]))/WorldConstants.NANO_IN_SECONDS;
            if (deltaTimeInSeconds > 1f/6f){
                deltaTimeInSeconds = 0.12f;
                dWorld.stepSimulation(deltaTimeInSeconds, WorldConstants.MAX_SUB_STEPS, WorldConstants.TIME_STEP);
                render.updatePositions();
            }
            else {
                dWorld.stepSimulation(deltaTimeInSeconds, WorldConstants.MAX_SUB_STEPS, WorldConstants.TIME_STEP);
                render.updatePositions();
            }
            previousTime[0] = currentTime[0];
        }).start();
        
    }

    /**
     * Creates a collision shape first, then attaches that to a rigid body.
     * @param spawn the point the rigid body spawns at.
     * @param rotationMatrix How it is rotated when it spawns in.
     * @return the rigid body
     */
    public RigidBody createRigidBox(float[] spawn, float[] rotationMatrix){
        CollisionShape boxShape = new BoxShape(new Vector3f(0.09f, 0.09f, 0.09f));
        boxShape.setMargin(WorldConstants.COLLISION_TOLERANCE);
        Transform boxTransform = new Transform();
        boxTransform.setIdentity();
        boxTransform.origin.set(new Vector3f(spawn));
        boxTransform.basis.set(rotationMatrix);
        MotionState boxState = new DefaultMotionState(boxTransform);
        Vector3f localInertia = new Vector3f(0.0054f, 0.0054f, 0.0054f); //Inertia of each dimension of a cube is calculated by I = 1/3*Mass*dimension^2\
        RigidBodyConstructionInfo boxConfig = new RigidBodyConstructionInfo(1f, boxState, boxShape, localInertia);
        RigidBody boxRigidBody = new RigidBody(boxConfig);
        boxRigidBody.setDamping(WorldConstants.LINEAR_AIR_RESISTANCE, WorldConstants.ROTATIONAL_AIR_RESISTANCE);
        boxRigidBody.setFriction(WorldConstants.SLIDE_FRICTION);
        boxRigidBody.setRestitution(WorldConstants.Restitution);
        boxRigidBody.setDeactivationTime(WorldConstants.STOP_TIME);
        boxRigidBody.activate();
        dWorld.addRigidBody(boxRigidBody);
        return boxRigidBody;
    }

    /**
     * Creates a collision shape for the sphere first, then attaches that to a rigid body.
     * @param spawn the point the rigid body spawns at.
     * @return the rigid body.
     */ 
    public RigidBody createRigidSphere(float[] spawn){
        CollisionShape sphereShape = new SphereShape(0.09f);
        sphereShape.setMargin(WorldConstants.COLLISION_TOLERANCE);
        Transform sphereTransform = new Transform();
        sphereTransform.setIdentity();
        sphereTransform.origin.set(new Vector3f(spawn));
        MotionState sphereState = new DefaultMotionState(sphereTransform);
        Vector3f localInertia = new Vector3f(0.00324f, 0.00324f, 0.00324f);
        RigidBodyConstructionInfo sphereConfig = new RigidBodyConstructionInfo(1, sphereState, sphereShape, localInertia);
        RigidBody sphereRigidBody = new RigidBody(sphereConfig);
        sphereRigidBody.setDamping(WorldConstants.LINEAR_AIR_RESISTANCE, WorldConstants.ROTATIONAL_AIR_RESISTANCE);
        sphereRigidBody.setFriction(WorldConstants.ROLLING_FRICTION);
        sphereRigidBody.setRestitution(WorldConstants.Restitution);
        sphereRigidBody.setDeactivationTime(WorldConstants.STOP_TIME);
        sphereRigidBody.activate();
        dWorld.addRigidBody(sphereRigidBody);
        return sphereRigidBody;

    }

    public void usrCtrledGravity(float gravity){
        Vector3f gForce = new Vector3f(0, gravity, 0);
        dWorld.setGravity(gForce);
        syncList();
        for (PhysicsShape shape:shapes){
            RigidBody rigidBody = shape.getRigidBody();
            rigidBody.activate(true);
            rigidBody.applyGravity();
        }
    }

    public void usrCtrledAirResistance(float resistance){
        syncList();
        for (PhysicsShape shape:shapes){
            RigidBody rigidBody = shape.getRigidBody();
            rigidBody.activate(true);
            rigidBody.applyDamping(resistance);
        }
    }

    public void syncList(){
        this.shapes = render.getObjects();
    }
      /**
     * creates the collision affect if two objects are colliding.
     */
    public void collisionAffect(){
        Dispatcher dispatcher = dWorld.getDispatcher();
        int numManiFolds = dispatcher.getNumManifolds();

        for (int i = 0; i < numManiFolds; i++){
            PersistentManifold pManifold = dispatcher.getManifoldByIndexInternal(i);
            float totalImpulse = 0f;

            for (int j = 0; j < pManifold.getNumContacts(); j++){
                totalImpulse += pManifold.getContactPoint(j).appliedImpulse;
            }

            if (totalImpulse > 400){
                ManifoldPoint mPoint = pManifold.getContactPoint(0);
                Vector3f pos = new Vector3f();
                mPoint.getPositionWorldOnA(pos);
            }

        

        }

    }


    /**
     * Gets the floor object
     * @return the floor object
     */
    public CollisionShape getFloor(){
        return floor;
    }

    /**
     * Gets the ceiling object
     * @return the ceiling object
     */
    public CollisionShape getCeiling(){
        return ceiling;
    }

    /**
     * Gets the left wall object
     * @return the left wall
     */
    public CollisionShape getLeftWall(){
        return leftWall;
    }

    /**
     * Gets the right wall object
     * @return the right wall
     */
    public CollisionShape getRightWall(){
        return rightwall;
    }

}
