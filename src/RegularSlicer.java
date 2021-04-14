import bagel.Image;
import bagel.util.Point;

/**
 * regular sliver is a type of enemy.
 * it spawns no children on its death
 */
public class RegularSlicer extends Enemy {

    private static final String FILENAME = "res/images/slicer.png";
    private static final Image IMAGE = new Image(FILENAME);
    private static final double SPEED = 2.0;
    private static final int REWARD = 2;
    private static final int HEALTH = 1;
    private static final int PENALTY = 1;
    private static final int CHILD_NUMBER = 0;

    /**
     * creates a new regular slicer
     * @param position where it is spawned
     * @param targetPosition what target it will move toward
     * @param targetIndex the index of the current target in the worlds current path
     */
    public RegularSlicer(Point position, Point targetPosition, int targetIndex) {
        super(IMAGE,SPEED,REWARD,HEALTH,PENALTY,CHILD_NUMBER,position,targetPosition,targetIndex);
    }

    /**
     * returns the child that would spawn on the death of the regular slicer
     * @return null since the regular slicer doesn't spawn any enemies
     */
    @Override
    public Enemy getChild(Point targetPosition,int targetIndex) {
        return null;
    }
}
