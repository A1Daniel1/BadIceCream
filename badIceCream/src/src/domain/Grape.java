package domain;

public class Grape extends Fruit {
    public Grape(Position position) {
        super(position, FruitType.GRAPE);
    }
    
    @Override
    public void update(Level level) {
        // Las uvas no se mueven
    }
    
    @Override
    public String getSymbol() { return "G"; }
}
