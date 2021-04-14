import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * an active tower targets enemies and shoots projectiles at them
 */
public abstract class ActiveTower extends Sprite {

    private static final double ROTATION_OFFSET = Math.PI/2;

    private Enemy target;
    private Vector2 direction;
    private final double radius;
    private final double projectileDelay;
    private double timeUntil;

    /**
     * creates a new Active Tower at the specified position
     */
    public ActiveTower(Image image, Point position,double radius,double projectileDelay) {
        super(image, position);
        this.radius = radius;
        this.projectileDelay = projectileDelay;
        target = World.getInstance().findClosestEnemy(getPosition(),radius);
        direction = Vector2.up;
        timeUntil = projectileDelay;

    }

    /**
     * creates a new projectile the type of projectile depends on the tower
     * @return a new projectile that will be fired at the enemy
     */
    public abstract Projectile newProjectile();

    /**
     * updates the active towers state by..
     * updating the timeUntil a projectile is launched
     * launching a projectile at the target if possible
     */
    @Override
    public void update() {
        // account for passed time
        timeUntil-=ShadowDefend.getTimeScale()*ShadowDefend.getmSecondPerFrame();
        // if current target is not valid find a new one
        if (target == null || target.isDead() || target.isComplete() || distanceToTarget()>radius) {
            target = World.getInstance().findClosestEnemy(getPosition(),radius);
            if (target==null) return;
        }
        // update the direction so we can draw it properly
        updateDirection();
        // if enough time has passed fire at the target
        if (timeUntil<=0.0) {
            World.getInstance().addSprite(newProjectile());
            timeUntil = projectileDelay;
        }
    }

    /**
     * used to pass specific draw instructions to sprite superclass
     * @return drawOptions with accounting for the ActiveTowers rotation
     */
    @Override
    public DrawOptions getDrawOptions() {
        return new DrawOptions().setRotation(vectorToRadians(direction)+ROTATION_OFFSET);
    }

    /**
     * used by subclass when firing a projectile towards the target enemy
     * @return the target enemy
     */
    public Enemy getTarget() {
        return target;
    }

    // is used by the public update method to determine if the current target is valid
    // is only called once we are certain that we currently have a target
    private double distanceToTarget() {
        return target.getPosition().distanceTo(getPosition());
    }

    // is called by the public update() method to update the direction that the tower is facing
    // is only called once we are certain that we have a target
    private void updateDirection() {
        direction = target.getPosition().asVector().sub(getPosition().asVector()).normalised();
    }

}
