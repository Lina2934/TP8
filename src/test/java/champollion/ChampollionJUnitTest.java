package champollion;

import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ChampollionJUnitTest {

	// Déclaration des objets à tester
	Enseignant untel;
	UE uml, java;

	// Méthode d'initialisation, exécutée avant chaque test
	@BeforeEach
	public void setUp() {
		// Initialisation des objets
		untel = new Enseignant("untel", "untel@gmail.com");
		uml = new UE("UML");
		java = new UE("Programmation en java");
	}

	// Test 1 : Vérifier qu'un nouvel enseignant n'a pas d'heures prévues
	@Test
	public void testNouvelEnseignantSansService() {
		// Vérification que l'enseignant a bien 0 heures prévues
		assertEquals(0, untel.heuresPrevues(),
				"Un nouvel enseignant doit avoir 0 heures prévues");
	}

	// Test 2 : Ajouter des heures et vérifier la mise à jour pour une UE
	@Test
	public void testAjouteHeures() {
		// Ajout de 10 heures de TD pour l'UE "UML"
		untel.ajouteEnseignement(uml, 0, 10, 0);
		// Vérification que les heures sont bien comptabilisées pour l'UE "UML"
		assertEquals(10, untel.heuresPrevuesPourUE(uml),
				"L'enseignant doit maintenant avoir 10 heures prévues pour l'UE 'uml'");

		// Ajout de 20 heures supplémentaires pour l'UE "UML"
		untel.ajouteEnseignement(uml, 0, 20, 0);
		// Vérification que les heures sont bien cumulées pour l'UE "UML"
		assertEquals(30, untel.heuresPrevuesPourUE(uml),
				"L'enseignant doit maintenant avoir 30 heures prévues pour l'UE 'uml'");
	}

	// Test 3 : Vérifier l'ajout d'heures pour une autre UE
	@Test
	public void testAjoutHeuresPourAutreUE() {
		// Ajouter 15 heures de TD pour l'UE "Java"
		untel.ajouteEnseignement(java, 0, 15, 0);
		// Vérification que l'enseignant a bien 15 heures pour l'UE "Java"
		assertEquals(15, untel.heuresPrevuesPourUE(java),
				"L'enseignant doit avoir 15 heures prévues pour l'UE 'Programmation en java'");
	}

	// Test 4 : Vérification que l'enseignant n'a pas d'heures pour une UE non affectée
	@Test
	public void testHeuresNonAffectees() {
		// Vérifier que l'enseignant n'a pas d'heures pour une UE sans enseignement ajouté
		assertEquals(0, untel.heuresPrevuesPourUE(java),
				"L'enseignant ne doit avoir aucune heure pour l'UE non affectée.");
	}

	// Test 5 : Vérification du total des heures prévues de l'enseignant
	@Test
	public void testTotalHeuresPrevues() {
		// Ajouter des heures pour différentes UEs
		untel.ajouteEnseignement(uml, 0, 10, 0);  // 10h pour UML
		untel.ajouteEnseignement(java, 0, 20, 0);  // 20h pour Java
		// Vérification du total des heures
		assertEquals(30, untel.heuresPrevues(),
				"Le total des heures prévues pour l'enseignant devrait être de 30.");
	}

	// Test 6 : Vérification du bon calcul des heures prévues avec un cas où l'enseignant n'a aucune heure
	@Test
	public void testAucuneHeurePrevues() {
		// Vérification que l'enseignant n'a aucune heure avant tout ajout
		assertEquals(0, untel.heuresPrevues(),
				"Un enseignant sans ajout d'heures ne doit avoir aucune heure prévue.");
	}
	// Test 8 : Vérifier qu'un enseignant est en sous-service
	@Test
	public void testSousService() {
		// L'enseignant "untel" n'a encore aucune heure de prévue
		assertTrue(untel.enSousService(),
				"L'enseignant doit être en sous-service s'il n'a pas au moins 192 heures prévues.");

		// Ajouter des heures pour l'UE UML
		untel.ajouteEnseignement(uml, 10, 0, 0);

		// Vérification qu'il est toujours en sous-service
		assertTrue(untel.enSousService(),
				"L'enseignant doit être en sous-service avec moins de 192 heures.");

		// Ajouter plus d'heures pour l'UE Java
		untel.ajouteEnseignement(java, 50, 0, 0);

		// Vérification qu'il est toujours en sous-service
		assertTrue(untel.enSousService(),
				"L'enseignant doit toujours être en sous-service même après avoir ajouté des heures.");

		// Ajouter suffisamment d'heures pour dépasser les 192 heures
		untel.ajouteEnseignement(uml, 100, 0, 0);  // Total des heures > 192

		// Vérification que l'enseignant n'est plus en sous-service
		assertFalse(untel.enSousService(),
				"L'enseignant ne doit pas être en sous-service avec plus de 192 heures.");
	}

	@Test
	public void testEnSousService() {
		// Ajouter un grand nombre d'heures pour simuler un total supérieur à 192 heures
		untel.ajouteEnseignement(uml, 100, 50, 30);  // 100h CM, 50h TD, 30h TP pour UML
		untel.ajouteEnseignement(java, 40, 25, 15);   // 40h CM, 25h TD, 15h TP pour Java

		// Vérifier que l'enseignant n'est pas en sous-service
		assertFalse(untel.enSousService(), "L'enseignant ne doit pas être en sous-service.");
	}
	@Test
	public void testAucuneHeurePlanifieeAvantAjout() {
		// Vérifier que l'enseignant n'a pas d'heures planifiées avant d'ajouter des enseignements
		assertEquals(0, untel.heuresPrevues(), "Aucune heure ne doit être planifiée au départ.");
	}
	@Test
	public void testResteAPlanifierAvecInterventions() {
		// Ajouter des heures pour UML
		untel.ajouteEnseignement(uml, 10, 10, 10);  // 10h CM, 10h TD, 10h TP pour UML

		// Créer des interventions
		Date date = new Date();
		Intervention interventionCM = new Intervention(date, 5, uml, new Salle("Salle A", 30), TypeIntervention.CM);
		Intervention interventionTD = new Intervention(date, 5, uml, new Salle("Salle B", 30), TypeIntervention.TD);

		// Ajouter les interventions
		untel.ajouteIntervention(interventionCM);
		untel.ajouteIntervention(interventionTD);

		// Vérifier que les heures restantes à planifier sont correctement mises à jour
		assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.CM), "Il reste 5 heures de CM à planifier.");
		assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.TD), "Il reste 5 heures de TD à planifier.");
		assertEquals(10, untel.resteAPlanifier(uml, TypeIntervention.TP), "Il reste 10 heures de TP à planifier.");
	}

}
