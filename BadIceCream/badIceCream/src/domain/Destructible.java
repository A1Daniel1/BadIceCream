package domain;

/**
 * Interface para objetos que pueden ser destruidos
 */
public interface Destructible {
    boolean isDestroyed();
    void destroy();
    void repair();
}