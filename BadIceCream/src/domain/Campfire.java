package domain;

/**
 * Fogata - Mata al jugador pero no a los enemigos
 * Se apaga y prende automáticamente cada cierto tiempo
 */
public class Campfire extends GameObject {
    private boolean lit; // Encendida o apagada
    private long lastStateChange; // Última vez que cambió de estado
    private static final long STATE_CHANGE_INTERVAL = 5000; // 5 segundos encendida, 5 segundos apagada
    
    public Campfire(Position position) {
        super(position);
        this.lit = true; // Comienza encendida
        this.lastStateChange = System.currentTimeMillis();
    }
    
    public boolean isLit() {
        return lit;
    }
    
    public void extinguish() {
        this.lit = false;
        this.lastStateChange = System.currentTimeMillis();
    }
    
    public void relight() {
        this.lit = true;
        this.lastStateChange = System.currentTimeMillis();
    }
    
    /**
     * Actualiza el estado de la fogata - alterna entre encendida y apagada
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastStateChange;
        
        // Cambiar estado cada 5 segundos
        if (elapsed >= STATE_CHANGE_INTERVAL) {
            if (lit) {
                extinguish();
            } else {
                relight();
            }
        }
    }
    
    /**
     * Verifica si el fuego mata al jugador en esta posición
     */
    public boolean isDeadly() {
        return lit;
    }
    
    @Override
    protected String generateId() {
        if (position != null) {
            return "CAMPFIRE_" + position.getX() + "_" + position.getY();
        }
        return "CAMPFIRE_" + System.nanoTime();
    }
    
    @Override
    public String getSymbol() {
        return lit ? "F" : "f";
    }
    
    @Override
    public String getType() {
        return "Campfire";
    }
}