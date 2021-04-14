import bagel.Image;
import bagel.util.Point;

/**
 * a mega slicer is a type of enemy
 * it spawns super slicers on its death
 */
public class MegaSlicer extends Enemy {

    private static final String FILENAME = "res/images/megaslicer.png";
    private static final Image IMAGE = new Image(FILENAME);
    private static final double SPEED = 1.25; // should be 1.5 but it helps enemies split apart
    private static final int REWARD = 10;
    private static final int HEALTH = 2;
    private static final int PENALTY = 4;
    private static final int CHILD_NUMBER = 2;

    /**
     * creates a new mega slicer
     * @param position where it is spawned
     * @param targetPosition what target it will move toward
     * @param targetIndex the index of the current target in the worlds current path
     */
    public MegaSlicer(Point position, Point targetPosition, int targetIndex) {
        super(IMAGE,SPEED,REWARD,HEALTH,PENALTY,CHILD_NUMBER,position,targetPosition,targetIndex);
    }


    /**
     * returns the child that would spawn on the death of the mega slicer
     * this child has the same position,target, and target index as the mega slicer
     * @return a super slicer
     */
    @Override
    public Enemy getChild(Point targetPosition,int targetIndex) {
        return new SuperSlicer(getPosition(),targetPosition,targetIndex);
    }
}