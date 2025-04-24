package miage.knarr.equipeC.model;

import static org.junit.jupiter.api.Assertions.*;

import miage.knarr.equipeC.model.enums.TypeGain;
import org.junit.jupiter.api.Test;

import miage.knarr.equipeC.model.enums.VikingCouleur;

public class CarteVikingTest {

    @Test
    public void testCarteVikingCreation() {
        Gain gain = new Gain(TypeGain.POINT, 1);
        CarteViking carte = new CarteViking(1, 2, VikingCouleur.ROUGE, gain);

        assertEquals(1, carte.getId());
        assertEquals(2, carte.getForce());
        assertEquals(VikingCouleur.ROUGE, carte.getCouleur());
        assertEquals(gain, carte.getGain());
    }

    @Test
    public void testSetters() {
        Gain gain = new Gain(TypeGain.POINT, 1);
        CarteViking carte = new CarteViking(1, 2, VikingCouleur.ROUGE, gain);

        carte.setForce(3);
        assertEquals(3, carte.getForce());

        carte.setCouleur(VikingCouleur.BLEU);
        assertEquals(VikingCouleur.BLEU, carte.getCouleur());

        Gain newGain = new Gain(TypeGain.RENOMME, 2);
        carte.setGain(newGain);
        assertEquals(newGain, carte.getGain());
    }

    @Test
    public void testToString() {
        Gain gain = new Gain(TypeGain.POINT, 1);
        CarteViking carte = new CarteViking(1, 2, VikingCouleur.ROUGE, gain);

        String expected = "CarteViking{id=1, force=2, couleur=ROUGE, gain=POINT:1}";
        assertEquals(expected, carte.toString());
    }
}