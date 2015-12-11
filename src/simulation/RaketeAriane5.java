package simulation;

import rakete.Rakete;

import java.util.Locale;

/**
 * Created by Roman on 06.12.2015.
 */
public class RaketeAriane5 implements Rakete {

    public static final float maxFuelRate = 100f;
    public static final float maxHeight = 4900f;
    public static final float maxFuel = 500f;

    public enum RaketenSignal {
        OK,
        CRASHED,
        LANDED,
        LAUNCHED,
        NOGRAVITIVY
    }

    private boolean launched = false;
    private boolean crashed = false;

    private double hoehe;             // m
    private float geschwindigkeit;   // m/s
    private float brennstoffVorrat;  // liter
    private float brennrate;         // liter / sek
    private final float leergewicht; // kg

    // Annahme: ein Liter Brennstoff wiegt ein kg

    public RaketeAriane5() {
        leergewicht = 10;
        brennstoffVorrat = maxFuel; // voll getankt

        brennrate = 0;
        geschwindigkeit = 0;
        hoehe = 0;
    }

    public double getHoehe() { return hoehe; }
    public float getBrennrate() { return brennrate; }
    public float getGeschwindigkeit() { return geschwindigkeit; }
    public float getBrennstoffVorrat() { return brennstoffVorrat; }
    public boolean istGestartet() { return launched; }
    public float getTankvolumen() { return maxFuel; }


    void brennrateSetzen(float brennrate) {
        /* ungültige Werte werden zurechtgestutzt */
        if (brennrate < 0)
            this.brennrate = 0;
        else if(brennrate > maxFuelRate)
            this.brennrate = maxFuelRate;
        else
            this.brennrate = brennrate;
    }

    RaketenSignal timeTick() {
        /* stay dead after crash */
        if (this.crashed)
            return RaketenSignal.CRASHED;

        RaketenSignal ret = RaketenSignal.OK;

        float brennMenge = brennrate * Simulator.timeStep;
        if (brennMenge > brennstoffVorrat) {
            brennMenge = brennstoffVorrat;
            brennrate = brennMenge / Simulator.timeStep;
        }
        float gewicht = leergewicht + brennstoffVorrat;

        /* vorrat updaten */
        brennstoffVorrat -= brennMenge;

        /* beschleunigung berechnen */
        float erdKraft = gewicht * Simulator.gravitation; // Newton
        float schubKraft = brennMenge * 1000; // 1000m/s^2 ist angenommene Beschleunigung pro kg verbrannten Brennstoffs
        float gesamtKraft = schubKraft - erdKraft; // Newton; schubKraft nach oben, Erdkraft zieht nach unten
        float resultierendeBeschleunigung = gesamtKraft / gewicht; // m/s^2
        float geschwindigkeitsZunahmeProTick = resultierendeBeschleunigung * Simulator.timeStep;

        /* Zustandsänderungen anwenden */
        this.geschwindigkeit += geschwindigkeitsZunahmeProTick;
        this.hoehe += geschwindigkeit * Simulator.timeStep;

        if (this.hoehe > maxHeight) {
            this.crashed = true; // keine weiteren Berechnungen
            return RaketenSignal.NOGRAVITIVY;
        }

        if (launched && this.hoehe <= 0) {
            this.hoehe = 0;
            if (geschwindigkeit < -0.5) {
                this.crashed = true;
                return RaketenSignal.CRASHED;
            } else {
                return RaketenSignal.LANDED;
            }
        } else if (!launched) {
            if (this.hoehe > 0) {
                this.launched = true;
                ret = RaketenSignal.LAUNCHED;
            } else if(resultierendeBeschleunigung <= 0) {
                System.out.printf(Locale.US, "Warnung: Beschleunigung reicht nicht zum Abheben. (%.2fm/s2)%n",
                        resultierendeBeschleunigung);
            }
        }

        return ret;
    }
}
