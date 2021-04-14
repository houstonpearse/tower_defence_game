/**
 * a type of wave event that just lets time pass and delays releasing more enemies
 */
public class DelayEvent extends WaveEvent {

    private final double delay;
    private double timeElapsed;

    /**
     * creates a new delay event with a specified delay
     * @param delay the amount of time that is allowed to pass
     */
    public DelayEvent(double delay) {
        this.delay = delay;
    }

    /**
     * checks if the delay event is complete
     * @return true if enough time has passed
     */
    @Override
    public Boolean isComplete() {
        return timeElapsed>=delay;
    }

    /**
     * increases the time passed according to the universal timeScale multiplier
     */
    @Override
    public void update() {
        timeElapsed += ShadowDefend.getTimeScale()*ShadowDefend.getmSecondPerFrame();
    }
}
