package domain;

/**
 * Interfaz para objetos que pueden ser recolectados por el jugador.
 */
public interface Collectable {
    /**
     * Verifica si el objeto ya ha sido recolectado.
     * 
     * @return true si el objeto ha sido recolectado.
     */
    boolean isCollected();

    /**
     * Marca el objeto como recolectado.
     */
    void collect();

    /**
     * Obtiene la cantidad de puntos que otorga este objeto.
     * 
     * @return Los puntos del objeto.
     */
    int getPoints();
}