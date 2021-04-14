import bagel.Image;
import bagel.util.Point;

/**
 * super tank projectile is a type of projectile shot by a super tank that deals 3 damage
 */
public class SuperTankProjectile extends Projectile {

    private static final String SUPER_TANK_PROJECTILE_FILE = "res/images/supertank_projectile.png";
    private static final int SUPER_TANK_DAMAGE = 3;
    private static final Image IMAGE = new Image(SUPER_TANK_PROJECTILE_FILE);

    /**
     * constructor for projectile shot by super tank
     * @param position is where the projectile originates
     * @param targetEnemy the enemy that the projectile will move towards
     */
    public SuperTankProjectile(Point position, Enemy targetEnemy) {
        super(IMAGE, position, SUPER_TANK_DAMAGE, targetEnemy);
    }
}
