// src/main/java/miage/knarr/equipeC/model/CoutExploration.java

package miage.knarr.equipeC.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import miage.knarr.equipeC.model.enums.VikingCouleur;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;




public class CoutExploration {
    @JsonAlias({"couleur", "requis"})
    private List<VikingCouleur> couleur;

    @JsonAlias({"nombres", "nombre"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Integer> nombres;

    @JsonCreator
    public CoutExploration(
            @JsonProperty("couleur") List<VikingCouleur> couleur,
            @JsonProperty("requis") List<VikingCouleur> requis,
            @JsonProperty("nombres") List<Integer> nombres,
            @JsonProperty("nombre") List<Integer> nombre) {

        // Handle 'couleur' and 'requis' fields
        this.couleur = Objects.requireNonNullElseGet(couleur, () -> Objects.requireNonNullElseGet(requis, List::of));

        // Handle 'nombres' and 'nombre' fields
        this.nombres = Objects.requireNonNullElseGet(nombres, () -> Objects.requireNonNullElseGet(nombre, List::of));
    }

    public List<VikingCouleur> getCouleur() {
        return couleur;
    }

    public void setCouleur(List<VikingCouleur> couleur) {
        this.couleur = couleur;
    }

    public List<Integer> getNombres() {
        return nombres;
    }

    @Override
    public String toString() {
        return "Couleur: " + couleur + ", Nombres: " + nombres;
    }

}