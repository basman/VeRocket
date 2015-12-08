package simulation;

import rakete.*;
import steuerung.*;

/**
 * Created by Roman on 06.12.2015.
 */
public class Simulator implements Runnable {
    /* Zeitschrittlänge in Sekunden */
    public static final float timeStep = 0.1f;
    public static final float gravitation = 9.8f;
    public static long timeCount = 0;

    private RaketeAriane5 rakete;
    private RaketenSteuerung control;

    public Simulator() {
        rakete = new RaketeAriane5();
        control = new SteuerungDoof();
    }

    public Simulator(String raketenSteuerungKlasse) {
        rakete = new RaketeAriane5();
        try {
            control = (RaketenSteuerung) Class.forName(raketenSteuerungKlasse).newInstance();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Raketensteuerung nicht gefunden: " + raketenSteuerungKlasse);
            System.exit(8);
        } catch (Exception e) {
            System.err.println("Raketensteuerung konnte nicht geladen werden: " + e.getMessage());
            System.exit(9);
        }
    }

    /* Programm Startpunkt */
    public static void main(String args[]) {
        Simulator sim;
        if (args.length > 0)
            sim = new Simulator(args[0]);
        else
            sim = new Simulator();

        Thread me = new Thread(sim);
        me.start();
    }

    public void run() {
        while (true) {
            /* simuliere Raketenbewegung */
            RaketeAriane5.RaketenSignal ttick = rakete.timeTick();
            switch (ttick) {
                case CRASHED:
                    control.crashed();
                    crashed();
                    break;
                case NOGRAVITIVY:
                    control.lostInSpace();
                    lostInSpace();
                    break;
                case LANDED:
                    control.gelandet();
                    gelandet();
                    break;
                case LAUNCHED:
                    launched();
                default:
                    /* frage Raketensteuerung ab */
                    float brennRate = control.timeTick(rakete);
                    if (rakete.istGestartet())
                        logVerbose("running");
                    else if(timeCount % 50 == 0)
                        logVerbose("ready");
                    rakete.brennrateSetzen(brennRate);
            }

            try {
                Thread.sleep((long) (1000 * timeStep));
            } catch (InterruptedException e) {
            }

            timeCount++;
        }
    }

    private void launched() {
        logVerbose("Rakete gestartet!");
    }

    private void gelandet() {
        log("Rakete gelandet!");
        System.exit(0);
    }

    private void crashed() {
        log("Rakete gecrasht!");
        System.exit(1);
    }

    private void lostInSpace() {
        log("Rakete verliess die Erdanziehungskraft!");
        System.exit(2);
    }

    private void logVerbose(String msg) {
        System.out.println(timeCount + " - " + msg + " h: " + rakete.getHoehe() + "m v: " + rakete.getGeschwindigkeit() +
                "m/s fuel: " + rakete.getBrennstoffVorrat() + " l  f.rate: " + rakete.getBrennrate());
    }

    private void log(String msg) {
        System.out.println(timeCount + " - " + msg + " v: " + rakete.getGeschwindigkeit() +
                "m/s fuel: " + rakete.getBrennstoffVorrat());
    }
}
