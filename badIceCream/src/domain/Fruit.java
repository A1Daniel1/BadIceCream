package domain;

/**
 * Clase abstracta para frutas
 */
public abstract class Fruit extends GameObject implements Collectable {
    protected FruitType fruitType;
    protected boolean collected;
    
    public Fruit(Position position, FruitType fruitType) {
        super(position);
        this.fruitType = fruitType;
        this.collected = false;
        // Regenerar el ID después de asignar el fruitType
        this.id = generateId();
    }
    
    @Override
    public boolean isCollected() { return collected; }
    
    @Override
    public void collect() { this.collected = true; }
    
    @Override
    public int getPoints() { 
        return fruitType != null ? fruitType.getPoints() : 0; 
    }
    
    public FruitType getFruitType() { return fruitType; }
    
    @Override
    protected String generateId() {
        if (fruitType == null) {
            return "FRUIT_TEMP_" + System.nanoTime();
        }
        return "FRUIT_" + fruitType.name() + "_" + System.nanoTime();
    }
    
    @Override
    public String getType() { return "Fruit"; }
    
    public abstract void update(Level level);
}