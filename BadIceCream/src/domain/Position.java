package domain;

import java.io.Serializable;

/**
 * Representa una posición en el tablero de juego mediante coordenadas X e Y.
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 1L;
 
    private int x;
    private int y;

    /**
     * Constructor de la clase Position.
     * 
     * @param x Coordenada X.
     * @param y Coordenada Y.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene la coordenada X.
     * 
     * @return El valor de X.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la coordenada Y.
     * 
     * @return El valor de Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Establece la coordenada X.
     * 
     * @param x El nuevo valor de X.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Establece la coordenada Y.
     * 
     * @param y El nuevo valor de Y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Compara si esta posición es igual a otra.
     * 
     * @param other La otra posición a comparar.
     * @return true si ambas coordenadas son iguales.
     */
    public boolean equals(Position other) {
        if (other == null)
            return false;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Crea una copia de esta posición.
     * 
     * @return Una nueva instancia de Position con las mismas coordenadas.
     */
    public Position clone() {
        return new Position(this.x, this.y);
    }

    /**
     * Calcula una nueva posición sumando un desplazamiento basado en una dirección.
     * 
     * @param direction La dirección en la que moverse.
     * @return Una nueva posición desplazada.
     */
    public Position add(Direction direction) {
        return new Position(
                this.x + direction.getOffsetX(),
                this.y + direction.getOffsetY());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
}
