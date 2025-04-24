package miage.knarr.equipeC.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.model.CarteDestination;
import miage.knarr.equipeC.model.CarteViking;
import miage.knarr.equipeC.model.Jeu;
import miage.knarr.equipeC.model.Joueur;
import miage.knarr.equipeC.model.MoteurJeu;
import miage.knarr.equipeC.model.enums.TypeAction;
import miage.knarr.equipeC.model.enums.VikingCouleur;

public class JeuController {
    private final Jeu jeu;
    private final LoggerController loggerController;
    private boolean jeuEnCours;
    private final MoteurJeu moteurJeu;


    /**
     * Constructeur de JeuController.
     * @param jeu Instance du jeu à contrôler.
     * @param loggerController Contrôleur de la journalisation.
     */
    public JeuController(Jeu jeu, LoggerController loggerController) {
        this.jeu = jeu;
        this.loggerController = loggerController;
        this.jeuEnCours = false;
        this.moteurJeu = new MoteurJeu(jeu); 
    }

    /**
     * Démarre le jeu.
     */
    public void lancerJeu() {
        jeuEnCours = true;
        loggerController.log("Le jeu a démarré.");
        moteurJeu.lancerJeu();

        // Distribute 2 Viking cards of different colors to each player's zoneEquipage
        for (Joueur joueur : jeu.getJoueurs()) {
            List<VikingCouleur> couleursDistribuees = new ArrayList<>();
            while (couleursDistribuees.size() < 2) {
                CarteViking carte = jeu.getVikingDeck().tirer();
                if (carte != null && !couleursDistribuees.contains(carte.getCouleur())) {
                    joueur.ajouterCarteVikingEquipage(carte);
                    couleursDistribuees.add(carte.getCouleur());
                } else {
                    // If the card is null or the color is already distributed, put it back in the deck
                    if (carte != null) {
                        jeu.getVikingDeck().ajouterCarte(carte);
                    }
                }
            }
        }

        loggerController.log("\n===============================");
        loggerController.log(" Cartes de départ des joueurs ");
        loggerController.log("===============================\n");
    
        for (Joueur joueur : jeu.getJoueurs()) {
            StringBuilder cartesAffichage = new StringBuilder();
            cartesAffichage.append("  ").append(joueur.getNom()).append(" : ");
    
            List<CarteViking> cartesViking = joueur.getCartesVikingMain();
            if (cartesViking.isEmpty()) {
                cartesAffichage.append("Aucune carte Viking en main.");
            } else {
                for (CarteViking carte : cartesViking) {
                    cartesAffichage.append("\n    - ").append(carte.toString());
                }
            }
            loggerController.log(cartesAffichage.toString());
        }

        afficherCartesDuVikingDeck();
        afficherZoneEquipage();

        logCartesDestination(loggerController,jeu);
    
        loggerController.log("\n===============================");
        loggerController.log(" Cartes Viking disponibles sur le plateau ");
        loggerController.log("===============================\n");
    
        HashMap<VikingCouleur, CarteViking> emplacements = jeu.getPlateau().getEmplacementViking();
        if (emplacements.isEmpty()) {
            loggerController.log("Aucune carte Viking disponible sur le plateau.");
        } else {
            for (Map.Entry<VikingCouleur, CarteViking> entry : emplacements.entrySet()) {
                loggerController.log("  Couleur: " + entry.getKey() + ", " + entry.getValue());
            }
        }
    
    }

    /**
     * Exécute un tour pour chaque joueur.
     */
    public void jouerTour() {
        // On fait un tour pour chaque joueur
        for (Joueur joueur : jeu.getJoueurs()) {
            if (!jeuEnCours) {
                break;
            }
            loggerController.log("\nTour du joueur: " + joueur.getNom());
            loggerController.log("\nPions recrue : " + joueur.getPionsRecrue());
            loggerController.log("\nPions bracelet : " + joueur.getPionsBracelet());

            moteurJeu.calculerRennome(joueur);

            TypeAction action = joueur.choisirAction();
            loggerController.log(joueur.getNom() + " a choisi l'action: " + action);

            moteurJeu.executerAction(joueur, action);

            if (Math.random() < 0.5 && joueur.peutCommercer()) {
                joueur.deciderEtEffectuerCommerce();
            }

            if (verifierFinJeu()) {
                jeuEnCours = false;
                loggerController.log("Le jeu est terminé après le tour de " + joueur.getNom() + ".");
                break;
            }
        }
    }

    // Dans la classe JeuController
    public void jouerTourFinal() {
        Joueur joueurQuiADeclencheLeDernierTour = jeu.getJoueurQuiADeclencheLeDernierTour();
        for (Joueur joueur : jeu.getJoueurs()) {
            if (joueur.equals(joueurQuiADeclencheLeDernierTour)) {
                continue; // Skip the player who triggered the final round
            }
            loggerController.log("\nTour du joueur: " + joueur.getNom());
            loggerController.log("\nPions recrue : " + joueur.getPionsRecrue());
            loggerController.log("\nPions bracelet : " + joueur.getPionsBracelet());

            moteurJeu.calculerRennome(joueur);

            TypeAction action = joueur.choisirAction();
            loggerController.log(joueur.getNom() + " a choisi l'action: " + action);

            moteurJeu.executerAction(joueur, action);

            if (Math.random() < 0.5 && joueur.peutCommercer()) {
                joueur.deciderEtEffectuerCommerce();
            }
        }
    }

    /**
     * Vérifie si le jeu est terminé.
     * @return true si le jeu est terminé, false sinon.
     */
    public boolean verifierFinJeu() {
        return moteurJeu.verifierFinJeu();
    }

    /**
     * Termine le jeu et affiche les résultats.
     */
    public void finJeu() {
        jeuEnCours = false;
        loggerController.log("Le jeu est terminé.");
        moteurJeu.finJeu();
    }

    public void afficherZoneEquipage() {
        System.out.println("\n===============================");
        System.out.println(" LOGGER : Zone d'équipage de chaque joueur ");
        System.out.println("===============================\n");

        for (Joueur joueur : jeu.getJoueurs()) {
            StringBuilder equipageAffichage = new StringBuilder();
            equipageAffichage.append("  ").append(joueur.getNom()).append(" : ");

            HashMap<VikingCouleur, ArrayList<CarteViking>> zoneEquipage = joueur.getZoneEquipage();
            if (zoneEquipage.isEmpty()) {
                equipageAffichage.append("Aucune carte Viking dans la zone d'équipage.");
            } else {
                for (Map.Entry<VikingCouleur, ArrayList<CarteViking>> entry : zoneEquipage.entrySet()) {
                    VikingCouleur couleur = entry.getKey();
                    ArrayList<CarteViking> cartes = entry.getValue();
                    equipageAffichage.append("\n    - ").append(couleur).append(" : ");
                    if (cartes.isEmpty()) {
                        equipageAffichage.append("Aucune carte.");
                    } else {
                        for (CarteViking carte : cartes) {
                            equipageAffichage.append("\n        * ").append(carte.toString());
                        }
                    }
                }
            }
            System.out.println(equipageAffichage);
        }
        System.out.println("\n===============================");
    }

    private void afficherCartesDuVikingDeck() {
        loggerController.log("\n===============================");
        loggerController.log(" Cartes restantes dans le deck Viking ");
        loggerController.log("===============================\n");
    
        List<CarteViking> cartesRestantes = jeu.getVikingDeck().getCartes();
        int nombreCartes = cartesRestantes.size(); 
    
        if (nombreCartes == 0) {
            loggerController.log("Aucune carte Viking restante dans le deck.");
        } else {
            loggerController.log("Nombre de cartes Viking restantes: " + nombreCartes);
            for (CarteViking carte : cartesRestantes) {
                loggerController.log("  - ID: " + carte.getId() + ", " + carte);
            }
        }
    
        loggerController.log("===============================\n");
    }

    public void logCartesDestination(LoggerController loggerController, Jeu jeu) {
    loggerController.log("\n===============================");
    loggerController.log("\n===============================");
    loggerController.log(" Cartes Destination sur le plateau ");
    loggerController.log("===============================\n");

    for (CarteDestination destination : jeu.getPlateau().getEmplacementCarteDest()) {
        loggerController.log("  - " + destination.toString());
        loggerController.log("");
    }

    loggerController.log("\n===============================");
}
}