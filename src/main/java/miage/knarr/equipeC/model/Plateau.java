package miage.knarr.equipeC.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miage.knarr.equipeC.model.enums.VikingCouleur;

/**
 * Classe représentant le plateau de jeu.
 * Gère les emplacements des Vikings, les cartes Destination, Echange et Influence.
 */
public class Plateau implements Cloneable {
    private HashMap<VikingCouleur, CarteViking> emplacementViking;
    private List<CarteDestination> emplacementCarteDest;
    private Deck<CarteDestination> destinationDeck;
    private boolean isClone;
    private Jeu jeu;

    
    @Override
    public Plateau clone() throws CloneNotSupportedException {
        Plateau cloned = (Plateau) super.clone();
        cloned.emplacementViking = new HashMap<>(this.emplacementViking);
        cloned.emplacementCarteDest = new ArrayList<>(this.emplacementCarteDest);
        cloned.destinationDeck = this.destinationDeck.clone();
        cloned.jeu = this.jeu;
        cloned.isClone = true;
        return cloned;
    }

    /**
     * Constructeur de la classe Plateau.
     * Initialise les collections utilisées pour les emplacements et les cartes.
     */
    public Plateau(Jeu jeu) {
        this.jeu = jeu; 
        this.emplacementViking = new HashMap<>();
        this.emplacementCarteDest = new ArrayList<>();
        this.isClone = false; 
    }

    public boolean isClone() {
        return isClone;
    }



    /**
     * Obtient l'emplacement des Vikings.
     *
     * @return HashMap des emplacements des Vikings.
     */
    public HashMap<VikingCouleur, CarteViking> getEmplacementViking() {
        return emplacementViking;
    }

    /**
     * Obtient les cartes Destination sur le plateau.
     *
     * @return Liste des cartes Destination.
     */
    public List<CarteDestination> getEmplacementCarteDest() {
        return emplacementCarteDest;
    }


    /**
     * Rafraîchit l'emplacement d'un Viking spécifique.
     *
     * @param couleur Couleur du Viking à rafraîchir.
     */
    public void rafraichirVikingEmplacement(VikingCouleur couleur) {
        CarteViking ancienneCarte = emplacementViking.get(couleur);
        if (ancienneCarte != null) {
            jeu.ajouterCarteVikingDefausse(ancienneCarte);
        }
    
        CarteViking nouvelleCarte = tirerCarteViking();
        if (nouvelleCarte != null) {
            emplacementViking.put(couleur, nouvelleCarte);
            if (isClone) {
                //System.out.println("[CLONE] L'emplacement " + couleur + " a été rafraîchi avec une nouvelle carte: " + nouvelleCarte + ".\n\n");
            } else {
                System.out.println("L'emplacement " + couleur + " a été rafraîchi avec une nouvelle carte: " + nouvelleCarte + ".\n\n");
            }
        } else {
            emplacementViking.remove(couleur);
            if (isClone) {
                //System.out.println("[CLONE] Pioche et défausse vides.");
            } else {
                System.out.println("Pioche et défausse vides.");
            }
        }
    }

    /**
     * Rafraîchit une carte Destination spécifique.
     *
     * @param destination Carte Destination à rafraîchir.
     */
    public void rafraichirCarteDestination(CarteDestination destination) {
        if (destination == null) {
            System.out.println("Destination null, impossible de rafraîchir.");
            return;
        }
        int index = emplacementCarteDest.indexOf(destination);
        if (index != -1) {
            emplacementCarteDest.remove(index);
            if (isClone){
                //System.out.println("[CLONE] L'emplacement de la carte " + destination + " a été supprimé.");
            } else {
                System.out.println("L'emplacement de la carte " + destination + " a été supprimé.");
            }
            CarteDestination nouvelleDestination = tirerCarteDestination();
            if (nouvelleDestination != null) {
                emplacementCarteDest.add(index, nouvelleDestination);
                if (isClone) {
                    //System.out.println("[CLONE] Une nouvelle carte Destination a été ajoutée à l'emplacement " + index + ".");
                } else {
                    System.out.println("Une nouvelle carte Destination a été ajoutée à l'emplacement " + index + ".");
                }
            } else {
                if (isClone) {
                    //System.out.println("[CLONE] Aucune nouvelle carte Destination n'a été tirée.");
                } else {
                    System.out.println("Aucune nouvelle carte Destination n'a été tirée.");
                }
            }
        } else {
            if (isClone) {
                //System.out.println("[CLONE] La destination spécifiée n'est pas sur le plateau.");
            } else {
                System.out.println("La destination spécifiée n'est pas sur le plateau.");
            }
        }
    }

    /**
     * Tire une carte Viking du deck (implémenter selon votre logique de deck).
     *
     * @return Nouvelle carte Viking ou null si le deck est vide.
     */
    private CarteViking tirerCarteViking() {
        jeu.gererPiocheViking();
        Deck<CarteViking> vikingDeck = jeu.getVikingDeck(); // Get the deck from Jeu
        if (!vikingDeck.estVide()) {
            return vikingDeck.tirer(); 
        } else {
            System.out.println("ERREUR : La pioche de cartes Viking est vide dans la pioche et défausse.");
            return null; 
        }
    }

    /**
     * Tire une carte Destination du deck (implémenter selon votre logique de deck).
     *
     * @return Nouvelle carte Destination ou null si le deck est vide.
     */
    private CarteDestination tirerCarteDestination() {
        if (destinationDeck != null && !destinationDeck.estVide()) {
            return destinationDeck.tirer();
        }
        return null;
    }

    /**
     * Initialise les emplacements du plateau avec les decks de cartes.
     *
     * @param vikingDeck        Deck des cartes Viking.
     * @param cartesDestination Deck des cartes Destination.
     */
    public void initialiserEmplacements(Deck<CarteViking> vikingDeck, Deck<CarteDestination> cartesDestination) {
        this.destinationDeck = cartesDestination; 
    
        for (VikingCouleur couleur : VikingCouleur.values()) {
            if (couleur != VikingCouleur.COULEURANY && couleur != VikingCouleur.COULEURDIFF) {

                CarteViking carte = vikingDeck.tirer();
                if (carte != null) {
                    emplacementViking.put(couleur, carte);
                } else {
                    if (isClone) {
                        //System.out.println("[CLONE] Pas assez de cartes Viking pour la couleur: " + couleur);
                    } else {
                        System.out.println("Pas assez de cartes Viking pour la couleur: " + couleur);
                    }
                }
            }
        }

        int maxDestinations = 6;
        while (!cartesDestination.estVide() && emplacementCarteDest.size() < maxDestinations) {
            CarteDestination carte = cartesDestination.tirer();
            if (carte != null) {
                emplacementCarteDest.add(carte);
            }
        }
    }
}