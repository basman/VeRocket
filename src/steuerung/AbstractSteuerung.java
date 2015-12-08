package steuerung;

import rakete.Rakete;
import rakete.RaketenSteuerung;
import simulation.ZeitUndRaum;

/**
 * Created by Roman on 08.12.2015.
 */
public abstract class AbstractSteuerung implements RaketenSteuerung {
    private ZeitUndRaum sim;
    protected Rakete rakete;

    public AbstractSteuerung(ZeitUndRaum sim, Rakete rakete) {
        this.sim = sim;
        this.rakete = rakete;
    }

    protected float getTime() {
        return sim.getTime();
    }

    @Override
    public abstract void crashed();

    @Override
    public abstract void gelandet();

    @Override
    public abstract void lostInSpace();

    @Override
    public abstract float timeTick();
}
