package domain;

/**
 * Clase abstracta para todas las entidades del juego
 */
public abstract class GameObject {
    protected Position position;
    protected String id;
    
    public GameObject(Position position) {
        this.position = position;
        // El ID será generado por las subclases después de inicializar sus atributos
        this.id = "TEMP_" + System.nanoTime();
    }
    
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public String getId() { return id; }
    
    protected abstract String generateId();
    public abstract String getSymbol();
    public abstract String getType();
}