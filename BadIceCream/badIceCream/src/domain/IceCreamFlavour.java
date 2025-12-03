package domain;

/**
 * Enumeración que define los sabores de helado disponibles para el jugador.
 * Cada sabor tiene un nombre y un color asociado.
 */
public enum IceCreamFlavour {
    VANILLA("Vainilla", "#FFF8DC"),
    STRAWBERRY("Fresa", "#FFB6C1"),
    CHOCOLATE("Chocolate", "#8B4513");

    private final String name;
    private final String color;

    /**
     * Constructor de la enumeración IceCreamFlavour.
     * 
     * @param name  Nombre del sabor.
     * @param color Código de color hexadecimal.
     */
    IceCreamFlavour(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Obtiene el nombre del sabor.
     * 
     * @return El nombre.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el código de color del sabor.
     * 
     * @return El color en formato hexadecimal.
     */
    public String getColor() {
        return color;
    }
}
