package steuerung;

import mission.MissionElement;
import rakete.Rakete;
import simulation.ZeitUndRaum;

/**
 * Created by Roman on 06.12.2015.
 */
public class Langlauf extends AbstractControl {

    public Langlauf(ZeitUndRaum sim, Rakete rakete) {
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
        if(rakete.getHoehe() < 10)
            return 80 * Math.max(rakete.getBrennstoffVorrat() / rakete.getTankvolumen(), rakete.getLeergewicht());
        else
            return 0;
    }
}
