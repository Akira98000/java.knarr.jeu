package miage.knarr.equipeC.model;

import miage.knarr.equipeC.model.enums.CouleurBateau;
import miage.knarr.equipeC.model.enums.TypeAction;
import miage.knarr.equipeC.model.enums.TypeGain;
import miage.knarr.equipeC.model.enums.VikingCouleur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoueurTest {

    private Joueur joueur;

    @BeforeEach
    public void setUp() {
        joueur = new Joueur("BOTA", "JoueurTest", CouleurBateau.BEIGE, 0, 0, 0, 0) {
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
            public void recruter() {

            }

            @Override
            public void explorer() {

            }

            @Override
            public void commercer() {

            }

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
            public void utiliserPionsRecrut(int montant) {

            }
        };
        joueur.getZoneEquipage().put(VikingCouleur.BLEU, new ArrayList<>());
    }

    @Test
    public void testJoueurCreation() {
        assertEquals("JoueurTest", joueur.getNom());
    }

    @Test
    public void testAjouterCarteVikingMain() {
        assertNotNull(joueur.getCartesVikingMain(), "La collection cartesVikingMain doit être initialisée.");
        CarteViking carte = new CarteViking(1, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1));
        joueur.ajouterCarteVikingMain(carte);
        assertEquals(1, joueur.getCartesVikingMain().size());
    }


    @Test
    public void testAjouterCarteVikingEquipage() {
        CarteViking carte = new CarteViking(1, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1));
        joueur.ajouterCarteVikingEquipage(carte);
        assertEquals(1, joueur.getZoneEquipage().get(VikingCouleur.BLEU).size());
    }

    @Test
    public void testAjouterCarteDestination() {
        CarteDestination carte = new CarteDestination(1, new ArrayList<>(), new HashMap<>(), new HashMap<>());
        joueur.ajouterCarteDestination(carte);
        assertEquals(1, joueur.getCartesDestination().size());
    }

    @Test
    public void testAppliquerGain() {
        Gain gain = new Gain(TypeGain.POINT, 1);
        joueur.appliquerGain(gain);
        assertEquals(1, joueur.getPointVictoire());
    }

    @Test
    public void testSetters() {
        joueur.setPointVictoire(10);
        assertEquals(10, joueur.getPointVictoire());
    }
}