import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.util.Vector2;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** singleton implementation of world
 * utilises eager initialisation so a single instance of
 * this class will be loaded at runtime
 */
public class World {

    // singleton
    private static final World instance = new World();

    // level info
    private static final String MAP_1_FILE = "res/levels/1.tmx";
    private static final String MAP_2_FILE = "res/levels/2.tmx";
    private static final String WAVES_FILE = "res/levels/waves.txt";
    private static final int MAP_WIDTH = 1024;
    private static final int MAP_HEIGHT = 768;
    private static final int PLANE_SPAWN_OFFSET = -50;

    // critical level variables
    private TiledMap map;
    private List<Point> path;
    private List<Sprite> spriteList;
    private final List<Sprite> spriteQueue;
    private List<Wave> waveList;
    private int levelNumber;

    // helper variables
    private int waveNumber;
    private boolean waveInProgress;
    private Vector2 nextAirSupportDirection;

    /** private constructor of singleton world class
     * loads first map from 1.tmx
     * loads first wave file from waves.txt
     *
     */
    private World(){
        // load first level
        loadLevel(MAP_1_FILE,WAVES_FILE);
        levelNumber = 1;
        // our list of game objects that will be used
        spriteList = new ArrayList<>();
        spriteQueue = new ArrayList<>();
        // the first plane object created will fly to the right
        nextAirSupportDirection = Vector2.right;
    }

    /** provides global access to the singleton world instance
     *
     * @return the singleton instance that was instantiated at run-time
     */
    public static World getInstance() {
        return instance;
    }

    /** draws map and each sprite game object by calling the draw() method
     * on each of them
     */
    public void draw() {
        map.draw(0, 0, 0, 0, MAP_WIDTH, MAP_HEIGHT);
        for (Sprite sprite:spriteList) {
            sprite.draw();
        }
    }

    /** updates the state of the world by individually updating each sprite game object
     * will remove unwanted sprites that are no longer required
     */
    public void update() {
        //update all sprites
        for (Sprite sprite:spriteList) {
            sprite.update();
        }
        // add all pending sprites that were added to the queue and clear the queue
        spriteList.addAll(spriteQueue);
        spriteQueue.clear();
        // remove unwanted game objects
        removeProjectiles();
        removeExplosives();
        removeEnemies();

        //check if we have finished all waves
        if (waveList.isEmpty()) {
            // if there are no more enemies we should attempt to load the next level
            if (getNumEnemies()==0) {
                loadNextLevel();
            }
            return;
        }

        // check if wave is complete
        if (getNumEnemies()==0 && waveList.get(0).allEventsProcessed()) {
            // pause game
            waveInProgress = false;
            // give reward for completing wave
            ShadowDefend.increaseMoney(waveList.get(0).getReward());
            // remove completed wave
            waveList.remove(0);
            waveNumber++;
        }

        // if the wave is still in progress update the wave
        if (waveInProgress) {
            waveList.get(0).updateWave();
        }
    }

    /** method used by user to start a new wave
     * will be ineffective if there are no waves left to start
     */
    public void startWave() {
        if (!waveList.isEmpty()) waveInProgress = true;
    }

    /** method used to by user to gain level completion information
     *
     * @return boolean value representing if the level is complete or not
     */
    public boolean isLevelComplete() {
        return waveList.isEmpty();
    }

    /** method used by user to gain wave status information
     *
     * @return boolean representing
     */
    public boolean isWaveInProgress() {
        return waveInProgress;
    }

    // removes unwanted enemies
    private void removeEnemies() {
        List <Sprite> newSpriteList = new ArrayList<>();
        for (Sprite sprite:spriteList) {
            if (sprite instanceof Enemy) {
                Enemy enemy = (Enemy) sprite;
                if (enemy.isComplete()) {
                    //we want to discard enemies at the end of the path and penalise the player
                    ShadowDefend.decreaseLives(enemy.getPenalty());
                } else if (enemy.isDead()) {
                    //remove dead enemies and reward the player
                    ShadowDefend.increaseMoney(enemy.getReward());
                    // spawn children of the dead enemy
                    enemy.spawnChildren();
                } else {
                    // we want to keep these enemies as they are still traversing the path
                    newSpriteList.add(sprite);
                }
            } else {
                // add all other non-enemy sprites
                newSpriteList.add(sprite);
            }
        }
        // set sprite list to the new list with all completed enemies removed
        spriteList=newSpriteList;
    }

    // removes unwanted projectiles
    private void removeProjectiles() {
        List <Sprite> newSpriteList = new ArrayList<>();
        for (Sprite sprite:spriteList) {
            if (sprite instanceof Projectile) {
                Projectile projectile = (Projectile) sprite;
                if (projectile.isComplete()) {
                    // discard. projectile has served its purpose. decrease its targets health
                    projectile.getTargetEnemy().decreaseHealth(projectile.getDamage());
                } else {
                    // projectile is still moving we want to keep it
                    newSpriteList.add(sprite);
                }
            } else {
                // add all other non-projectile sprites
                newSpriteList.add(sprite);
            }
        }
        spriteList=newSpriteList;
    }

    // removes explosives
    private void removeExplosives() {
        List <Sprite> newSpriteList = new ArrayList<>();
        for (Sprite sprite:spriteList) {
            // check if explosive
            if (sprite instanceof Explosive) {
                Explosive explosive = (Explosive) sprite;
                if (explosive.isComplete()) {
                    damageEnemiesWithinR(explosive.getPosition(),Explosive.getRadius(),explosive.getDamage());
                } else {
                    newSpriteList.add(sprite);
                }
            } else {
                newSpriteList.add(sprite);
            }
        }
        spriteList=newSpriteList;
    }

    // loads next available level and resets shadow defend by calling the reset method
    private void loadNextLevel() {
        // if the current level is 1 then go to the second level
        if (levelNumber==1) {
            // load level 2
            waveList.clear();
            spriteList.clear();
            spriteQueue.clear();
            loadLevel(MAP_2_FILE,WAVES_FILE);
            levelNumber++;
            ShadowDefend.reset();
        }
        // no other levels to go to :(((
    }

    // loads a level from a given map and waves file
    private void loadLevel(String mapFile,String wavesFile) {
        // load map
        map = new TiledMap(mapFile);
        // retrieve path which is encoded inside map
        path = map.getAllPolylines().get(0);
        // initially the first wave is not in progress until it is manually started
        waveInProgress = false;
        // we are currently waiting for the first wave to commence
        waveNumber = 1;
        //load waves to waveList from WAVES_FILE
        waveList = loadWaves(wavesFile);
    }

    // deals damage to all enemies within a certain distance from a point
    private void damageEnemiesWithinR(Point point,double radius,int damage) {
        for (Sprite sprite: spriteList) {
            if (sprite instanceof Enemy) {
                Enemy enemy = (Enemy) sprite;
                if (point.distanceTo(enemy.getPosition())<=radius) {
                    enemy.decreaseHealth(damage);
                }
            }
        }
    }

    /**
     * checks if the point given is available for a tower to be placed at
     * @param point the point on the map
     * @return true if a tower can be placed at that poin
     */
    public boolean canPlaceTower(Point point) {
        // we cant place a tower outside the map
        if (point.x<0 || point.x>MAP_WIDTH || point.y<0 || point.y>MAP_HEIGHT) {
            return false;
        }
        // we cant place a tower on the map if it is blocked
        if (map.getPropertyBoolean((int)point.x,(int)point.y,"blocked",false)) {
            return false;
        }
        // we cant place a tower on a point that intersects with another sprites bounding box
        for (Sprite sprite :spriteList) {
            //check if this sprites bounding box intersects with the point
            if (sprite.getBox().intersects(point)) {
                return false;
            }
        }
        return true;
    }

    /**
     * creates a new tank and adds it to the sprite queue
     * @param point the position of the new tank
     */
    public void newTankAt(Point point) {
        spriteQueue.add(new Tank(point));
    }

    /**
     * creates a new superTank and adds it to the sprite queue
     * @param point the position of the new tank
     */
    public void newSuperTankAt(Point point) {
        spriteQueue.add(new SuperTank(point));
    }

    /**
     * creates a new plane flying in the next available direction
     * @param point the point on the map that was clicked
     */
    public void newPlaneAt(Point point) {
        // when creating planes we want them to alternate flying directions
        // between going right and going down
        if (nextAirSupportDirection == Vector2.right) {
            // new plane starting at the left edge of the map
            spriteQueue.add(new AirSupport(new Point(PLANE_SPAWN_OFFSET, point.y),Vector2.right));
            nextAirSupportDirection = Vector2.down;
        } else {
            // new plane starting at the top of the map
            spriteQueue.add(new AirSupport(new Point(point.x, PLANE_SPAWN_OFFSET),Vector2.down));
            nextAirSupportDirection = Vector2.right;
        }
    }

    /**
     * when new game objects are to be added to the sprite list they must first be placed in the queue
     * and will be added to the sprite list on the next available update call
     * this avoids a modifying the spriteList while its being iterated through
     * @param sprite the new sprite to be added to the sprite queue
     */
    public void addSprite(Sprite sprite) {
        spriteQueue.add(sprite);
    }

    /**
     * default enemies are spawned by the wave
     * @param enemyType a string representing the enemy type
     */
    public void newDefaultEnemy(String enemyType) {
        // default enemies start at the first point in the path and move towards the second
        Point defaultPosition = path.get(0);
        Point defaultTarget = path.get(1);
        int defaultTargetIndex = 1;

        // check what enemy should be spawned
        switch (enemyType) {
            case "slicer":
                addSprite(new RegularSlicer(defaultPosition, defaultTarget, defaultTargetIndex));
                break;
            case "superslicer":
                addSprite(new SuperSlicer(defaultPosition, defaultTarget, defaultTargetIndex));
                break;
            case "megaslicer":
                addSprite(new MegaSlicer(defaultPosition, defaultTarget, defaultTargetIndex));
                break;
            case "apexslicer":
                addSprite(new ApexSlicer(defaultPosition, defaultTarget, defaultTargetIndex));
                break;
            default:
                System.out.format("FATAL ERROR: %s is not a valid enemytype\n", enemyType);
                break;
        }
    }

    /**
     * finds the closest enemy to a point within a certain distance
     * used by towers to find targets
     * @param position the location of a tower
     * @param radius the range of a tower
     * @return the new target or null if there is none available
     */
    public Enemy findClosestEnemy(Point position,double radius) {
        Enemy closestEnemy = null;
        for (Sprite sprite:spriteList) {
            if (sprite instanceof Enemy) {
                Enemy enemy = (Enemy) sprite;
                if (closestEnemy==null && enemy.getPosition().distanceTo(position)<=radius) {
                    // first enemy found
                    closestEnemy = (Enemy) sprite;
                } else if (closestEnemy!=null && closestEnemy.getPosition().distanceTo(position)
                        > enemy.getPosition().distanceTo(position)) {
                    // we have found another closer enemy
                    closestEnemy = enemy;
                }
            }
        }
        return closestEnemy;
    }

    // gets the number of active enemies to determine if the wave has been completed
    private int getNumEnemies() {
        int count=0;
        for (Sprite sprite:spriteList) {
            if (sprite instanceof Enemy) count++;
        }
        return count;
    }

    /**
     * used by enemies travelling along this path
     * @return the current path of the level
     */
    public List<Point> getPath() {
        return path;
    }

    /**
     * the wave number is needed in order to draw the status panel
     * @return the current wave number
     */
    public int getWaveNumber() {
        return waveNumber;
    }

    // method to load a list of waves from a waves file
    private List<Wave> loadWaves(String wavesFile) {
        // wave list that will be returned
        List<Wave> newWaveList = new ArrayList<>();

        //open the wavesFile
        try (Scanner scanner = new Scanner(new FileReader(wavesFile))) {
            //creating a list of wave events to add to new wave
            int waveCount=1;
            List<WaveEvent> waveEventList = new ArrayList<>();
            //get all wave events from file
            while (scanner.hasNextLine()) {
                //process next waveEvent string
                String[] words = scanner.nextLine().split(",");
                // test if we have found the start of a new wave
                if (Integer.parseInt(words[0]) != waveCount) {
                    //create new wave with current list of wave Events
                    newWaveList.add(new Wave(waveEventList,waveCount));
                    // create list for storing new waveEvents for next wave
                    waveEventList = new ArrayList<>();
                    waveCount+=1;
                }
                // add new wave event either a delay or spawn event
                if (words[1].equals("spawn")) {
                    waveEventList.add(new SpawnEvent(Integer.parseInt(words[2]),words[3],Double.parseDouble(words[4])));
                } else if (words[1].equals("delay")) {
                    waveEventList.add(new DelayEvent(Double.parseDouble(words[2])));
                } else {
                    System.out.format("ERROR: %s is not a valid wave event type\n",words[1]);
                }

            }

            //create last wave from current list of wave events
            newWaveList.add(new Wave(waveEventList,waveCount));


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("could not open file");
        }
        return newWaveList;
    }
}
