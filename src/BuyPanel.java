import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * a class for the buy panel that is drawn at the top of the screen while the game is being played
 * it shows the current balance of the player as well as showing what types of towers can be purchased
 * aat what price.
 */
public class BuyPanel extends Panel {

    // files
    private static final String PANEL_FILE = "res/images/buypanel.png";
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";

    // images
    private static final Image PANEL_IMAGE = new Image(PANEL_FILE);

    // draw positions
    private static final int PANEL_CENTER_X = 512;
    private static final int PANEL_CENTER_Y = 50;
    private static final int TANK_Y = PANEL_CENTER_Y-10;
    private static final int TANK_X = 64;
    private static final int SUPER_TANK_X = TANK_X + 120;
    private static final int AIR_SUPPORT_X = SUPER_TANK_X + 120;
    private static final int MONEY_POS_X = 824;
    private static final int MONEY_POS_Y = 65;
    private static final int TOWER_TEXT_Y = 90;
    private static final int TANK_TEXT_X = 40;
    private static final int SUPER_TANK_TEXT_X = TANK_TEXT_X+120;
    private static final int AIR_SUPPORT_TEXT_X = SUPER_TANK_TEXT_X+120;

    // tower costs
    private static final int TANK_COST = 250;
    private static final int SUPER_TANK_COST = 600;
    private static final int AIR_SUPPORT_COST = 500;

    // fonts
    private static final int COST_FONT_SIZE = 20;
    private static final int MONEY_FONT_SIZE = 50;
    private static final DrawOptions GREEN = new DrawOptions().setBlendColour(0,255,0);
    private static final DrawOptions RED = new DrawOptions().setBlendColour(255,0,0);
    private static final Font COST_FONT = new Font(FONT_FILE,COST_FONT_SIZE);
    private static final Font MONEY_FONT = new Font(FONT_FILE,MONEY_FONT_SIZE);

    /**
     * an instance of this class is created by the shadow defend game
     */
    public BuyPanel() {
        // exists for the purpose of using the draw method
    }

    /**
     * draws the buy panel to the game
     */
    public void draw() {
        //draw panel
        PANEL_IMAGE.draw(PANEL_CENTER_X,PANEL_CENTER_Y);
        //draw towers
        Tank.getImage().draw(TANK_X,TANK_Y);
        SuperTank.getImage().draw(SUPER_TANK_X,TANK_Y);
        AirSupport.getImage().draw(AIR_SUPPORT_X,TANK_Y);
        //draw money
        MONEY_FONT.drawString("$" + ShadowDefend.getMoney(),MONEY_POS_X,MONEY_POS_Y);

        // draw tower costs
        if (ShadowDefend.getMoney() >= TANK_COST) {
            COST_FONT.drawString("$" + TANK_COST,TANK_TEXT_X, TOWER_TEXT_Y, GREEN);
        } else {
            COST_FONT.drawString("$" + TANK_COST,TANK_TEXT_X, TOWER_TEXT_Y, RED);
        }

        if (ShadowDefend.getMoney() >= SUPER_TANK_COST) {
            COST_FONT.drawString("$" + SUPER_TANK_COST,SUPER_TANK_TEXT_X, TOWER_TEXT_Y, GREEN);
        } else {
            COST_FONT.drawString("$" + SUPER_TANK_COST,SUPER_TANK_TEXT_X, TOWER_TEXT_Y, RED);
        }

        if (ShadowDefend.getMoney() >= AIR_SUPPORT_COST) {
            COST_FONT.drawString("$" + AIR_SUPPORT_COST,AIR_SUPPORT_TEXT_X, TOWER_TEXT_Y, GREEN);
        } else {
            COST_FONT.drawString("$" + AIR_SUPPORT_COST,AIR_SUPPORT_TEXT_X, TOWER_TEXT_Y, RED);
        }
    }

    /**
     * used by the world to avoid placing a tower that would overlap with the buy panel
     * @return a rectangle that the buy panel is drawn over
     */
    public static Rectangle getPanelBox() {
        return PANEL_IMAGE.getBoundingBoxAt(new Point(PANEL_CENTER_X,PANEL_CENTER_Y));
    }

    /**
     * used by the shadowDefend game to test if the player clicked the tank image on the panel
     * @return the rectangle that corresponds to the tank buyPanel image
     */
    public static Rectangle getTankBuyBox() {
        return Tank.getImage().getBoundingBoxAt(new Point(TANK_X,TANK_Y));
    }

    /**
     * used by the shadowDefend game to test if the player clicked the supertank image on the panel
     * @return the rectangle that corresponds to the super tank buyPanel image
     */
    public static Rectangle getSuperTankBuyBox() {
        return SuperTank.getImage().getBoundingBoxAt(new Point(SUPER_TANK_X,TANK_Y));
    }

    /**
     * used by the shadowDefend game to test if the player clicked the air support image on the panel
     * @return the rectangle that corresponds to the air support buyPanel image
     */
    public static Rectangle getAirSupportBuyBox() {
        return AirSupport.getImage().getBoundingBoxAt(new Point(AIR_SUPPORT_X,TANK_Y));
    }

}

