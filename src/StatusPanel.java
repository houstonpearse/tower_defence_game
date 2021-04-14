import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * a class to draw the game status panel showing the number of lives, waveNumber,timeScale, and game status
 */
public class StatusPanel extends Panel {

    // panel
    private static final String PANEL_FILE = "res/images/statuspanel.png";
    private static final Image PANEL_IMAGE = new Image(PANEL_FILE);
    private static final int CENTER_X = 512;
    private static final int CENTER_Y = 756;

    // font constants
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";
    private static final int TEXT_SIZE = 18;
    private static final Font FONT = new Font(FONT_FILE,TEXT_SIZE);
    private static final Font LOSE_FONT = new Font(FONT_FILE,TEXT_SIZE*5);
    private static final DrawOptions GREEN = new DrawOptions().setBlendColour(0,255,0);
    private static final DrawOptions RED = new DrawOptions().setBlendColour(255,0,0);


    // text constants
    private static final int TEXT_Y = 760;
    private static final int WAVE_X = 10;
    private static final int TIME_SCALE_X = 200;
    private static final int STATUS_X = 500;
    private static final int LIVES_X = 900;

    /**
     * creates a game panel instance
     */
    public StatusPanel() {
    }

    /**
     * draws the status panel
     * gets information from the shadowDefend game and the world to display for the player
     */
    public void draw() {
        // draw panel background
        PANEL_IMAGE.draw(CENTER_X,CENTER_Y);

        // draw extra losing status for fun
        if (ShadowDefend.getStatus().equals("Loser. You Suck!")) {
            LOSE_FONT.drawString("WASTED",300,400, RED);
        }

        //draw wave string
        FONT.drawString("Wave: "+World.getInstance().getWaveNumber(),WAVE_X,TEXT_Y);

        //draw timeScale string making the text green if the timeScale > 1
        if (ShadowDefend.getTimeScale()==1) {
            FONT.drawString("Time Scale: 1.0",TIME_SCALE_X,TEXT_Y);
        } else {
            FONT.drawString(String.format("Time Scale: %d.0",ShadowDefend.getTimeScale()),TIME_SCALE_X,TEXT_Y, GREEN);
        }

        //draw status string
        FONT.drawString("Status: "+ ShadowDefend.getStatus(),STATUS_X,TEXT_Y);

        //draw lives
        FONT.drawString("Lives: "+ ShadowDefend.getLives(),LIVES_X,TEXT_Y);

    }

    /**
     * prevents the player from placing a tower that overlaps with the status panel
     * @return a rectangle of the region that the status panel is drawn in
     */
    public static Rectangle getPanelBox() {
        return PANEL_IMAGE.getBoundingBoxAt(new Point(CENTER_X,CENTER_Y));
    }
}
