import java.sql.Time;

import javax.swing.Timer;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import Constants.WorldConstants;


public class Physics{
    private DiscreteDynamicsWorld dWorld;
    private CollisionShape floor;
    private CollisionShape ceiling;
    private CollisionShape leftWall;
    private CollisionShape rightwall;

    public void init(){

        // World configs
        DefaultCollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatch = new CollisionDispatcher(collisionConfig);
        BroadphaseInterface phasePair = new DbvtBroadphase();
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        //Dynamics world object
        dWorld = new DiscreteDynamicsWorld(dispatch , phasePair, solver, collisionConfig);

        //Setting the gravity
        // Vector3f gravity = new Vector3f(0f, WorldConstants.G_FORCE, 0f);
        // dWorld.setGravity(gravity);

        
        floor = new BoxShape(new Vector3f(2f, 0.1f, 2f));
        ceiling = new BoxShape(new Vector3f(2f, 0.1f,2f));
        leftWall = new BoxShape(new Vector3f(0.1f,1f,2f));
        rightwall = new BoxShape(new Vector3f(0.1f, 1f, 2f));

        //floor configs
        Transform floorTransform = new Transform();
        floorTransform.setIdentity();
        floorTransform.origin.set(0, -1f, 0);
        MotionState fMotionState = new DefaultMotionState(floorTransform);
        RigidBodyConstructionInfo floorConfig = new RigidBodyConstructionInfo(0f, fMotionState, floor);
        RigidBody floorRigidBody = new RigidBody(floorConfig);

        //ceiling configs
        Transform ceilingTransform = new Transform();
        ceilingTransform.setIdentity();
        ceilingTransform.origin.set(0, 1f, 0);
        MotionState cMotionState = new DefaultMotionState(ceilingTransform);
        RigidBodyConstructionInfo ceilingInfo = new RigidBodyConstructionInfo(0f, cMotionState, ceiling, new Vector3f(0, 0, 0));
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
            }
            else {
                dWorld.stepSimulation(deltaTimeInSeconds, WorldConstants.MAX_SUB_STEPS, WorldConstants.TIME_STEP);
            }
            previousTime[0] = currentTime[0];
        }).start();
        
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
