package domain;

/**
 * Clase abstracta que representa a un enemigo en el juego.
 * Define el comportamiento básico y las propiedades comunes de todos los
 * enemigos.
 */
public abstract class Enemy extends Unit implements EnemyBehavior {
    protected EnemyType enemyType;

    /**
     * Constructor de la clase Enemy.
     * 
     * @param position  La posición inicial del enemigo.
     * @param enemyType El tipo de enemigo.
     */
    public Enemy(Position position, EnemyType enemyType) {
        super(position);
        this.enemyType = enemyType;
        this.id = generateId();
    }

    /**
     * Obtiene el tipo de enemigo.
     * 
     * @return El tipo de enemigo.
     */
    public EnemyType getEnemyType() {
        return enemyType;
    }

    /**
     * Indica si el enemigo puede perseguir al jugador.
     * 
     * @return true si el tipo de enemigo tiene la capacidad de perseguir.
     */
    @Override
    public boolean canChasePlayer() {
        return enemyType.canChase();
    }

    /**
     * Indica si el enemigo puede romper bloques de hielo.
     * 
     * @return true si el tipo de enemigo tiene la capacidad de romper bloques.
     */
    @Override
    public boolean canBreakBlocks() {
        return enemyType.canBreakBlocks();
    }

    @Override
    protected String generateId() {
        if (enemyType == null) {
            return "ENEMY_TEMP_" + System.nanoTime();
        }
        return "ENEMY_" + enemyType.name() + "_" + System.nanoTime();
    }

    @Override
    public String getType() {
        return "Enemy";
    }

    /**
     * Actualiza el comportamiento del enemigo en el nivel.
     * 
     * @param level  El nivel actual.
     * @param player El jugador.
     */
    public abstract void updateBehavior(Level level, Player player);
}