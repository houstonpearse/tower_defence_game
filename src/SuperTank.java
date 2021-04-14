import bagel.Image;
import bagel.util.Point;

/**
 * a super tank is a type of active tower that shoots superTank projectiles
 * it has a cool down of 1/2 of a second and a effect radius of 150 pixels
 */
public class SuperTank extends ActiveTower {

    private static final String SUPER_TANK_FILE= "res/images/supertank.png";
    private static final Image IMAGE = new Image(SUPER_TANK_FILE);
    private static final double PROJECTILE_DELAY = 500.0;
    private static final double EFFECT_RADIUS = 150.0;

    /**
     * constructor for a superTank
     * @param position the position on the map the tank is located
     */
    public SuperTank(Point position) {
        super(IMAGE, position, EFFECT_RADIUS, PROJECTILE_DELAY);
    }

    /**
     * creates a new SuperTankProjectile
     * used by active tower super class to fire a projectile when its appropriate
     * @return a superTankProjectile
     */
    @Override
    public Projectile newProjectile() {
        return new SuperTankProjectile(getPosition(),getTarget());
    }

    /**
     * allows the image to be drawn onto the buy Panel and when placing
     * @return the super tank image
     */
    public static Image getImage() {
        return IMAGE;
    }

}
