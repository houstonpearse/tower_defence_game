import bagel.*;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * the overarching game class that runs the game
 * and facilitates user interaction
 */
public class ShadowDefend extends AbstractGame {

    private static final int INITIAL_TIMESCALE = 1;
    private static final int MAX_TIMESCALE = 5;
    private static final int FPS = 60;
    private static final double M_SEC_PER_FRAME = 1000.0/FPS;
    private static final int INITIAL_MONEY = 500;
    private static final int INITIAL_LIVES = 25;
    private static final int TANK_COST = 250;
    private static final int SUPER_TANK_COST = 600;
    private static final int PLANE_COST= 500;

    private static int timeScale = INITIAL_TIMESCALE;
    private static boolean isPlacingTank;
    private static boolean isPlacingSuperTank;
    private static boolean isPlacingAirSupport;
    private static int money;
    private static int lives;
    private final List<Panel> panelList;

    /**
     *  creates and runs a new instance of the shadowDefend game
     */
    public static void main(String[] args) {
        // Create new instance of game and run it
        new ShadowDefend().run();
    }

    /** initial state of the game*/
    public ShadowDefend(){
        panelList = new ArrayList<>();
        panelList.add(new BuyPanel());
        panelList.add(new KeyBindsPanel());
        panelList.add(new StatusPanel());
        isPlacingTank = false;
        isPlacingSuperTank = false;
        isPlacingAirSupport = false;
        money = INITIAL_MONEY;
        lives = INITIAL_LIVES;
    }

    @Override
    protected void update(Input input) {
        // draw world
        World.getInstance().draw();
        // update world if there is enough lives
        if (lives>0) World.getInstance().update();
        //draw panels
        for (Panel panel: panelList) {
            panel.draw();
        }
        // render towers on cursor if one is being placed
        if (isPlacingTank) {
            Tank.getImage().draw(input.getMouseX(),input.getMouseY());
        }
        if (isPlacingSuperTank) {
            SuperTank.getImage().draw(input.getMouseX(),input.getMouseY());
        }
        if (isPlacingAirSupport) {
            AirSupport.getImage().draw(input.getMouseX(),input.getMouseY());
        }

        // user inputs
        if (input.wasPressed(MouseButtons.LEFT)) {
            clickPoint(input.getMousePosition());
        }
        if (input.wasPressed(MouseButtons.RIGHT)) {
            // reset placing
            isPlacingTank = isPlacingSuperTank = isPlacingAirSupport = false;
        }
        if (input.wasPressed(Keys.S)) {
            World.getInstance().startWave();
        }
        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }
        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

    }

    // processes a left click
    private void clickPoint(Point point) {
        //if we clicked in the buy box of a tower then start to place that tower
        if (getMoney()>=TANK_COST && BuyPanel.getTankBuyBox().intersects(point)) {
            isPlacingTank = true;
            isPlacingAirSupport = false;
            isPlacingSuperTank = false;
            return;
        }
        if (getMoney()>=SUPER_TANK_COST && BuyPanel.getSuperTankBuyBox().intersects(point)) {
            isPlacingTank = false;
            isPlacingSuperTank = true;
            isPlacingAirSupport = false;
            return;
        }
        if (getMoney()>=PLANE_COST && BuyPanel.getAirSupportBuyBox().intersects(point)) {
            isPlacingTank = false;
            isPlacingSuperTank = false;
            isPlacingAirSupport = true;
            return;
        }

        if (isPlacingTank || isPlacingSuperTank || isPlacingAirSupport) {
            // we can't place a tower on a panel
            if (!BuyPanel.getPanelBox().intersects(point) && !StatusPanel.getPanelBox().intersects(point)) {
                // check if we can place a tower on the map at that current position
                if (World.getInstance().canPlaceTower(point)) {
                    // place the corresponding tower
                    if (isPlacingTank) {
                        World.getInstance().newTankAt(point);
                        decreaseMoney(TANK_COST);
                        isPlacingTank = isPlacingAirSupport = isPlacingSuperTank = false;
                    } else if (isPlacingSuperTank) {
                        World.getInstance().newSuperTankAt(point);
                        decreaseMoney(SUPER_TANK_COST);
                        isPlacingTank = isPlacingAirSupport = isPlacingSuperTank = false;
                    } else if (isPlacingAirSupport) {
                        World.getInstance().newPlaneAt(point);
                        decreaseMoney(PLANE_COST);
                        isPlacingTank = isPlacingAirSupport = isPlacingSuperTank = false;
                    }
                }
            }
        }
    }


    private void increaseTimescale() {
        if (timeScale < MAX_TIMESCALE) {
            timeScale++;
        }
    }

    private void decreaseTimescale() {
        if (timeScale > INITIAL_TIMESCALE) {
            timeScale--;
        }
    }

    /** gets the current status of the game
     * draws information from world
     * @return a string containing the status
     */
    public static String getStatus() {
        if (lives <=0 ) return "Loser. You Suck!";
        else if (World.getInstance().isLevelComplete()) return "Winner!";
        else if (isPlacingAirSupport || isPlacingTank|| isPlacingSuperTank) return "Placing";
        else if (World.getInstance().isWaveInProgress()) return "Wave in progress";
        else return "Awaiting start";
    }

    //getters and setters
    public static int getTimeScale() {
        return timeScale;
    }

    public static double getmSecondPerFrame() {
        return M_SEC_PER_FRAME;
    }

    public static int getMoney() {
        return money;
    }

    public static void increaseMoney(int amount) {
        money += amount;
    }

    public static void decreaseMoney(int amount) {
        money -= amount;
    }

    public static void decreaseLives(int amount) {
        lives-=amount;
    }

    public static int getLives() {
        return lives;
    }

    /**
     * resets all game state variable back to default values
     * used when loading a new level
     */
    public static void reset() {
        money = INITIAL_MONEY;
        lives = INITIAL_LIVES;
        isPlacingTank = false;
        isPlacingSuperTank = false;
        isPlacingAirSupport = false;
        timeScale = INITIAL_TIMESCALE;
    }
}
