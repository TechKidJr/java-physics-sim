import javax.vecmath.Vector3f;

import com.bulletphysics.*;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.BroadphasePair;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import Constants.WorldConstants;


public class Physics {
    DiscreteDynamicsWorld dWorld;

    public void physics(){

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
        StaticPlaneShape floor = new StaticPlaneShape(new Vector3f(0, -10, 0), 0);
        StaticPlaneShape ceiling = new StaticPlaneShape(new Vector3f(0, 10, 0), 0);
        BoxShape leftWall = new BoxShape(new Vector3f(-10,0,0));
        BoxShape rightwall = new BoxShape(new Vector3f(10, 0, 0));
        
        
        
    }

}
