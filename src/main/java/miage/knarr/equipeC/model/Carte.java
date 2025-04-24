package miage.knarr.equipeC.model;

public class Carte implements Cloneable {
    private final int id;

    public Carte(int id) {
        this.id = id;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    @Override
    protected Carte clone() {
        try {
            return (Carte) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}