package miage.knarr.equipeC.controller;

import java.time.format.DateTimeFormatter;

/**
 * Classe contrôlant la journalisation des événements du jeu.
 */
public class LoggerController {

    /**
     * Constructeur de LoggerController.
     */
    public LoggerController() {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Enregistre un message dans le journal.
     * @param message Message à enregistrer.
     */
    public void log(String message) {
        System.out.println(message);
    }
}
