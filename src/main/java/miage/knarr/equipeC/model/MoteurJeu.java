package miage.knarr.equipeC.model;

import java.util.ArrayList;
import java.util.List;

import miage.knarr.equipeC.controller.LoggerController;
import miage.knarr.equipeC.model.enums.TypeAction;

/**
 * Classe représentant le moteur principal du jeu.
 * Elle contient la logique et les règles du jeu.
 */
public class MoteurJeu {
    private final Jeu jeu;
    LoggerController loggerController;

    /**
     * Constructeur de la classe MoteurJeu.
     *
     * @param jeu Instance de la classe Jeu contenant l'état du jeu.
     */
    public MoteurJeu(Jeu jeu) {
        this.jeu = jeu;
        this.loggerController = new LoggerController(); // Initialisez l'instance

    }

    /**
     * Démarre le jeu en initialisant les decks et le plateau.
     */
    public void lancerJeu() {
        // Initialiser les decks avec des cartes spécifiques
        jeu.initialiserDecks();

        // Mélanger les decks
        jeu.getVikingDeck().melanger();
        jeu.getCarteDestination().melanger();
        // Ajoutez d'autres decks si nécessaire, par exemple Echange et Influence
        // jeu.getCarteEchange().melanger();
        // jeu.getCarteInfluence().melanger();

        // Initialiser les emplacements sur le plateau
        jeu.getPlateau().initialiserEmplacements(jeu.getVikingDeck(), jeu.getCarteDestination());
    }

    /**
     * Exécute le tour d'un joueur, incluant les actions possibles.
     *
     * @param joueur Joueur qui joue son tour.
     */
    public void executerTour(Joueur joueur) {
        int pointsRenommee = joueur.getPointRenomme();
        joueur.ajouterPointVictoire(pointsRenommee);


        joueur.debuterTour(); // Réinitialiser les actions du joueur

        loggerController.log("===============================");
        loggerController.log("Tour du joueur: " + joueur.getNom());

        // Le joueur choisit les actions à effectuer pendant son tour
        List<TypeAction> actionsDuTour = joueur.choisirActionsDuTour();

        for (TypeAction action : actionsDuTour) {
            if (!peutEffectuerAction(joueur, action)) {
                System.out.println("Action non permise pour le joueur " + joueur.getNom() + ": " + action);
                continue;
            }
            loggerController.log(joueur.getNom() + " a choisi l'action: " + action);
            executerAction(joueur, action);
        }

        // Vérifier les conditions de fin de jeu après le tour du joueur
        if (verifierFinJeu()) {
            finJeu();
        }
    }

    /**
     * Exécute une action choisie par le joueur.
     *
     * @param joueur Joueur qui exécute l'action.
     * @param action Type d'action à exécuter.
     */
    public void executerAction(Joueur joueur, TypeAction action) {
        switch (action) {
            case RECRUTER -> joueur.recruter();
            case EXPLORER -> joueur.explorer();
            //case COMMERCER -> joueur.commercer();
            default -> System.out.println("Action inconnue : " + action);
        }
    }

    /**
     * Vérifie si le jeu est terminé selon les conditions définies.
     *
     * @return true si le jeu est terminé, false sinon.
     */
    public boolean verifierFinJeu() {
        if (jeu.isFinalRound()) {
            return true;
        }

        for (Joueur joueur : jeu.getJoueurs()) {
            if (joueur.getPointVictoire() >= 40) {
                jeu.setFinalRound(true);
                jeu.setJoueurQuiADeclencheLeDernierTour(joueur);
                return false;
            }
        }
        return false;
    }

    /**
     * Termine le jeu en déterminant le gagnant et en affichant les résultats.
     */
    public void finJeu() {
        List<Joueur> gagnants = new ArrayList<>();
        int maxPoints = -1;

        for (Joueur joueur : jeu.getJoueurs()) {
            int points = joueur.getPointVictoire();
            if (points > maxPoints) {
                maxPoints = points;
                gagnants.clear();
                gagnants.add(joueur);
            } else if (points == maxPoints) {
                gagnants.add(joueur);
            }
        }

        if (gagnants.size() == 1) {
            System.out.println("Le gagnant est " + gagnants.get(0).getNom() +
                    " avec " + maxPoints + " points de victoire !");
        } else {
            Joueur gagnantFinal = null;
            int maxRessources = -1;
            for (Joueur joueur : gagnants) {
                int ressources = joueur.getPionsRecrue() + joueur.getPionsBracelet();
                if (ressources > maxRessources) {
                    maxRessources = ressources;
                    gagnantFinal = joueur;
                }
            }
            if (gagnantFinal != null) {
                System.out.println("Le gagnant est " + gagnantFinal.getNom() +
                        " avec " + maxPoints + " points de victoire et " +
                        maxRessources + " ressources (recrues et bracelets) !");
            } else {
                System.out.println("Égalité parfaite entre les joueurs !");
            }
        }

        // Afficher les points de victoire et de renommée de chaque joueur
        System.out.println("\nScores finaux des joueurs :");
        for (Joueur joueur : jeu.getJoueurs()) {
            System.out.println(joueur.getNom() + " - Points de victoire : " + joueur.getPointVictoire() +
                    ", Points de renommée : " + joueur.getPointRenomme());
        }
    }

    /**
     * Vérifie si un joueur peut effectuer une action spécifique.
     *
     * @param joueur Joueur concerné.
     * @param action Action à vérifier.
     * @return true si l'action est permise, false sinon.
     */
    private boolean peutEffectuerAction(Joueur joueur, TypeAction action) {
        return switch (action) {
            case RECRUTER -> !joueur.getCartesVikingMain().isEmpty();
            case EXPLORER -> !jeu.getPlateau().getEmplacementCarteDest().isEmpty();
            //case COMMERCER -> joueur.peutCommercer();
            default -> false;
        };
    }

    public Jeu getJeu() {
        return jeu;
    }

    public void setLoggerController(LoggerController loggerController) {
        this.loggerController = loggerController;

    }

    public void calculerRennome(Joueur joueur) {
        int renomme = joueur.getPointRenomme();
        int pointsVictoire = 0;
        if (renomme >= 14) {
            pointsVictoire += 5;
        } else if (renomme >= 10) {
            pointsVictoire += 3;
        } else if (renomme >= 6) {
            pointsVictoire += 2;
        } else if (renomme >= 3) {
            pointsVictoire += 1;
        }
        joueur.ajouterPointVictoire(pointsVictoire);
        System.out.println("Points de victoire gagné grâce à la renommée : " + pointsVictoire);
    }
}