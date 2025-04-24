package miage.knarr.equipeC.model;

public interface Clonable<T> extends Cloneable {
    T clone();
}