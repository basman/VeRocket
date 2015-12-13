package mission;

import simulation.RaketeAriane5;

/**
 * Created by Roman on 12.12.2015.
 */
public class TakeOff extends MissionElement {
    public TakeOff() {
        super(-1, -1, -1, RaketeAriane5.RaketenSignal.LAUNCHED);
    }
}
