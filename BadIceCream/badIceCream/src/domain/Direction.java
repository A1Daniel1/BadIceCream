package domain;

/**
 * Enumeración que representa las direcciones posibles de movimiento.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int offsetX;
    private final int offsetY;

    /**
     * Constructor de la enumeración Direction.
     * 
     * @param offsetX Desplazamiento en el eje X.
     * @param offsetY Desplazamiento en el eje Y.
     */
    Direction(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Obtiene el desplazamiento en X.
     * 
     * @return El valor de offsetX.
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Obtiene el desplazamiento en Y.
     * 
     * @return El valor de offsetY.
     */
    public int getOffsetY() {
        return offsetY;
    }
}
