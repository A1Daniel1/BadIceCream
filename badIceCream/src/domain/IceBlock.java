// IceBlock.java
package domain;

/**
 * Bloque de hielo
 */
public class IceBlock extends GameObject implements Destructible {
    private boolean destroyed;
    
    public IceBlock(Position position) {
        super(position);
        this.destroyed = false;
        this.id = generateId();
    }
    
    @Override
    public boolean isDestroyed() { return destroyed; }
    
    @Override
    public void destroy() { this.destroyed = true; }
    
    @Override
    public void repair() { this.destroyed = false; }
    
    @Override
    protected String generateId() {
        return "ICE_" + System.nanoTime();
    }
    
    @Override
    public String getSymbol() { return "#"; }
    
    @Override
    public String getType() { return "IceBlock"; }
}
