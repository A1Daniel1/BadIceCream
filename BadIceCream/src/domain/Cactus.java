package domain;
import java.io.Serializable;
/**
 * Representa un cactus en el juego.
 * Es una fruta especial que alterna entre tener púas (peligroso) y no tenerlas (recolectable).
 * Cada 30 segundos cambia de estado.
 */
public class Cactus extends Fruit implements Serializable {
    private static final long serialVersionUID = 1L; 
    private boolean hasThorns; // Tiene púas actualmente
    private long lastStateChange; // Última vez que cambió de estado
    private static final long STATE_CHANGE_INTERVAL = 30000; // 30 segundos en milisegundos
    
    /**
     * Constructor de la clase Cactus.
     * 
     * @param position La posición inicial del cactus.
     */
    public Cactus(Position position) {
        super(position, FruitType.CACTUS);
        this.hasThorns = false; // Comienza sin púas
        this.lastStateChange = System.currentTimeMillis();
    }

    /**
     * Actualiza el estado del cactus.
     * Alterna entre tener púas y no tenerlas cada 30 segundos.
     * 
     * @param level El nivel actual.
     */
    @Override
    public void update(Level level) {
        if (collected) return;
        
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastStateChange;
        
        // Cambiar estado cada 30 segundos
        if (elapsed >= STATE_CHANGE_INTERVAL) {
            hasThorns = !hasThorns;
            lastStateChange = currentTime;
        }
        
        // Si tiene púas y el jugador está en la misma posición, matarlo
        if (hasThorns) {
            Player player = level.getPlayer();
            if (player != null && player.getPosition().equals(position)) {
                player.die();
            }
        }
    }
    
    /**
     * Verifica si el cactus tiene púas actualmente.
     * 
     * @return true si tiene púas, false si no.
     */
    public boolean hasThorns() {
        return hasThorns;
    }
    
    /**
     * Obtiene el tiempo restante hasta el próximo cambio de estado.
     * 
     * @return Milisegundos hasta el próximo cambio.
     */
    public long getTimeUntilStateChange() {
        long elapsed = System.currentTimeMillis() - lastStateChange;
        return Math.max(0, STATE_CHANGE_INTERVAL - elapsed);
    }
    
    /**
     * Establece si el cactus tiene púas (usado para deserialización).
     */
    public void setHasThorns(boolean hasThorns) {
        this.hasThorns = hasThorns;
    }
    
    /**
     * Establece el tiempo del último cambio de estado (usado para deserialización).
     */
    public void setLastStateChange(long lastStateChange) {
        this.lastStateChange = lastStateChange;
    }
    
    @Override
    public String getSymbol() {
        return hasThorns ? "C!" : "C";
    }
    
    @Override
    public void collect() {
        // Solo se puede recolectar si no tiene púas
        if (!hasThorns) {
            super.collect();
        }
    }
}