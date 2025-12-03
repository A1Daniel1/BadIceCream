package domain;

/**
 * Interfaz que define el comportamiento de los enemigos.
 */
public interface EnemyBehavior {
    /**
     * Calcula la siguiente posición del enemigo.
     * 
     * @param level  El nivel actual.
     * @param player El jugador objetivo.
     * @return La siguiente posición.
     */
    Position getNextPosition(Level level, Player player);

    /**
     * Indica si el enemigo puede perseguir al jugador.
     * 
     * @return true si puede perseguir.
     */
    boolean canChasePlayer();

    /**
     * Indica si el enemigo puede romper bloques de hielo.
     * 
     * @return true si puede romper bloques.
     */
    boolean canBreakBlocks();
}
