/**
 * a abstract wave event class
 * waves are composed of waveEvents
 */
public abstract class WaveEvent {

    /**
     * used by a wave to determine if the event has been completed
     * @return true if completed
     */
    public abstract Boolean isComplete();

    /**
     * updates the waveEvents progress
     */
    public abstract void update();

}

