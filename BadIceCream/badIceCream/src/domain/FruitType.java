package domain;

public enum FruitType {
    GRAPE("Uva", 50, false),           // 50 puntos
    BANANA("Plátano", 100, false),     // 100 puntos
    PINEAPPLE("Piña", 150, true),
    CHERRY("Cereza", 200, true);
    
    private final String name;
    private final int points;
    private final boolean movable;
    
    FruitType(String name, int points, boolean movable) {
        this.name = name;
        this.points = points;
        this.movable = movable;
    }
    
    public String getName() { return name; }
    public int getPoints() { return points; }
    public boolean isMovable() { return movable; }
}