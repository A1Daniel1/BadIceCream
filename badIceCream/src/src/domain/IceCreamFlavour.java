package domain;

public enum IceCreamFlavour {
    VANILLA("Vainilla", "#FFF8DC"),
    STRAWBERRY("Fresa", "#FFB6C1"),
    CHOCOLATE("Chocolate", "#8B4513");
    
    private final String name;
    private final String color;
    
    IceCreamFlavour(String name, String color) {
        this.name = name;
        this.color = color;
    }
    
    public String getName() { return name; }
    public String getColor() { return color; }
}
