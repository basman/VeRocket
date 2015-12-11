package simulation;

import rakete.*;
import view.Anzeige;

import java.lang.reflect.Constructor;
import java.util.Locale;

/**
 * Created by Roman on 06.12.2015.
 */
public class Simulator implements Runnable, ZeitUndRaum {
    /* Zeitschrittlänge in Sekunden */
    public static final float timeStep = 0.1f;
    public static final float gravitation = 9.8f;

    private long timeCount = 0;
    private boolean stop = false;
    private RaketeAriane5 rakete;
    private RaketenSteuerung control;
    private Anzeige view;

    /* constructor used by Anzeige */
    public Simulator(String raketenSteuerungKlasse, Anzeige view) {
        rakete = new RaketeAriane5();
        this.view = view;
        try {
            Constructor<?> c = Class.forName(raketenSteuerungKlasse).getDeclaredConstructor(
                    Class.forName("simulation.ZeitUndRaum"), Class.forName("rakete.Rakete"));
            this.control = (RaketenSteuerung) c.newInstance(new Object[] {this, rakete});
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Raketensteuerung nicht gefunden: " + raketenSteuerungKlasse);
            System.exit(8);
        } catch (Exception e) {
            System.err.println("Raketensteuerung konnte nicht geladen werden: " + e.getMessage());
            e.printStackTrace();
            System.exit(9);
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void run() {
        this.stop = false;
        view.setStatus("Ready.");

        rocket_not_running:
        while (timeCount < 1e6 && !stop) {
            /* simuliere Raketenbewegung */
            RaketeAriane5.RaketenSignal ttick = rakete.timeTick();
            switch (ttick) {
                case CRASHED:
                    control.crashed();
                    crashed();
                    break rocket_not_running;
                case NOGRAVITIVY:
                    control.lostInSpace();
                    lostInSpace();
                    break rocket_not_running;
                case LANDED:
                    control.gelandet();
                    gelandet();
                    break rocket_not_running;
                case LAUNCHED:
                    launched();
                    view.setStatus("Launched.");
                default:
                    /* frage Raketensteuerung ab */
                    float brennRate = control.timeTick();
                    if (rakete.istGestartet())
                        logVerbose("running");
                    else if(timeCount % 50 == 0)
                        logVerbose("ready");
                    rakete.brennrateSetzen(brennRate);
            }

            view.redraw();
            try {
                Thread.sleep((long) (1000 * timeStep));
            } catch (InterruptedException e) {
            }

            timeCount++;
        }

        view.redraw();
    }

    private void launched() {
        logVerbose("Rakete gestartet!");
    }

    private void gelandet() {
        log("Rakete gelandet!");
        view.setStatus("Landed.");
    }

    private void crashed() {
        log("Rakete gecrasht!");
        view.setStatus("Crashed!");
    }

    private void lostInSpace() {
        log("Rakete verliess die Erdanziehungskraft!");
        view.setStatus("Out of gravity!");
    }

    private void logVerbose(String msg) {
        System.out.printf(Locale.US, "%2d - %s h: %.3fm v: %.3fm/s fuel: %.3fL f.rate: %.1fL/s%n",
                timeCount, msg, rakete.getHoehe(), rakete.getGeschwindigkeit(), rakete.getBrennstoffVorrat(), rakete.getBrennrate());
    }

    private void log(String msg) {
        System.out.printf(Locale.US, "%2d - %s v: %.2fm/s fuel: %.3fL%n",
                timeCount, msg, rakete.getGeschwindigkeit(), rakete.getBrennstoffVorrat());
    }

    @Override
    public Rakete getRakete() {
        return rakete;
    }

    @Override
    public float getTime() {
        return timeCount * timeStep;
    }
}
