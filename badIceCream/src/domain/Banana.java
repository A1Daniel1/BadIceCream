package domain;

public class Banana extends Fruit {
    public Banana(Position position) {
        super(position, FruitType.BANANA);
    }
    
    @Override
    public void update(Level level) {
        // Los plátanos no se mueven
    }
    
    @Override
    public String getSymbol() { return "B"; }
}