package domain;

/**
 * Representa un bloque de hielo en el juego.
 * Los bloques de hielo pueden ser creados y destruidos por el jugador y algunos
 * enemigos.
 */
public class IceBlock extends GameObject implements Destructible {
    private boolean destroyed;

    /**
     * Constructor de la clase IceBlock.
     * 
     * @param position La posición del bloque de hielo.
     */
    public IceBlock(Position position) {
        super(position);
        this.destroyed = false;
        this.id = generateId();
    }

    /**
     * Verifica si el bloque de hielo está destruido.
     * 
     * @return true si está destruido.
     */
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Destruye el bloque de hielo.
     */
    @Override
    public void destroy() {
        this.destroyed = true;
    }

    /**
     * Repara el bloque de hielo (lo restaura).
     */
    @Override
    public void repair() {
        this.destroyed = false;
    }

    @Override
    protected String generateId() {
        return "ICE_" + System.nanoTime();
    }

    @Override
    public String getSymbol() {
        return "#";
    }

    @Override
    public String getType() {
        return "IceBlock";
    }
}
