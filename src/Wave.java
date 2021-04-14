import java.util.List;

/**
 * the world contains a list of waves
 *
 * a wave is composed of WaveEvents
 * the wave completes each waveEvent in the order they are stored in waveEventList
 */
public class Wave {

    private static final int REWARD_BASE_AMOUNT = 150;
    private static final int REWARD_SCALE_AMOUNT = 100;

    private final List<WaveEvent> waveEventList;
    private final int waveNumber;

    /**
     * creates a new wave
     * @param waveEventList a list of wave events to be processed
     * @param waveNumber the wave number in the world
     */
    public Wave(List<WaveEvent> waveEventList,int waveNumber) {
        this.waveEventList = waveEventList;
        this.waveNumber = waveNumber;
    }

    /**
     * updates the progress of the wave by updating current waveEvents
     * and removing completed waveEvents
     */
    public void updateWave() {
        // if there are no more waveEvents to be processed do nothing
        if (waveEventList.isEmpty()) return;
        // if current waveEvent is complete remove it from the list
        if (waveEventList.get(0).isComplete()) {
            waveEventList.remove(0);
            // if there are no more waveEvents do nothing
            if (waveEventList.isEmpty()) return;
        }
        // update current wave Event
        waveEventList.get(0).update();
    }

    /**
     * used by the world to get the reward for completing a Wave
     * @return the reward amount
     */
    public int getReward() {
        return REWARD_BASE_AMOUNT + REWARD_SCALE_AMOUNT*waveNumber;
    }

    /**
     * checks if the wave is complete
     * @return true if there are no more waveEvents to be processed
     */
    public boolean allEventsProcessed() {
        return waveEventList.isEmpty();
    }

}


