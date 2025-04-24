package miage.knarr.equipeC.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe générique représentant un deck de cartes.
 * @param <T> Le type de carte contenu dans le deck.
 */
public class Deck<T extends Clonable<T>> implements Clonable<Deck<T>>  {
    private List<T> cartes;

    @Override
    public Deck<T> clone() {
        try {
            Deck<T> cloned = (Deck<T>) super.clone();
            cloned.cartes = new ArrayList<>();
            for (T carte : this.cartes) {
                cloned.cartes.add(carte.clone());
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Constructeur par défaut qui initialise un deck vide.
     */
    public Deck() {
        this.cartes = new ArrayList<>();
    }

    /**
     * Constructeur qui initialise le deck avec une liste de cartes.
     * @param cartes La liste initiale de cartes dans le deck.
     */
    public Deck(List<T> cartes) {
        this.cartes = new ArrayList<>(cartes);
    }

    /**
     * Définit les cartes du deck.
     * @param cartes La liste de cartes à définir dans le deck.
     */
    public void setCartes(List<T> cartes) {
        this.cartes = new ArrayList<>(cartes);
    }

    /**
     * Piocher une carte du dessus du deck.
     * @return La carte piochée, ou null si le deck est vide.
     */
    public T piocher() {
        if (!cartes.isEmpty()) {
            return cartes.remove(0);
        }
        return null;
    }

    /**
     * Mélange les cartes du deck de manière aléatoire.
     */
    public void melanger() {
        Collections.shuffle(cartes);
    }

    /**
     * Tire la première carte du deck.
     * @return La carte tirée ou null si le deck est vide.
     */
    public T tirer() {
        if (!cartes.isEmpty()) {
            return cartes.remove(0);
        }
        return null;
    }

    /**
     * Vérifie si le deck est vide.
     * @return true si le deck est vide, false sinon.
     */
    public boolean estVide() {
        return cartes.isEmpty();
    }

    /**
     * Obtient le nombre de cartes restantes dans le deck.
     * @return Le nombre de cartes restantes.
     */
    public int getNombreCartes() {
        return cartes.size();
    }

    /**
     * Obtient une copie non modifiable des cartes du deck.
     * @return Liste non modifiable des cartes.
     */
    public List<T> getCartes() {
        return Collections.unmodifiableList(cartes);
    }

    /**
     * Ajoute une carte au deck.
     * @param carte La carte à ajouter.
     */
    public void ajouterCarte(T carte) {
        if (carte != null) {
            cartes.add(carte);
        }
    }

    /**
     * Retire une carte spécifique du deck.
     * @param carte La carte à retirer.
     * @return true si la carte a été retirée, false sinon.
     */
    public boolean retirerCarte(T carte) {
        return cartes.remove(carte);
    }
}