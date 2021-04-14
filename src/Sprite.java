import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * sprite class the base class for all game objects
 * all objects have a position and an image that is drawn
 */
public abstract class Sprite {

    private final Image image;
    private Point position;

    /**
     * sets the image and position of a new game object
     * @param image is the image that will be draw in game
     * @param position is the current position of the game object
     */
    public Sprite(Image image, Point position) {
        this.image = image;
        this.position = position;
    }

    /**
     * updates the current state of the sprite
     */
    public abstract void update();

    /**
     * allows the sprite to have specific draw requirements
     * @return drawOptions object used to specify how the image should be drawn
     */
    public abstract DrawOptions getDrawOptions();

    /**
     * draws the image at its position with its draw options
     */
    public void draw() {
        DrawOptions drawOptions = getDrawOptions();
        if (drawOptions == null) {
            image.draw(position.x, position.y);
        } else {
            image.draw(position.x, position.y,drawOptions);
        }
    }

    /**
     * adds a vector to the position to move it
     * @param vector the vector to be added to the position
     */
    public void positionAddVector(Vector2 vector) {
        position = position.asVector().add(vector).asPoint();
    }

    /**
     * uses a.b = |a||b|cos(x) to find the angle between the vector and the +x-axis
     * @param vector the vector we are using to find the angle
     * @return double representing the angle in radians
     */
    public double vectorToRadians(Vector2 vector) {
        // we need a unit vector for the equation a.b = |a||b|cos(x)
        Vector2 direction = vector.normalised();
        double angle = Math.acos(direction.dot(Vector2.right));
        // angles below the x-axis are negative
        if (direction.asPoint().y<0) {
            angle*=-1;
        }
        return angle;
    }

    /**
     * gives information about the current position of the game object
     * @return a Point which is the current position of the game object
     */
    public Point getPosition() {
        return position;
    }

    /**
     * used by the world to get information about the area the sprite takes up in the world
     * this enables us to avoid placing towers on top of each other
     * @return a rectangle that specifies what area the sprite takes up on the map
     */
    public Rectangle getBox() {
        return image.getBoundingBoxAt(position);
    }

}
