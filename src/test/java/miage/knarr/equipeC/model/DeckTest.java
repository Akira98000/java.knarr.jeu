package miage.knarr.equipeC.model;

import miage.knarr.equipeC.model.enums.TypeGain;
import miage.knarr.equipeC.model.enums.VikingCouleur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck<CarteViking> deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck<>();
        assertNotNull(deck, "L'objet Deck doit être initialisé.");
    }

    @Test
    public void testPiocher() {
        CarteViking carte = new CarteViking(1, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1));
        deck.ajouterCarte(carte);
        assertNotNull(deck.piocher(), "La méthode piocher doit retourner une carte.");
    }


    @Test
    public void testTirer() {
        CarteViking carte = new CarteViking(2, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1));
        deck.ajouterCarte(carte);
        assertNotNull(deck.tirer());
    }

    @Test
    public void testEstVide() {
        assertTrue(deck.estVide(), "Le deck doit être vide au départ.");
        deck.ajouterCarte(new CarteViking(1, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1)));
        assertFalse(deck.estVide(), "Le deck ne doit pas être vide après ajout.");
    }

    @Test
    public void testAjouterCarte() {
        CarteViking carte = new CarteViking(3, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1));
        deck.ajouterCarte(carte);
        assertTrue(deck.getCartes().contains(carte));
    }

    @Test
    public void testRetirerCarte() {
        CarteViking carte = new CarteViking(4, 1, VikingCouleur.BLEU, new Gain(TypeGain.POINT, 1));
        deck.ajouterCarte(carte);
        deck.retirerCarte(carte);
        assertFalse(deck.getCartes().contains(carte));
    }
}