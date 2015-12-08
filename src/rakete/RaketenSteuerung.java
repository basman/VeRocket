package rakete;

import rakete.Rakete;

/**
 * Created by Roman on 06.12.2015.
 */
public interface RaketenSteuerung {
    /* Wird zum Abschluss eines Zeitabschnitts aufgerufen, um den neuen Status der Rakete mitzuteilen.
     * return: neue Brennrate */
    float timeTick(Rakete rakete);

    /* Rakete ist abgestürtzt */
    void crashed();

    /* Rakete ist gelandet */
    void gelandet();

    /* Rakete flog zu hoch */
    void lostInSpace();
}
