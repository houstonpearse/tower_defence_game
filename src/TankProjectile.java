import bagel.Image;
import bagel.util.Point;

/**
 * a type of projectile shot by a tank
 */
public class TankProjectile extends Projectile {

    private static final String TANK_PROJECTILE_FILE = "res/images/tank_projectile.png";
    private static final int TANK_PROJECTILE_DAMAGE = 1;
    private static final Image IMAGE = new Image(TANK_PROJECTILE_FILE);

    /**
     * constructor for projectile shot by tank
     * @param position is where the projectile originates
     * @param targetEnemy the enemy that the projectile will move towards
     */
    public TankProjectile(Point position, Enemy targetEnemy) {
        super(IMAGE, position, TANK_PROJECTILE_DAMAGE, targetEnemy);
    }
}
