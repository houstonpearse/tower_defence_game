import bagel.Image;
import bagel.util.Point;

/**
 * an abstract class for a damage object that exists in the world
 */
public abstract class DamageObject extends Sprite {

    private final int damage;

    /**
     * creates a new damage object is used by subclasses using super()
     */
    public DamageObject(Image image,Point position,int damage) {
        super(image,position);
        this.damage = damage;
    }

    /**
     * update the damage objects state
     * is dependent on the type of damage object
     */
    public abstract void update();

    /**
     * used by the world to test if the damageObject has completed its objective and should be removed
     * @return true if it should be removed from the game
     */
    public abstract boolean isComplete();

    /**
     * used by world to determine how much damage to deal to an enemy hit by this damage object
     * @return the amount of damage dealt to an enemy
     */
    public int getDamage() {
        return damage;
    }
}
