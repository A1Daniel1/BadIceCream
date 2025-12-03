// Wall.java
package domain;

/**
 * Representa una pared en el juego.
 * Las paredes son obstáculos indestructibles que bloquean el movimiento.
 */
public class Wall extends GameObject {
    /**
     * Constructor de la clase Wall.
     * 
     * @param position La posición de la pared.
     */
    public Wall(Position position) {
        super(position);
        this.id = generateId();
    }

    @Override
    protected String generateId() {
        if (position != null) {
            return "WALL_" + position.getX() + "_" + position.getY();
        }
        return "WALL_" + System.nanoTime();
    }

    @Override
    public String getSymbol() {
        return "█";
    }

    @Override
    public String getType() {
        return "Wall";
    }
}