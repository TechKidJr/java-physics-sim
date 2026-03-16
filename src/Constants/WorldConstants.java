package Constants;


public class WorldConstants {

    // Simulation Constants
    public static final float G_FORCE = -9.81f;
    public static final float TIME_STEP = 1f/60f;
    public static final int MAX_SUB_STEPS = 7;

    // Air Resistance
    public static final float MOVING_AIR_RESISTANCE = 0.2f; //temp
    public static final float ROTATIONAL_AIR_RESISTANCE = 0.07f; //temp

    // Minimum and Maximum allowed movement of the objects.
    public static final float MIN_VELOCITY = 0.8f;
    public static final float ANGLE_THRESHOLD = 1f; 
    public static final float MAX_ROTATION = 100f;
    public static final float STOP_TIME = 2.0f; 

    public static final float SLIDE_FRICTION = 0.5f; //temp 
    public static final float ROLLING_FRICTION = 0.1f; //temp
    public static final float BOUNCINESS = 0.5f; //temp

    public static final float COLLISION_TOLERANCE = 0.04f; //temp
    public static final float BREAKING_THRESHOLD = 0.02f; //temp


    public static final int SOLVER_ITERATIONS = 20;

}

