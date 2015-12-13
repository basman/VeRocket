package steuerung;

import mission.MissionElement;
import rakete.Rakete;
import simulation.ZeitUndRaum;

/**
 * Created by Roman on 06.12.2015.
 */
public class Icarus extends AbstractControl {

    public Icarus(ZeitUndRaum sim, Rakete rakete) {
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
    public void nextMissionElement(MissionElement miEle) {

    }

    @Override
    public float timeTick() {
        return 100;
    }
}
