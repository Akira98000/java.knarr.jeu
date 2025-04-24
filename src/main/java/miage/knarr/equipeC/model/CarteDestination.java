package miage.knarr.equipeC.model;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import miage.knarr.equipeC.model.enums.TypeGain;

public class CarteDestination extends Carte implements Clonable<CarteDestination> {
    private final List<CoutExploration> coutExploration;
    private HashMap<TypeGain, Integer> gain; // Changed to TypeGain
    private final HashMap<Integer, HashMap<TypeGain, Integer>> gainCommerce;


    @Override
    public CarteDestination clone() {
        try {
            return (CarteDestination) super.clone();
        } catch (Throwable e) {
            throw new AssertionError();
        }
    }

    /**
     * Constructor annotated with @JsonCreator to enable Jackson deserialization.
     *
     * @param id              ID of the CarteDestination.
     * @param coutExploration List of CoutExploration.
     * @param gain            Map of TypeGain to Integer.
     * @param gainCommerce    Nested Map for gainCommerce.
     */
    @JsonCreator
    public CarteDestination(
            @JsonProperty("id") int id,
            @JsonProperty("coutExploration") List<CoutExploration> coutExploration,
            @JsonProperty("gain") HashMap<TypeGain, Integer> gain,
            @JsonProperty("gainCommerce") HashMap<Integer, HashMap<TypeGain, Integer>> gainCommerce) {
        super(id);
        this.coutExploration = coutExploration != null ? coutExploration : List.of();
        this.gain = gain != null ? gain : new HashMap<>();
        this.gainCommerce = gainCommerce != null ? gainCommerce : new HashMap<>();
    }

    // Getters and Setters

    public List<CoutExploration> getCoutExploration() {
        return coutExploration;
    }

    public HashMap<TypeGain, Integer> getGain() {
        return gain;
    }

    public void setGain(HashMap<TypeGain, Integer> gain) {
        this.gain = gain;
    }

    public HashMap<Integer, HashMap<TypeGain, Integer>> getGainCommerce() {
        return gainCommerce;
    }

    /**
     * Additional method to access gains consistently.
     *
     * @return Map of gains.
     */
    public HashMap<TypeGain, Integer> getGains() {
        return gain;
    }

    /**
     * Returns a string representation of the CarteDestination.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        String gainsStr = gain.entrySet().stream()
                .map(entry -> entry.getKey().toString() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        String gainsCommerceStr = gainCommerce.entrySet().stream()
                .map(entry -> entry.getKey() + "={" +
                        entry.getValue().entrySet().stream()
                                .map(e -> e.getKey().toString() + ": " + e.getValue())
                                .collect(Collectors.joining(", ")) +
                        "}")
                .collect(Collectors.joining(", "));

        String coutStr = coutExploration.stream()
                .map(CoutExploration::toString)
                .collect(Collectors.joining("; "));

        return "CarteDestination{" +
                "id=" + getId() +
                ", coutExploration=[" + coutStr + "]" +
                ", gains=[" + gainsStr + "]" +
                ", gainCommerce={" + gainsCommerceStr + "}" +
                '}';
    }
}