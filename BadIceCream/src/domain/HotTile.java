package domain;

/**
 * Baldosa caliente - Derrite bloques de hielo inmediatamente
 */
public class HotTile extends GameObject {
    
    public HotTile(Position position) {
        super(position);
    }
    
    /**
     * Verifica si derrite hielo en esta posición
     */
    public boolean meltsIce() {
        return true;
    }
    
    @Override
    protected String generateId() {
        if (position != null) {
            return "HOTTILE_" + position.getX() + "_" + position.getY();
        }
        return "HOTTILE_" + System.nanoTime();
    }
    
    @Override
    public String getSymbol() {
        return "H";
    }
    
    @Override
    public String getType() {
        return "HotTile";
    }
}