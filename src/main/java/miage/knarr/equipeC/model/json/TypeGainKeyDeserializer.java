package miage.knarr.equipeC.model.json;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import miage.knarr.equipeC.model.enums.TypeGain;

import java.io.IOException;

public class TypeGainKeyDeserializer extends KeyDeserializer {
    @Override
    public TypeGain deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        // Split the key if it contains a colon (e.g., "RENOMME:1")
        String typeStr = key.split(":")[0];
        try {
            return TypeGain.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new IOException("Unknown TypeGain: " + typeStr, e);
        }
    }
}