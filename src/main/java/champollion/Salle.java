package champollion;

public class Salle {
    private final String intitule;
    private final int capacite;

    public Salle(String intitule, int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité d'une salle doit être supérieure à 0.");
        }
        this.intitule = intitule;
        this.capacite = capacite;
    }

    public String getIntitule() {
        return intitule;
    }

    public int getCapacite() {
        return capacite;
    }
}
