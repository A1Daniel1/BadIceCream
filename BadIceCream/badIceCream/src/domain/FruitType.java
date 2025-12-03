package domain;

/**
 * Enumeración que define los tipos de frutas disponibles en el juego.
 * Cada tipo tiene un nombre, un valor en puntos y una propiedad de movilidad.
 */
public enum FruitType {
    GRAPE("Uva", 50, false), // 50 puntos
    BANANA("Plátano", 100, false), // 100 puntos
    PINEAPPLE("Piña", 150, true),
    CHERRY("Cereza", 200, true);

    private final String name;
    private final int points;
    private final boolean movable;

    /**
     * Constructor de la enumeración FruitType.
     * 
     * @param name    Nombre de la fruta.
     * @param points  Puntos que otorga.
     * @param movable Indica si la fruta puede moverse.
     */
    FruitType(String name, int points, boolean movable) {
        this.name = name;
        this.points = points;
        this.movable = movable;
    }

    /**
     * Obtiene el nombre de la fruta.
     * 
     * @return El nombre.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene los puntos de la fruta.
     * 
     * @return Los puntos.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Indica si la fruta es móvil.
     * 
     * @return true si la fruta puede moverse.
     */
    public boolean isMovable() {
        return movable;
    }
}