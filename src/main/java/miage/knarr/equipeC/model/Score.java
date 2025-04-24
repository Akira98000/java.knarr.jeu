package miage.knarr.equipeC.model;

/**
 * Classe représentant le score d'un joueur, incluant les points de victoire et de renommée.
 */
public class Score {
    private int pointsVictoire;
    private int pointsRenommee;

    /**
     * Constructeur de la classe Score.
     *
     * @param pointsVictoire Points de victoire initiaux.
     * @param pointsRenommee Points de renommée initiaux.
     */
    public Score(int pointsVictoire, int pointsRenommee) {
        this.pointsVictoire = pointsVictoire;
        this.pointsRenommee = pointsRenommee;
    }

    /**
     * Obtient les points de victoire.
     *
     * @return Points de victoire.
     */
    public int getPointsVictoire() {
        return pointsVictoire;
    }

    /**
     * Obtient les points de renommée.
     *
     * @return Points de renommée.
     */
    public int getPointsRenommee() {
        return pointsRenommee;
    }

    /**
     * Définit les points de victoire.
     * Méthode package-private pour sécuriser l'accès.
     *
     * @param pointsVictoire Nouveaux points de victoire.
     */
    void setPointsVictoire(int pointsVictoire) {
        this.pointsVictoire = pointsVictoire;
    }

    /**
     * Définit les points de renommée.
     * Méthode package-private pour sécuriser l'accès.
     *
     * @param pointsRenommee Nouveaux points de renommée.
     */
    void setPointsRenommee(int pointsRenommee) {
        this.pointsRenommee = pointsRenommee;
    }
}