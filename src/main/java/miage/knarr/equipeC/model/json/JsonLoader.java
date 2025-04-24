package miage.knarr.equipeC.model.json;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import miage.knarr.equipeC.model.CarteDestination;
import miage.knarr.equipeC.model.CarteViking;
import miage.knarr.equipeC.model.enums.TypeGain;


public class JsonLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(TypeGain.class, new TypeGainKeyDeserializer());
        objectMapper.registerModule(module);

        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static List<CarteViking> chargerCartesViking(String chemin) throws Exception {
        InputStream inputStream = JsonLoader.class.getResourceAsStream("/" + chemin);
        if (inputStream == null) {
            throw new IllegalArgumentException("Le fichier JSON n'a pas été trouvé : " + chemin);
        }
        CartesKnarr cartesKnarr = objectMapper.readValue(inputStream, CartesKnarr.class);
        return cartesKnarr.getCartesViking();
    }

    public static List<CarteDestination> chargerCartesDestination(String chemin) throws Exception {
        InputStream inputStream = JsonLoader.class.getResourceAsStream("/" + chemin);
        if (inputStream == null) {
            throw new IllegalArgumentException("Le fichier JSON n'a pas été trouvé : " + chemin);
        }
        CartesKnarr cartesKnarr = objectMapper.readValue(inputStream, CartesKnarr.class);
        return cartesKnarr.getCartesDestination();
    }
}