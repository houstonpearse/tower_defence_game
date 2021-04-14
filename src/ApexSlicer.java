import bagel.Image;
import bagel.util.Point;

/**
 * an apex slicer is a type of enemy
 * it spawns mega slicers on its death
 */
public class ApexSlicer extends Enemy {

    private static final String FILENAME = "res/images/apexslicer.png";
    private static final Image IMAGE = new Image(FILENAME);
    private static final double SPEED = 0.75;
    private static final int REWARD = 150;
    private static final int HEALTH = 25;
    private static final int PENALTY = 16;
    private static final int CHILD_NUMBER = 4;

    /**
     * creates a new apex slicer
     * @param position where it is spawned
     * @param targetPosition what target it will move toward
     * @param targetIndex the index of the current target in the worlds current path
     */
    public ApexSlicer(Point position, Point targetPosition, int targetIndex) {
        super(IMAGE,SPEED,REWARD,HEALTH,PENALTY,CHILD_NUMBER,position,targetPosition,targetIndex);
    }

    /**
     * returns a child that would spawn on the death of the Apex slicer
     * this child has the same position,target, and target index as the apex slicer
     * @return a mega slicer
     */
    @Override
    public Enemy getChild(Point targetPosition,int targetIndex) {
        return new MegaSlicer(getPosition(),targetPosition,targetIndex);
    }
}