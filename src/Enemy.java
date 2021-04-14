import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/** an abstract class for an enemy. is alive or dead, has a position,
 * velocity and a path it attempts to traverse. an enemy traverses
 * the path by moving to each point in the path in order. an enemy dies
 * when it reaches the end of the path.
 */
public abstract class Enemy extends Sprite {

    private static final double MIN_DISTANCE = 20.0;

    private final double speed;
    private final int reward;
    private final int penalty;
    private final int numChildren;
    private int health;
    private Point targetPosition;
    private int targetIndex;
    private Vector2 direction;

    /**
     * constructor for an enemy
     * enemies can be created by the wave events or they can be spawned by their parents
     * */
    public Enemy(Image image,double speed,int reward,int penalty,int health,int numChildren,Point position,Point targetPosition,int targetIndex) {
        super(image,position);
        this.speed = speed;
        this.reward = reward;
        this.penalty = penalty;
        this.health = health;
        this.numChildren = numChildren;
        this.targetPosition = targetPosition;
        this.targetIndex = targetIndex;
        // unit vector from position to target
        direction = targetPosition.asVector().sub(getPosition().asVector()).normalised();
    }

    /**
     * the child that spawns of the death of the enemy depends on the type of enemy
     * the child will have the same position and targetPosition as its parent
     * @param targetPosition current target of the parent
     * @param targetIndex the index of the target in the worlds path
     * @return the child that is created
     */
    public abstract Enemy getChild(Point targetPosition,int targetIndex);

    /**
     * returns the drawOption object which determines how the image will be drawn
     * the enemies image should be orientated in the direction of travel
     * @return a drawOptions object that will rotate the image
     */
    @Override
    public DrawOptions getDrawOptions() {
        return new DrawOptions().setRotation(vectorToRadians(direction));
    }

    /**
     * updates the progress of the enemy as it traverses the worlds path
     */
    @Override
    public void update() {
        // if the enemy is at the current target we want to get a new targetPosition.
        // the enemy cannot have a new targetPosition if its at the end of the path
        if (atTargetPosition() && !isComplete()) {
            nextTargetPosition();
        }
        approachTarget();
    }

    /**
     * used by the world to spawn the children of the enemy if it has been killed by the player
     * the children will be spawned at the same position of the enemy
     */
    public void spawnChildren() {
        for (int i = 0; i < numChildren; i++) {
            World.getInstance().addSprite(getChild(targetPosition,targetIndex));
        }
    }

    /**
     * used by the world to determines if an enemy is dead or not
     * @return true if health is less than or equal to 0
     */
    public boolean isDead() {
        return health<=0;
    }

    /**
     * used by the world to test if an enemy has made it to the end of the path
     *
     * @return true if at final position in path
     */
    public boolean isComplete() {
        List<Point> path = World.getInstance().getPath();
        Point finalPos = path.get(path.size()-1);
        return getPosition().distanceTo(finalPos)<=MIN_DISTANCE;
    }

    /**
     * used by the world to reward the player for killing the enemy
     * @return money reward amount as an int
     */
    public int getReward() {
        return reward;
    }

    /**
     * used by the world to penalise player for failing to kill the enemy
     * @return penalty in lives as an int
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     * used by World class to deal damage to the enemy
     * @param damage is the amount of enemy health reduced
     */
    public void decreaseHealth(int damage) {
        this.health -= damage;
    }

    // test if within MIN_DISTANCE to target
    private boolean atTargetPosition() {
        return getPosition().distanceTo(targetPosition)<=MIN_DISTANCE;
    }

    // moves closer to target position based on universal timescale and speed
    private void approachTarget() {
        // check whether step size is longer than the distance to the target
        double distanceToTarget = getPosition().distanceTo(targetPosition);
        if (ShadowDefend.getTimeScale()*speed>=distanceToTarget) {
            // move to target
            positionAddVector(direction.mul(distanceToTarget));
        } else {
            // move closer to target
            positionAddVector(direction.mul(ShadowDefend.getTimeScale() * speed));
        }
        // our position changed so we need to that change to be reflected in the direction vector
        updateDirection();
    }

    // changes target to next available and updates direction
    private void nextTargetPosition() {
        // set the next target
        targetIndex++;
        targetPosition = World.getInstance().getPath().get(targetIndex);
        // our target changed so we need that change to be reflected in the direction vector
        updateDirection();
    }

    // changes the direction to be a unit vector in the direction of the target from the current position
    private void updateDirection() {
        direction = targetPosition.asVector().sub(getPosition().asVector()).normalised();
    }

}