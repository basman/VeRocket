package steuerung;

import rakete.Rakete;
import rakete.RaketenSteuerung;
import simulation.ZeitUndRaum;
import view.Anzeige;

/**
 * Created by Roman on 08.12.2015.
 */
public abstract class AbstractControl implements RaketenSteuerung {
    private ZeitUndRaum sim;
    protected Rakete rakete;

    public AbstractControl(ZeitUndRaum sim, Rakete rakete) {
        this.sim = sim;
        this.rakete = rakete;
    }

    protected float getTime() {
        return sim.getTime();
    }
}
