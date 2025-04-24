// src/main/java/miage/knarr/equipeC/model/Gain.java

package miage.knarr.equipeC.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import miage.knarr.equipeC.model.enums.TypeGain;

/**
 * Classe représentant un gain pour un Viking.
 */
public class Gain {
    private TypeGain type;
    private int valeur;

    public Gain() {
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param type   Type de gain.
     * @param valeur Valeur du gain.
     */
    @JsonCreator
    public Gain(@JsonProperty("type") TypeGain type,
                @JsonProperty("valeur") int valeur) {
        this.type = type;
        this.valeur = valeur;
    }

    // Getters et Setters

    public TypeGain getType() {
        return type;
    }

    public void setType(TypeGain type) {
        this.type = type;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return type + ":" + valeur;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Gain other)) return false;
        return this.type == other.type && this.valeur == other.valeur;
    }

    @Override
    public int hashCode() {
        return type.hashCode() * 31 + valeur;
    }
}