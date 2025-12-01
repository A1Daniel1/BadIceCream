package domain;

/**
 * Interface para entidades que tienen vida
 */
public interface Alive {
    boolean isAlive();
    void die();
    void revive();
}
