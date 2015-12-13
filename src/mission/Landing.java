package mission;

import simulation.RaketeAriane5;

/**
 * Created by Roman on 12.12.2015.
 */
public class Landing extends MissionElement {
    public Landing() {
        super(-1, -1, -1, RaketeAriane5.RaketenSignal.LANDED);
    }
}
