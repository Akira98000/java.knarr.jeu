// src/main/java/miage/knarr/equipeC/App.java
package miage.knarr.equipeC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.controller.JeuController;
import miage.knarr.equipeC.controller.LoggerController;
import miage.knarr.equipeC.model.Jeu;
import miage.knarr.equipeC.model.Joueur;
import miage.knarr.equipeC.model.enums.Difficulte;
import miage.knarr.equipeC.model.json.PersistanceManager;
import miage.knarr.equipeC.model.setup.Setup;

public class App {

    public static void main(String[] args) {
        try {
            System.out.println("Arguments reçus :");
            for (int i = 0; i < args.length; i++) {
                System.out.println("args[" + i + "] = " + args[i]);
            }

            if (args.length < 3) {
                throw new IllegalArgumentException("Veuillez fournir les arguments suivants : nombre de joueurs, nombre de parties, et types de bots (ex: 3 3 BOTA BOTB BOTC).");
            }

            int nombreJoueurs;
            try {
                nombreJoueurs = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Le nombre de joueurs doit être un entier valide.", e);
            }

            if (nombreJoueurs < 2 || nombreJoueurs > 4) {
                throw new IllegalArgumentException("Le nombre de joueurs doit être compris entre 2 et 4.");
            }

            int nombreParties;
            try {
                nombreParties = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Le nombre de parties doit être un entier valide.", e);
            }

            if (nombreParties < 1) {
                throw new IllegalArgumentException("Le nombre de parties doit être au moins 1.");
            }

            List<String> botTypes = new ArrayList<>();
            botTypes.addAll(Arrays.asList(args).subList(2, args.length));

            if (botTypes.size() != nombreJoueurs) {
                throw new IllegalArgumentException("Le nombre de types de bots doit correspondre au nombre de joueurs.");
            }

            Difficulte difficulteChoisie = Difficulte.NORMAL;

            System.out.println("Nombre de joueurs : " + nombreJoueurs);
            System.out.println("Nombre de parties : " + nombreParties);

            if (nombreParties == 20) {
                Map<String, Object> classementGlobal = new LinkedHashMap<>();
                for (int i = 1; i <= nombreParties; i++) {
                    System.out.println("\n--- Partie " + i + " ---");

                    String jsonFilePath = "src/main/resources/carte_viking.json";
                    Setup setup = new Setup(jsonFilePath);
                    Jeu jeu = setup.initialiserJeu(nombreJoueurs, difficulteChoisie, botTypes).getJeu();
                    LoggerController logger = new LoggerController();
                    JeuController jeuController = new JeuController(jeu, logger);

                    jouerUnePartie(jeu, jeuController);

                    Map<String, Object> joueursMap = new LinkedHashMap<>();

                    for (Joueur joueur : jeu.getJoueurs()) {
                        Map<String, Object> points = new LinkedHashMap<>();
                        points.put("PointRenommer", joueur.getPointRenomme());
                        points.put("PointVictoire", joueur.getPointVictoire());
                        points.put("Type", joueur.getType());
                        joueursMap.put(joueur.getNom(), points);
                    }

                    classementGlobal.put("Partie " + i, joueursMap);
                }

                Map<String, Object> resultatFinal = new LinkedHashMap<>();
                resultatFinal.put("Classement", classementGlobal);
                PersistanceManager.saveResults(resultatFinal, 0); // Utiliser 0 pour indiquer une sauvegarde globale

            } else {
                Map<String, Object> classementGlobal = new LinkedHashMap<>();
                for (int i = 1; i <= nombreParties; i++) {
                    System.out.println("\n--- Partie " + i + " ---");

                    String jsonFilePath = "src/main/resources/carte_viking.json";
                    Setup setup = new Setup(jsonFilePath);
                    Jeu jeu = setup.initialiserJeu(nombreJoueurs, difficulteChoisie, botTypes).getJeu();
                    LoggerController logger = new LoggerController();
                    JeuController jeuController = new JeuController(jeu, logger);

                    jouerUnePartie(jeu, jeuController);

                    Map<String, Object> joueursMap = new LinkedHashMap<>();

                    for (Joueur joueur : jeu.getJoueurs()) {
                        Map<String, Object> points = new LinkedHashMap<>();
                        points.put("PointRenommer", joueur.getPointRenomme());
                        points.put("PointVictoire", joueur.getPointVictoire());
                        points.put("Type", joueur.getType());
                        joueursMap.put(joueur.getNom(), points);
                    }

                    classementGlobal.put("Partie " + i, joueursMap);
                }

                Map<String, Object> resultatFinal = new LinkedHashMap<>();
                resultatFinal.put("Classement", classementGlobal);
                PersistanceManager.saveResults(resultatFinal, 0); // Utiliser 0 pour indiquer une sauvegarde globale
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void jouerUnePartie(Jeu jeu, JeuController jeuController) {
        long startTime = System.currentTimeMillis(); // Horodatage début 
        jeuController.lancerJeu();
        int tour = 1;

        while (!jeuController.verifierFinJeu()) {
            System.out.println("\n========================================");
            System.out.println("Nouveau tour : " + tour);
            System.out.println("========================================\n");
            jeuController.jouerTour();
            tour++;
        }

        if (jeu.isFinalRound()) {
            System.out.println("\n========================================");
            System.out.println("Dernier tour : " + tour);
            System.out.println("========================================\n");
            jeuController.jouerTourFinal();
        }

        jeuController.finJeu();
        long endTime = System.currentTimeMillis(); // Horodatage fin
        // Calcul et affichage de la durée
        long duration = endTime - startTime;
        System.out.println("\n========================================");
        System.out.println("Durée de la partie : " + (duration / 1000) + " secondes");
        System.out.println("========================================\n");
    }
}