package mission;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simulation.RaketeAriane5;
import simulation.Simulator;

import java.util.ArrayList;

/**
 * Describe a mission consisting of height and time limits.
 *
 * Created by Roman on 12.12.2015.
 */
public class Mission {

    private ArrayList<MissionElement> elements;
    private int missionElementIndex;
    private Simulator simulation;
    private String name;

    private boolean completed = false;
    private float timeReachedTargetAltitude;

    public Mission(String name) {
        this.name = name;
        this.elements = new ArrayList<>();
        init();
    }

    public Mission(String name, JSONArray missionElements) {
        this.name = name;
        this.elements = new ArrayList<>();

        for(Object melem: missionElements) {
            JSONObject me = (JSONObject)melem;
            MissionElement mele = new MissionElement(me);
            elements.add(mele);
        }

        init();
    }

    public void init() {
        this.missionElementIndex = 0;
        this.timeReachedTargetAltitude = -1;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setSimulation(Simulator simulation) {
        this.simulation = simulation;
    }

    public ArrayList<MissionElement> getElements() {
        return elements;
    }

    public void addElement(MissionElement me) {
        elements.add(me);
    }

    /* reset state of all mission elements */
    public void reset() {
        init();
        for(MissionElement me: elements) {
            me.reset();
        }
    }

   /* called by the simulator to inform us about current rocket status
     *
      * @return true if current element is completed or all elements are completed */
    public boolean timeTick(RaketeAriane5.RaketenSignal rocketSignal) {
        if (completed)
            return true;

        boolean ret = false;

        if (elements.size() == 0)
            throw new RuntimeException("Mission has no elements.");

        MissionElement me = elements.get(missionElementIndex);

        boolean onTargetAltitude = me.getTargetAltitude() < 0 ||
                /* within target altitude range */
                simulation.getRakete().getHoehe() >= me.getTargetAltitude() - me.getTolerance() &&
                        simulation.getRakete().getHoehe() <= me.getTargetAltitude() + me.getTolerance() ||
                /* ascended over target altitude range */
                simulation.getRakete().getLastHoehe() < me.getTargetAltitude() - me.getTolerance() &&
                        simulation.getRakete().getHoehe() > me.getTargetAltitude() + me.getTolerance() ||
                /* descended over target altitude range */
                simulation.getRakete().getHoehe() < me.getTargetAltitude() - me.getTolerance() &&
                        simulation.getRakete().getLastHoehe() > me.getTargetAltitude() + me.getTolerance();

        if (onTargetAltitude) {
            if (timeReachedTargetAltitude < 0)
                timeReachedTargetAltitude = simulation.getTime();
        } else {
           timeReachedTargetAltitude = -1;
        }

        if ((rocketSignal == me.getRequiredRocketStatus() || me.getRequiredRocketStatus() == RaketeAriane5.RaketenSignal.UNDEFINED) &&
                onTargetAltitude && simulation.getTime()-timeReachedTargetAltitude >= me.getMinTime()) {
            // mission element completed
            ret = true;
            me.setCompleted();
            timeReachedTargetAltitude = -1;
            missionElementIndex++;

            if(missionElementIndex >= elements.size()) // last element completed?
                this.completed = true;
        }

        return ret;
    }
}
