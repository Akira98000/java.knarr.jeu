package miage.knarr.equipeC.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import miage.knarr.equipeC.model.enums.VikingCouleur;


public class CarteViking extends Carte implements Clonable<CarteViking> {
    private int force;
    private VikingCouleur couleur;
    private Gain gain;

    @Override
    public CarteViking clone() {
        try {
            return (CarteViking) super.clone();
        } catch (Throwable e) {
            throw new AssertionError();
        }
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param force   Force du Viking.
     * @param couleur Couleur du Viking.
     * @param gain    Gain associé au Viking.
     */
    @JsonCreator
    public CarteViking(
            @JsonProperty("id") int id, 
            @JsonProperty("force") int force,
            @JsonProperty("couleur") VikingCouleur couleur,
            @JsonProperty("gain") Gain gain) {
        super(id); // Appel au constructeur de la classe parente Carte
        this.force = force;
        this.couleur = couleur;
        this.gain = gain;
    }

    // Getters et Setters

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public VikingCouleur getCouleur() {
        return couleur;
    }

    public void setCouleur(VikingCouleur couleur) {
        this.couleur = couleur;
    }

    public Gain getGain() {
        return gain;
    }

    public void setGain(Gain gain) {
        this.gain = gain;
    }

    @Override
    public String toString() {
        return "CarteViking{" +
                "id=" + getId() +
                ", force=" + force +
                ", couleur=" + couleur +
                ", gain=" + gain +
                '}';
    }
}