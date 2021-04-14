import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;

/**
 * a type of damage object that deals damage to all enemies within its radius
 */
public class Explosive extends DamageObject {

    private static final String EXPLOSIVE_FILE = "res/images/explosive.png";
    private static final int EXPLOSIVE_DAMAGE = 500;
    private static final double EXPLOSIVE_RADIUS = 200.0;
    private static final double EXPLOSIVE_DELAY = 2;
    private static final int M_SEC_PER_SEC = 1000;
    private double mSecondsLeft;


    /**
     * new explosive that will explode in 2 seconds
     * @param position the point where the explosive was dropped on the map
     */
    public Explosive(Point position) {
        super(new Image(EXPLOSIVE_FILE), position, EXPLOSIVE_DAMAGE);
        mSecondsLeft = EXPLOSIVE_DELAY*M_SEC_PER_SEC;
    }

    /**
     * increases the time elapsed
     */
    @Override
    public void update() {
        mSecondsLeft-=ShadowDefend.getmSecondPerFrame()*ShadowDefend.getTimeScale();
    }

    /**
     * a explosive has no specific draw requirements
     * @return null to indicate no specific requirements
     */
    @Override
    public DrawOptions getDrawOptions() {
        return null;
    }

    /**
     * used by world to determine if the explosive is ready to explode
     * @return true if it is ready to explode
     */
    @Override
    public boolean isComplete() {
        return mSecondsLeft<=0.0;
    }

    /**
     * used by the world so it can damage all enemies within this radius
     */
    public static double getRadius() {
        return EXPLOSIVE_RADIUS;
    }
}

