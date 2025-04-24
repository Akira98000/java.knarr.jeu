package miage.knarr.equipeC.model.interfce;

import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.model.CarteDestination;
import miage.knarr.equipeC.model.CarteViking;
import miage.knarr.equipeC.model.Deck;
import miage.knarr.equipeC.model.Joueur;
import miage.knarr.equipeC.model.Plateau;
import miage.knarr.equipeC.model.Score;
import miage.knarr.equipeC.model.enums.Difficulte;

/**
 * Interface représentant les actions principales et les états du jeu.
 */
public interface JeuInterface {
    List<Joueur> getJoueurs();
    Difficulte getDifficulte();
    Map<Joueur, Score> getScoresPublics();
    Plateau getPlateau();
    Deck<CarteViking> getVikingDeck();
    Deck<CarteDestination> getCarteDestination();
    List<CarteViking> getVikingDefausse();

    void mettreAJourScores(Joueur joueur, int pointsVictoire, int pointsRenommee);
    void ajouterCarteVikingDefausse(CarteViking carte);
    void initialiserDecks();
    void setCartesDestination(List<CarteDestination> cartesDestination);
    void initialiserPlateau();
}
