package champollion;

import java.util.*;

/**
 * Un enseignant est caractérisé par ses informations personnelles, son service prévu et ses interventions planifiées.
 */
public class Enseignant extends Personne {
    private final Map<UE, ServicePrevu> servicesPrevus = new HashMap<>();
    private final List<Intervention> interventionsPlanifiees = new ArrayList<>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD".
     *
     * @return Le nombre total d'heures "équivalent TD" prévues.
     */
    public int heuresPrevues() {
        return servicesPrevus.values().stream()
                .mapToInt(ServicePrevu::calculerHeuresEquivalentTD)
                .sum();
    }

    /**
     * Calcule le nombre d'heures prévues pour cet enseignant dans une UE donnée, en "heures équivalent TD".
     *
     * @param ue L'UE concernée.
     * @return Le nombre d'heures prévues dans cette UE.
     */
    public int heuresPrevuesPourUE(UE ue) {
        ServicePrevu service = servicesPrevus.get(ue);
        return service == null ? 0 : service.calculerHeuresEquivalentTD();
    }

    /**
     * Ajoute un enseignement au service prévu pour cet enseignant.
     *
     * @param ue       L'UE concernée.
     * @param volumeCM Volume d'heures de CM.
     * @param volumeTD Volume d'heures de TD.
     * @param volumeTP Volume d'heures de TP.
     */
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        servicesPrevus.putIfAbsent(ue, new ServicePrevu(ue, 0, 0, 0));
        ServicePrevu service = servicesPrevus.get(ue);
        service.setVolumeCM(service.getVolumeCM() + volumeCM);
        service.setVolumeTD(service.getVolumeTD() + volumeTD);
        service.setVolumeTP(service.getVolumeTP() + volumeTP);
    }

    /**
     * Ajoute une intervention planifiée pour cet enseignant.
     *
     * @param intervention L'intervention à ajouter.
     * @throws IllegalArgumentException si elle dépasse le service prévu.
     */
    public void ajouteIntervention(Intervention intervention) {
        UE ue = intervention.getUe();
        TypeIntervention type = intervention.getType();
        int heuresPlanifiees = interventionsPlanifiees.stream()
                .filter(i -> i.getUe().equals(ue) && i.getType() == type)
                .mapToInt(Intervention::getDuree)
                .sum();
        int heuresPrevues = switch (type) {
            case CM -> servicesPrevus.getOrDefault(ue, new ServicePrevu(ue, 0, 0, 0)).getVolumeCM();
            case TD -> servicesPrevus.getOrDefault(ue, new ServicePrevu(ue, 0, 0, 0)).getVolumeTD();
            case TP -> servicesPrevus.getOrDefault(ue, new ServicePrevu(ue, 0, 0, 0)).getVolumeTP();
        };

        if (heuresPlanifiees + intervention.getDuree() > heuresPrevues) {
            throw new IllegalArgumentException("Intervention dépasse le service prévu.");
        }
        interventionsPlanifiees.add(intervention);
    }

    /**
     * Calcule le reste à planifier pour un type d'intervention dans une UE donnée.
     *
     * @param ue   L'UE concernée.
     * @param type Le type d'intervention.
     * @return Le volume horaire restant à planifier.
     */
    public int resteAPlanifier(UE ue, TypeIntervention type) {
        int heuresPlanifiees = interventionsPlanifiees.stream()
                .filter(i -> i.getUe().equals(ue) && i.getType() == type)
                .mapToInt(Intervention::getDuree)
                .sum();

        int heuresPrevues = switch (type) {
            case CM -> servicesPrevus.getOrDefault(ue, new ServicePrevu(ue, 0, 0, 0)).getVolumeCM();
            case TD -> servicesPrevus.getOrDefault(ue, new ServicePrevu(ue, 0, 0, 0)).getVolumeTD();
            case TP -> servicesPrevus.getOrDefault(ue, new ServicePrevu(ue, 0, 0, 0)).getVolumeTP();
        };

        return Math.max(0, heuresPrevues - heuresPlanifiees);
    }

    /**
     * Vérifie si l'enseignant est en sous-service.
     *
     * @return true si l'enseignant a moins de 192 heures prévues, sinon false.
     */
    public boolean enSousService() {
        return heuresPrevues() < 192;
    }


}
