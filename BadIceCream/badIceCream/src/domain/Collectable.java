package domain;

/**
 * Interface para objetos colectables
 */
public interface Collectable {
    boolean isCollected();
    void collect();
    int getPoints();
}