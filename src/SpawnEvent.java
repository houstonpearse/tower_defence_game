/**
 * a spawn event is an event that spawns a given number of enemies of a single type
 * there is a delay between each enemy spawned
 */
public class SpawnEvent extends WaveEvent {

    private final double spawnDelay;
    private final int numberToSpawn;
    private final String enemyType;
    private int enemiesSpawned;
    private double timeUntilSpawn;

    /**
     * constructor for spawn event
     * initially we want an enemy to spawn instantly
     * @param numberToSpawn the number of enemies to be spawned
     * @param enemyType a string to specify the enemy type
     * @param spawnDelay the delay between each enemy spawned
     */
    public SpawnEvent( int numberToSpawn, String enemyType, double spawnDelay) {
        this.spawnDelay = spawnDelay;
        this.numberToSpawn = numberToSpawn;
        this.enemyType = enemyType;
        timeUntilSpawn = 0.0;
        enemiesSpawned = 0;
    }

    /**
     * checks if it is complete by checking how many enemies has spawned
     * @return true if all enemies have been spawned
     */
    @Override
    public Boolean isComplete() {
        return (enemiesSpawned == numberToSpawn );
    }

    /**
     * updates time passed and spawns new enemies at the start of the path
     */
    @Override
    public void update(){
        //time passing
        timeUntilSpawn-=ShadowDefend.getTimeScale()*ShadowDefend.getmSecondPerFrame();
        // if enough time has passed spawn a new enemy at the start of the path
        if (timeUntilSpawn<=0.0) {
            // release enemy
            World.getInstance().newDefaultEnemy(enemyType);
            // just released a slicer we need to update the delay
            timeUntilSpawn = spawnDelay;
            enemiesSpawned +=1;
        }
    }

}

