package champollion;

import java.util.Date;

public class Intervention {
    private final Date debut;
    private final int duree;
    private final UE ue;
    private final Salle salle;
    private final TypeIntervention type;

    public Intervention(Date debut, int duree, UE ue, Salle salle, TypeIntervention type) {
        this.debut = debut;
        this.duree = duree;
        this.ue = ue;
        this.salle = salle;
        this.type = type;
    }

    public Date getDebut() {
        return debut;
    }

    public int getDuree() {
        return duree;
    }

    public UE getUe() {
        return ue;
    }

    public Salle getSalle() {
        return salle;
    }

    public TypeIntervention getType() {
        return type;
    }
}
