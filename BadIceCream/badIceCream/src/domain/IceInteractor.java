package domain;

/**
 * Interfaz para entidades que pueden interactuar con bloques de hielo (crear o
 * destruir).
 */
public interface IceInteractor {
    /**
     * Crea bloques de hielo en el nivel.
     * 
     * @param level El nivel donde se crearán los bloques.
     */
    void createIceBlock(Level level);

    /**
     * Destruye bloques de hielo en el nivel.
     * 
     * @param level El nivel donde se destruirán los bloques.
     */
    void destroyIceBlock(Level level);

    /**
     * Indica si la entidad tiene la capacidad de romper hielo.
     * 
     * @return true si puede romper hielo.
     */
    boolean canBreakIce();
}