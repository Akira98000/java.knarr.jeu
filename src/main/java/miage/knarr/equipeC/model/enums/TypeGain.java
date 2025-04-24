package miage.knarr.equipeC.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeGain {
    POINT,
    RENOMME,
    RECRUE,
    CARTE_VIKING,
    BRACELET;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static TypeGain forValue(String value) {
        return TypeGain.valueOf(value.toUpperCase());
    }
}