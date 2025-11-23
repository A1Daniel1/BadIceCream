package domain;

/**
 * Clase abstracta para enemigos
 */
public abstract class Enemy extends Unit implements EnemyBehavior {
    protected EnemyType enemyType;
    
    public Enemy(Position position, EnemyType enemyType) {
        super(position);
        this.enemyType = enemyType;
        // Regenerar el ID después de asignar el enemyType
        this.id = generateId();
    }
    
    public EnemyType getEnemyType() { return enemyType; }
    
    @Override
    public boolean canChasePlayer() {
        return enemyType != null && enemyType.canChase();
    }
    
    @Override
    public boolean canBreakBlocks() {
        return enemyType != null && enemyType.canBreakBlocks();
    }
    
    @Override
    protected String generateId() {
        if (enemyType == null) {
            return "ENEMY_TEMP_" + System.nanoTime();
        }
        return "ENEMY_" + enemyType.name() + "_" + System.nanoTime();
    }
    
    @Override
    public String getType() { return "Enemy"; }
    
    public abstract void updateBehavior(Level level, Player player);
}