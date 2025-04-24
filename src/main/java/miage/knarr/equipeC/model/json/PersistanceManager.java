package miage.knarr.equipeC.model.json;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistanceManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DIRECTORY_PATH = "src/main/java/miage/knarr/equipeC/model/json/resultat_partie/";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String generateFileName(int numeroPartie) {
        String dateString = LocalDateTime.now().format(formatter);
        return String.format("partieKnarr_%03d_%s.json", numeroPartie, dateString);
    }

    public static void saveResults(Map<String, Object> results, int numeroPartie) {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Erreur : impossible de créer le répertoire " + DIRECTORY_PATH);
            return;
        }

        String fileName = DIRECTORY_PATH + generateFileName(numeroPartie);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), results);
            System.out.println("Résultats sauvegardés dans : " + fileName);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des résultats : " + e.getMessage());
        }
    }
}