package miage.knarr.equipeC.model;

import miage.knarr.equipeC.model.enums.Difficulte;
import miage.knarr.equipeC.model.json.JsonLoader;
import miage.knarr.equipeC.model.setup.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SetupTest {

    private Setup setup;

    @BeforeEach
    public void setUp() throws Exception {
        // Chemin relatif vers le fichier JSON dans le classpath
        String resourcePath = "model/json/cartes_knarr.json";

        // Charger le fichier JSON en utilisant JsonLoader
        List<CarteViking> cartesViking = JsonLoader.chargerCartesViking(resourcePath);

        // Vérifier que les cartes Viking ont été chargées correctement
        assertNotNull(cartesViking, "Les cartes Viking doivent être chargées à partir du fichier.");
        assertFalse(cartesViking.isEmpty(), "Les cartes Viking ne doivent pas être vides.");

        // Initialiser l'objet Setup avec le même chemin d'accès au fichier JSON
        setup = new Setup(resourcePath);
        assertNotNull(setup, "L'objet Setup doit être initialisé.");
    }

    @Test
    public void testInitialiserJeu() {
        setup.initialiserJeu(3, Difficulte.NORMAL, Arrays.asList("BOTA", "BOTB", "BOTC"));
        assertNotNull(setup); // Vérifie que l'objet setup n'est pas null
    }

    @Test
    public void testInitialiserJeuNombreJoueursTropEleve() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            setup.initialiserJeu(5, Difficulte.NORMAL, Arrays.asList("BOTA", "BOTB", "BOTC", "BOTD", "BOTE"));
        });
        assertEquals("Nombre de joueurs trop élevé. Maximum possible: 4", exception.getMessage());
    }

    @Test
    public void testInitialiserJeuNombreBotTypesIncorrect() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            setup.initialiserJeu(3, Difficulte.NORMAL, Arrays.asList("BOTA", "BOTB"));
        });
        assertEquals("Le nombre de types de bots doit correspondre au nombre de joueurs.", exception.getMessage());
    }
}
