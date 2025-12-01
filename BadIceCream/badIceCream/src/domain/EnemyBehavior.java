package domain;

/**
 * Interface para comportamiento de enemigos
 */
public interface EnemyBehavior {
    Position getNextPosition(Level level, Player player);
    boolean canChasePlayer();
    boolean canBreakBlocks();
}
