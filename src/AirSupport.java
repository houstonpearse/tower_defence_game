import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

/**
 * a plane that files across the map from either the left or the top and drops explosives
 */
public class AirSupport extends Sprite {

    private static final Random RANDOM = new Random();
    private static final String AIR_SUPPORT_FILE = "res/images/airsupport.png";
    private static final Image IMAGE = new Image(AIR_SUPPORT_FILE);
    private static final int SPEED = 5;
    private static final int FINAL_X_POS = 1100;
    private static final int FINAL_Y_POS = 900;
    private static final int M_SEC_PER_SEC = 1000;
    private static final double ROTATION_OFFSET = Math.PI/2;

    private final DrawOptions rotation;
    private final Vector2 direction;
    private double timeUntil;

    /**
     * creates a new Air support plane
     * @param position the starting position
     * @param direction the movement direction
     */
    public AirSupport(Point position,Vector2 direction) {
        // set image and position in sprite class
        super(IMAGE, position);
        // set direction
        this.direction = direction;
        // set rotation drawOption
        rotation = new DrawOptions().setRotation(vectorToRadians(direction)+ROTATION_OFFSET);
        // set drop time
        setNextDropTime();
    }


    /**
     * updates the current position of the plane and the time until the
     * next explosive drop
     */
    @Override
    public void update() {
        // move position according to speed and timeScale
        positionAddVector(direction.mul(ShadowDefend.getTimeScale()*SPEED));
        // update timeUntil according to timeScale
        timeUntil-=ShadowDefend.getmSecondPerFrame()*ShadowDefend.getTimeScale();
        // release explosive if enough time has passed and chose next drop time
        if (timeUntil<=0.0) {
            World.getInstance().addSprite(new Explosive(getPosition()));
            setNextDropTime();
        }
    }

    /**
     * allow correct rotation of the plane to be drawn
     * @return rotation drawOption
     */
    @Override
    public DrawOptions getDrawOptions() {
        return rotation;
    }

    /**
     * checks if the plane has flown across the map and is completed
     * @return true if the plane is complete
     */
    public boolean isComplete() {
        // plane is complete if it has flown across the map
        return getPosition().x > FINAL_X_POS || getPosition().y > FINAL_Y_POS;
    }

    // randomly chooses next drop time between 1 and 2 seconds
    private void setNextDropTime() {
        // setting next drop time to be a number between 1 and 2 secs
        timeUntil = (RANDOM.nextDouble()+1)*M_SEC_PER_SEC;
    }

    /**
     * allows the image to be drawn onto the buy Panel and when placing
     * @return the air support image
     */
    public static Image getImage() {
        return IMAGE;
    }
}
