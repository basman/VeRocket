package rakete;

/**
 * Created by Roman on 06.12.2015.
 */
public interface Rakete {

    double getHoehe();
    double getLastHoehe();
    float getBrennrate();
    float getGeschwindigkeit();
    float getBrennstoffVorrat();
    float getTankvolumen();
    float getLeergewicht();
    boolean istGestartet();
}
