package miage.knarr.equipeC.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import miage.knarr.equipeC.model.CarteDestination;
import miage.knarr.equipeC.model.CarteViking;

public class CartesKnarr {
    private final List<CarteViking> cartesViking;
    private final List<CarteDestination> cartesDestination;

    @JsonCreator
    public CartesKnarr(
        @JsonProperty("cartesViking") List<CarteViking> cartesViking,
        @JsonProperty("cartesDestination") List<CarteDestination> cartesDestination
    ) {
        this.cartesViking = cartesViking;
        this.cartesDestination = cartesDestination;
    }

    public List<CarteViking> getCartesViking() {
        return cartesViking;
    }

    public List<CarteDestination> getCartesDestination() {
        return cartesDestination;
    }

}