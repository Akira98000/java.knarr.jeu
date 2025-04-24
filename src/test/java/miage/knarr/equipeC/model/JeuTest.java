package miage.knarr.equipeC.model;

import miage.knarr.equipeC.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JeuTest {

    private Jeu jeu;
    private Joueur joueur1;
    private Joueur joueur2;

    @BeforeEach
    void setUp() {
        joueur1 = createJoueur("BOTB", "Joueur 1", CouleurBateau.BLANC);
        joueur2 = createJoueur("BOTA", "Joueur 2", CouleurBateau.NOIR);
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueur1);
        joueurs.add(joueur2);
        jeu = new Jeu(joueurs, Difficulte.NORMAL);
    }

    private Joueur createJoueur(String type, String nom, CouleurBateau couleurBateau) {
        return new Joueur(type, nom, couleurBateau, 1, 1, 0, 0) {
            @Override
            public TypeAction choisirAction() {
                return null;
            }

            @Override
            public List<TypeAction> choisirActionsDuTour() {
                return List.of();
            }

            @Override
            public void deciderEtEffectuerCommerce() {
            }

            @Override
            public TypeAction choisirActionPrincipale() {
                return null;
            }

            @Override
            public void recruter() {}

            @Override
            public void explorer() {}

            @Override
            public void commercer() {}

            @Override
            public CarteViking choisirCarteEquipage() {
                return null;
            }

            @Override
            public boolean peutAcquerirCarteDestination(CarteDestination carteDestination) {
                return false;
            }

            @Override
            public boolean acquerirCarteDestination(CarteDestination carteDestination) {
                return false;
            }

            @Override
            public void utiliserPionsRecrut(int montant) {}
        };
    }

    @Test
    void testInitialiserDecks() {
        jeu.initialiserDecks();
        assertFalse(jeu.getVikingDeck().estVide(), "Le deck Viking ne doit pas être vide après initialisation.");
        assertFalse(jeu.getCarteDestination().estVide(), "Le deck Destination ne doit pas être vide après initialisation.");
    }

    @Test
    void testInitialiserPlateau() {
        jeu.initialiserDecks();
        jeu.initialiserPlateau();
        assertFalse(jeu.getPlateau().getEmplacementViking().isEmpty(), "Les emplacements Viking ne doivent pas être vides après initialisation.");
        assertFalse(jeu.getPlateau().getEmplacementCarteDest().isEmpty(), "Les emplacements de cartes Destination ne doivent pas être vides après initialisation.");
    }

    @Test
    void testMettreAJourScores() {
        jeu.mettreAJourScores(joueur1, 10, 5);
        Score score = jeu.getScoresPublics().get(joueur1);
        assertEquals(10, score.getPointsVictoire(), "Les points de victoire doivent être mis à jour correctement.");
        assertEquals(5, score.getPointsRenommee(), "Les points de renommée doivent être mis à jour correctement.");
    }

    @Test
    void testAjouterCarteVikingDefausse() {
        CarteViking carte = new CarteViking(1, 5, VikingCouleur.ROUGE, new Gain(TypeGain.RENOMME, 2));
        jeu.ajouterCarteVikingDefausse(carte);
        assertTrue(jeu.getVikingDefausse().contains(carte), "La carte Viking doit être ajoutée à la défausse.");
    }

    @Test
    void testGererPiocheViking() {
        jeu.initialiserDecks();
        jeu.gererPiocheViking();
        assertFalse(jeu.getVikingDeck().estVide(), "Le deck Viking ne doit pas être vide après gestion de la pioche.");
    }
}