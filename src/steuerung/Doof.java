package steuerung;

import rakete.Rakete;
import rakete.RaketenSteuerung;
import simulation.Simulator;
import simulation.ZeitUndRaum;

/**
 * Created by Roman on 06.12.2015.
 */
public class Doof extends AbstractSteuerung {

    public Doof(ZeitUndRaum sim, Rakete rakete) {
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
        else if(getTime() >= 4.5 && getTime() <= 5.3)
            return 87.4f;
        return 0;
    }
}
