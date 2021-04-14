import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

/** a projectile is launched by an active tower at a target enemy
 * the projectile always moves towards the enemy at a given speed until it hits the enemy
 */
public abstract class Projectile extends DamageObject {

    private static final double MIN_DISTANCE = 10.0;
    private static final double SPEED = 10;
    private final Enemy targetEnemy;


    /**
     * constructor used by specific projectile sub-classes
     */
    public Projectile(Image image, Point position, int damage, Enemy targetEnemy) {
        super(image, position, damage);
        this.targetEnemy = targetEnemy;
    }

    /** updates the progress of the projectile
     * its direction will be updated so it always moves toward the target
     * its position will move closer to the enemy
     */
    @Override
    public void update() {
        //get unit vector from position in the direction of the target Enemy
        Vector2 direction = targetEnemy.getPosition().asVector().sub(getPosition().asVector()).normalised();

        // check whether step size is longer than the distance to the enemy
        double distanceToEnemy = targetEnemy.getPosition().distanceTo(getPosition());
        if (ShadowDefend.getTimeScale()*SPEED>=distanceToEnemy) {
            positionAddVector(direction.mul(distanceToEnemy));
        } else {
            // move closer to target
            positionAddVector(direction.mul(ShadowDefend.getTimeScale() * SPEED));

        }
    }

    /**
     * checks if the projectile has completed its required task
     * @return true if the enemy is already dead or has collided with the enemy
     */
    @Override
    public boolean isComplete() {
        return targetEnemy.isDead() || targetEnemy.getPosition().distanceTo(getPosition()) <= MIN_DISTANCE;
    }

    /**
     * a projectile has no special draw requirements
     * @return null to specify no draw requirements
     */
    @Override
    public DrawOptions getDrawOptions() {
        return null;
    }

    /**
     * returns the target enemy so we can decrease its health when the projectile is completed
     * @return the target
     */
    public Enemy getTargetEnemy() {
        return targetEnemy;
    }
}
