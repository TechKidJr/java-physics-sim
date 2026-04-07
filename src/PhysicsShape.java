import javax.vecmath.*;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.Quat4f;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class PhysicsShape {
    private TransformGroup transformGroup;
    private RigidBody objectRigidBody;
    private Transform transform;
    private Transform3D transform3d;
    private float posX;
    private float posY;
    private float posZ;
    private javax.vecmath.Quat4f rotationMatrix;
    private Quat4f rotation;
    private Vector3f location;

    public PhysicsShape(TransformGroup transformGroup, RigidBody objectRigidBody){
        this.transformGroup = transformGroup;
        this.objectRigidBody = objectRigidBody;
        transform = new Transform();
        transform3d = new Transform3D();
        rotation = new Quat4f();
        rotationMatrix = new javax.vecmath.Quat4f();
        location = new Vector3f();
    }

    /**
     * Sets the position of the graphical cube based on its corresponding Rigid Body.
     */
    public void setPosition(){
        objectRigidBody.getMotionState().getWorldTransform(transform);
        posX = transform.origin.x;
        posY = transform.origin.y;
        posZ = transform.origin.z;
        transform.getRotation(rotationMatrix);
        location.set(posX, posY, posZ);
        transform3d.setTranslation(location);
        rotation.x = rotationMatrix.x;
        rotation.y = rotationMatrix.y;
        rotation.z = rotationMatrix.z;
        rotation.w = rotationMatrix.w;
        transform3d.setRotation(rotation);
        transformGroup.setTransform(transform3d);
    }
}
