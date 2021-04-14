import bagel.Image;
import bagel.util.Point;

/**
 * a tank is a type of active tower that shoots tank projectiles
 * it has a 1 second delay and a effect radius of 100 pixels
 */
public class Tank extends ActiveTower {

    private static final String TANK_FILE= "res/images/tank.png";
    private static final Image IMAGE = new Image(TANK_FILE);
    private static final double PROJECTILE_DELAY = 1000.0;
    private static final double EFFECT_RADIUS = 100.0;

    /**
     * constructor for a tank
     * @param position the position on the map the tank is located
     */
    public Tank(Point position) {
        super(IMAGE, position, EFFECT_RADIUS, PROJECTILE_DELAY);
    }


    /**
     * creates a new tank projectile
     * used by active tower super class to fire a projectile when its appropriate
     * @return the projectile
     */
    @Override
    public Projectile newProjectile() {
        return new TankProjectile(getPosition(),getTarget());
    }

    /**
     * allows the image to be drawn onto the buy Panel and when placing
     * @return the tank image
     */
    public static Image getImage() {
        return IMAGE;
    }
}
