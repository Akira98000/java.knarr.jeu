package miage.knarr.equipeC.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VikingCouleur {
    ROUGE,
    JAUNE,
    VERT,
    BLEU,
    VIOLET,
    COULEURANY,
    COULEURDIFF;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static VikingCouleur forValue(String value) {
        return VikingCouleur.valueOf(value.toUpperCase());
    }
}