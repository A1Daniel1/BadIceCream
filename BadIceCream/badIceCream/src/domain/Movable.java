package domain;

/**
 * Interfaz para entidades que pueden moverse en el tablero.
 */
public interface Movable {
    /**
     * Mueve la entidad a una nueva posición.
     * 
     * @param newPosition La nueva posición.
     */
    void move(Position newPosition);

    /**
     * Obtiene la posición actual de la entidad.
     * 
     * @return La posición actual.
     */
    Position getPosition();

    /**
     * Obtiene la dirección actual hacia la que mira la entidad.
     * 
     * @return La dirección actual.
     */
    Direction getDirection();

    /**
     * Establece la dirección hacia la que mira la entidad.
     * 
     * @param direction La nueva dirección.
     */
    void setDirection(Direction direction);

    /**
     * Verifica si la entidad puede moverse a una posición específica en el nivel
     * dado.
     * 
     * @param position La posición destino.
     * @param level    El nivel actual.
     * @return true si el movimiento es válido.
     */
    boolean canMoveTo(Position position, Level level);
}