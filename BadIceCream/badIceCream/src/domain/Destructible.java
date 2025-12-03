package domain;

/**
 * Interfaz para objetos que pueden ser destruidos y reparados.
 */
public interface Destructible {
    /**
     * Verifica si el objeto está destruido.
     * 
     * @return true si el objeto está destruido.
     */
    boolean isDestroyed();

    /**
     * Destruye el objeto.
     */
    void destroy();

    /**
     * Repara el objeto, restaurándolo a su estado original.
     */
    void repair();
}