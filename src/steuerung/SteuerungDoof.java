package steuerung;

import rakete.Rakete;
import rakete.RaketenSteuerung;
import simulation.Simulator;

/**
 * Created by Roman on 06.12.2015.
 */
public class SteuerungDoof implements RaketenSteuerung {
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
    public float timeTick(Rakete rakete) {
        if (Simulator.timeCount < 20)
            return 70;
        return 0;
    }
}
