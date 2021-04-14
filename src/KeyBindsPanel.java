import bagel.Font;

/**
 * a simple class that will draw the key binds that the player can use
 */
public class KeyBindsPanel extends Panel {

    private static final int INFO_FONT_SIZE = 15;
    private static final String INFO = "Key binds:\n\nS - start wave\nL - increase timescale\nK - decrease timescale";
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";
    private static final int X_POS = 500;
    private static final int Y_POS = 20;
    private static final Font FONT = new Font(FONT_FILE,INFO_FONT_SIZE);

    /**
     * default constructor
     */
    public KeyBindsPanel() {
    }

    /**
     * draws the key binds string at the specified location
     */
    public void draw() {
        FONT.drawString(INFO,X_POS,Y_POS);
    }

}
