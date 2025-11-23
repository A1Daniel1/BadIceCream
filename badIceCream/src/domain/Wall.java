// Wall.java
package domain;

/**
 * Pared (obstáculo permanente)
 */
public class Wall extends GameObject {
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
    public String getSymbol() { return "█"; }
    
    @Override
    public String getType() { return "Wall"; }
}