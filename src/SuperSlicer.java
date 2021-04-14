import bagel.Image;
import bagel.util.Point;

/**
 * a super slicer is a type of enemy
 * it spawns regular slicers on its death
 */
public class SuperSlicer extends Enemy {

    private static final String FILENAME = "res/images/superslicer.png";
    private static final Image IMAGE = new Image(FILENAME);
    private static final double SPEED = 1.5;
    private static final int REWARD = 15;
    private static final int HEALTH = 1;
    private static final int PENALTY = 2;
    private static final int CHILD_NUMBER = 2;

    /**
     * creates a new super slicer
     * @param position where it is spawned
     * @param targetPosition what target it will move toward
     * @param targetIndex the index of the current target in the worlds current path
     */
    public SuperSlicer(Point position, Point targetPosition, int targetIndex) {
        super(IMAGE,SPEED,REWARD,HEALTH,PENALTY,CHILD_NUMBER,position,targetPosition,targetIndex);
    }


    /**
     * returns the child that would spawn on the death of the super slicer
     * this child has the same position,target, and target index as the super slicer
     * @return a regular slicer
     */
    @Override
    public Enemy getChild(Point targetPosition,int targetIndex) {
        return new RegularSlicer(getPosition(),targetPosition,targetIndex);
    }
}
