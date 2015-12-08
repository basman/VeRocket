package steuerung;

import rakete.Rakete;
import rakete.RaketenSteuerung;
import simulation.Simulator;
import simulation.ZeitUndRaum;

/**
 * Created by Roman on 06.12.2015.
 */
public class SteuerungDoof extends AbstractSteuerung {

    public SteuerungDoof(ZeitUndRaum sim, Rakete rakete) {
        super(sim, rakete);
    }

    @Override
    public void crashed() {

    }

    @Override
    public void gelandet() {

    }

    @Override
    public void lostInSpace() {

    }

    @Override
    public float timeTick() {
        if (getTime() < 2.0)
            return 70;
        return 0;
    }
}
