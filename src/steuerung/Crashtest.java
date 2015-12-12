package steuerung;

import rakete.Rakete;
import simulation.ZeitUndRaum;

/**
 * Created by Roman on 06.12.2015.
 */
public class Crashtest extends AbstractSteuerung {

    public Crashtest(ZeitUndRaum sim, Rakete rakete) {
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
