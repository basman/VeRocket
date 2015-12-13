package mission;

import org.json.simple.JSONObject;
import simulation.RaketeAriane5;
import util.Util;

/**
 * A single mission element, consisting of a height to reach and a time span for how long to hover at that height.
 * Created by Roman on 12.12.2015.
 */
public class MissionElement {
    private final float targetAltitude; /* target altitude [m] */
    private final float minTime; /* how long to hover at targetAltitude within tolerance [s] */
    private final float tolerance; /* maximum allowed deviation from targetAltitude in both directions [m] */
    private final RaketeAriane5.RaketenSignal requiredRocketStatus; /* required rocket status (e.g. LANDED) */
    private boolean completed = false;

    public MissionElement(float targetAltitude, float minTime, float tolerance, RaketeAriane5.RaketenSignal requiredRocketStatus) {
        this.targetAltitude = targetAltitude;
        this.minTime = minTime;
        this.tolerance = tolerance;
        this.requiredRocketStatus = requiredRocketStatus;

        if (minTime > 0 && targetAltitude < 0)
            throw new RuntimeException("Warning: setting a minimum time without target altitude makes no sense.");
    }

    public MissionElement(JSONObject json) {
        this.targetAltitude = Util.getJSONfloat("altitude", json);
        this.tolerance = Util.getJSONfloat("tolerance", json);
        this.minTime = Util.getJSONfloat("time", json);
        this.requiredRocketStatus = RaketeAriane5.RaketenSignal.values()[Util.getJSONint("requiredRocketStatus", json, 0)];
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public float getTargetAltitude() {
        return targetAltitude;
    }

    public float getMinTime() {
        return minTime;
    }

    public float getTolerance() {
        return tolerance;
    }

    public RaketeAriane5.RaketenSignal getRequiredRocketStatus() {
        return requiredRocketStatus;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        if (targetAltitude >= 0) {
            String what = "reach";
            if(minTime > 0)
                what = "hover at";
            s.append(String.format("%s %.1f±%.1fm", what, targetAltitude, tolerance));

            if (minTime > 0)
                s.append(String.format(" for %.1fs", minTime));
        }

        if (requiredRocketStatus != RaketeAriane5.RaketenSignal.UNDEFINED) {
            if (s.length() > 0)
                s.append(" and ");

            if (requiredRocketStatus == RaketeAriane5.RaketenSignal.LAUNCHED) {
                s.append("take off");
            } else if (requiredRocketStatus == RaketeAriane5.RaketenSignal.LANDED) {
                s.append("land");
            } else {
                throw new RuntimeException("unknown required rocket status");
            }
        }

        return s.toString();
    }
}
