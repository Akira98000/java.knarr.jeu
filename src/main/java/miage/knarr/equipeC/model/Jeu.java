package miage.knarr.equipeC.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.model.enums.Difficulte;
import miage.knarr.equipeC.model.interfce.JeuInterface;
import miage.knarr.equipeC.model.json.JsonLoader;

/**
 * Classe représentant l'état global du jeu.
 */
public class Jeu implements JeuInterface, Clonable<Jeu> {
    private List<Joueur> joueurs;
    private int nbJoueurActuel;
    private Deck<CarteViking> vikingDeck;
    private List<CarteViking> vikingDefausse;
    private Deck<CarteDestination> carteDestination;
    private final Difficulte difficulte;
    private Plateau plateau;
    private final Map<Joueur, Score> scoresPublics;
    private boolean finalRound = false;
    private Joueur joueurQuiADeclencheLeDernierTour;
    private boolean isClone;

    /**
     * Constructeur principal pour initialiser le jeu avec les joueurs et la difficulté.
     *
     * @param joueurs    Liste des joueurs participant au jeu.
     * @param difficulte Niveau de difficulté du jeu.
     */
    public Jeu(List<Joueur> joueurs, Difficulte difficulte) {
        this.joueurs = joueurs;
        this.nbJoueurActuel = joueurs.size();
        this.vikingDeck = new Deck<>();
        this.vikingDefausse = new ArrayList<>();
        this.carteDestination = new Deck<>();
        this.difficulte = difficulte;
        this.plateau = new Plateau(this);
        this.scoresPublics = new HashMap<>();
        this.isClone = false;         

        // Initialisation des scores publics pour chaque joueur
        for (Joueur joueur : joueurs) {
            scoresPublics.put(joueur, new Score(0, 0));
        }
    }

    @Override
    public Jeu clone() {
        try {
            Jeu cloned = (Jeu) super.clone();
            // Deep clone mutable fields
            cloned.joueurs = new ArrayList<>();
            for (Joueur joueur : this.joueurs) {
                cloned.joueurs.add(joueur.clone());
            }
            cloned.vikingDeck = this.vikingDeck.clone();
            cloned.vikingDefausse = new ArrayList<>();
            for (CarteViking carte : this.vikingDefausse) {
                cloned.vikingDefausse.add(carte.clone());
            }
            cloned.carteDestination = this.carteDestination.clone();
            cloned.plateau = this.plateau.clone();
            cloned.isClone = true;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }


    // ---------------------- Getters ----------------------

    /**
     * Obtient la liste des joueurs.
     *
     * @return Liste non modifiable des joueurs.
     */
    @Override
    public List<Joueur> getJoueurs() {
        return Collections.unmodifiableList(joueurs);
    }

    /**
     * Obtient le niveau de difficulté du jeu.
     *
     * @return Niveau de difficulté.
     */
    @Override
    public Difficulte getDifficulte() {
        return difficulte;
    }

    /**
     * Obtient les scores publics des joueurs.
     *
     * @return Map non modifiable des scores publics.
     */
    @Override
    public Map<Joueur, Score> getScoresPublics() {
        return Collections.unmodifiableMap(scoresPublics);
    }

    /**
     * Obtient le plateau de jeu.
     *
     * @return Instance du plateau.
     */
    @Override
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Obtient le deck de cartes Viking.
     *
     * @return Instance du deck de Vikings.
     */
    @Override
    public Deck<CarteViking> getVikingDeck() {
        return vikingDeck;
    }

    /**
     * Obtient le deck de cartes Destination.
     *
     * @return Instance du deck de Destinations.
     */
    @Override
    public Deck<CarteDestination> getCarteDestination() {
        return carteDestination;
    }

    /**
     * Obtient la liste des cartes Viking défaussées.
     *
     * @return Liste non modifiable des cartes Viking défaussées.
     */
    @Override
    public List<CarteViking> getVikingDefausse() {
        return Collections.unmodifiableList(vikingDefausse);
    }

    /**
     * Met à jour les scores publics d'un joueur.
     *
     * @param joueur          Joueur dont les scores doivent être mis à jour.
     * @param pointsVictoire  Nouveaux points de victoire.
     * @param pointsRenommee  Nouveaux points de renommée.
     */
    @Override
    public void mettreAJourScores(Joueur joueur, int pointsVictoire, int pointsRenommee) {
        Score score = scoresPublics.get(joueur);
        if (score != null) {
            score.setPointsVictoire(pointsVictoire);
            score.setPointsRenommee(pointsRenommee);
        }
    }

    /**
     * Ajoute une carte Viking à la défausse.
     *
     * @param carte Carte Viking à défausser.
     */
    @Override
    public void ajouterCarteVikingDefausse(CarteViking carte) {
        if (carte != null) {
            vikingDefausse.add(carte);
        }
    }

    // ---------------------- Configuration des Decks ----------------------

    @Override
    public void initialiserDecks() {
        try {
            List<CarteViking> cartesViking = JsonLoader.chargerCartesViking("model/json/cartes_knarr.json");
            vikingDeck.setCartes(cartesViking);
            vikingDeck.melanger();

            // Distribute 3 Viking cards to each player after clearing their hands
            for (Joueur joueur : joueurs) {
                joueur.getCartesVikingMain().clear(); // Clear existing cards
                for (int i = 0; i < 3; i++) {
                    CarteViking carte = vikingDeck.tirer();
                    if (carte != null) {
                        joueur.ajouterCarteVikingMain(carte);
                    }
                }
            }

            // Load Destination cards from JSON
            List<CarteDestination> cartesDestination = JsonLoader.chargerCartesDestination("model/json/cartes_knarr.json");
            carteDestination.setCartes(cartesDestination);
            carteDestination.melanger();

            // Initialize other decks
            initialiserDeckDestination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void initialiserDeckDestination() {
    try {
        // Load destination cards from JSON file
        List<CarteDestination> cartesDestination = JsonLoader.chargerCartesDestination("model/json/cartes_knarr.json");
        // Set the loaded cards to the destination deck
        setCartesDestination(cartesDestination);
        
    } catch (Exception e) {
        System.err.println("\n====================================");
        System.err.println("    ERROR LOADING DESTINATION CARDS    ");
        System.err.println("====================================");
        System.err.println("Error details: " + e.getMessage());
        System.err.println("Stack trace:");
        e.printStackTrace();
        
        List<CarteDestination> fallbackCards = new ArrayList<>();
        setCartesDestination(fallbackCards);
        
        System.err.println("Initialized with empty fallback deck");
        System.err.println("====================================\n");
    }
}
    /**
     * Obtient l'état de la partie.
     *
     * @return Vrai si c'est le dernier tour, faux sinon.
     */
    public boolean isFinalRound() {
        return finalRound;
    }

    /**
     * Définit l'état de la partie.
     *
     * @param finalRound Vrai si c'est le dernier tour, faux sinon.
     */
    public void setFinalRound(boolean finalRound) {
        this.finalRound = finalRound;
    }

    /**
     * Définit les cartes Destination dans le deck.
     *
     * @param cartesDestination Liste de cartes Destination à ajouter.
     */
    @Override
    public void setCartesDestination(List<CarteDestination> cartesDestination) {
        this.carteDestination.setCartes(cartesDestination);
    }

    public Joueur getJoueurQuiADeclencheLeDernierTour() {
        return joueurQuiADeclencheLeDernierTour;
    }

    public void setJoueurQuiADeclencheLeDernierTour(Joueur joueur) {
        this.joueurQuiADeclencheLeDernierTour = joueur;
    }

    // ---------------------- Initialisation du Plateau ----------------------

    /**
     * Initialise les emplacements sur le plateau de jeu.
     */
    @Override
    public void initialiserPlateau() {
        plateau.initialiserEmplacements(vikingDeck, carteDestination);
    }

    public void gererPiocheViking() {
        if (vikingDeck.estVide()) {
            if (!vikingDefausse.isEmpty()) {
                Collections.shuffle(vikingDefausse);
                vikingDeck.setCartes(new ArrayList<>(vikingDefausse));
                vikingDeck.melanger();
                vikingDefausse.clear();
                if (isClone) {
                    //System.out.println("[CLONE] Viking deck has been recreated by shuffling the discard pile.");
                } else {
                    System.out.println("La pioche Viking a été recréée en mélangeant la défausse.");
                }
            } else {
                Joueur joueurAvecPlusDeCartes = null;
                int maxCartes = 0;

                // Find the player with the most Viking cards in the crew
                for (Joueur joueur : joueurs) {
                    int nombreCartes = joueur.getNombreCartesEquipage();
                    if (nombreCartes > maxCartes) {
                        maxCartes = nombreCartes;
                        joueurAvecPlusDeCartes = joueur;
                    }
                }

                if (joueurAvecPlusDeCartes != null) {
                    // Let the player choose a card from their crew
                    CarteViking carteChoisie = joueurAvecPlusDeCartes.choisirCarteEquipage();
                    if (carteChoisie != null) {
                        joueurAvecPlusDeCartes.getZoneEquipage().get(carteChoisie.getCouleur()).remove(carteChoisie);
                        vikingDeck.ajouterCarte(carteChoisie);
                        if (isClone) {
                            //System.out.println("[CLONE] A Viking card has been taken from " + joueurAvecPlusDeCartes.getNom() + "'s crew and added to the deck.");
                        } else {
                            System.out.println("Une carte Viking a été prise de la zone d'équipage de " + joueurAvecPlusDeCartes.getNom() + " et ajoutée à la pioche.");
                        }
                    }
                }
            }
        }
    }
}