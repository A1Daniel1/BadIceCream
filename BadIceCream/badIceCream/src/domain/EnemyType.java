package domain;

/**
 * Enumeración que define los tipos de enemigos disponibles en el juego.
 * Cada tipo tiene propiedades específicas como nombre y habilidades.
 */
public enum EnemyType {
    TROLL("Troll", false, false),
    POT("Maceta", true, false),
    ORANGE_SQUID("Calamar Naranja", true, true);

    private final String name;
    private final boolean canChase;
    private final boolean canBreakBlocks;

    /**
     * Constructor de la enumeración EnemyType.
     * 
     * @param name           Nombre legible del enemigo.
     * @param canChase       Indica si puede perseguir al jugador.
     * @param canBreakBlocks Indica si puede romper bloques de hielo.
     */
    EnemyType(String name, boolean canChase, boolean canBreakBlocks) {
        this.name = name;
        this.canChase = canChase;
        this.canBreakBlocks = canBreakBlocks;
    }

    /**
     * Obtiene el nombre del tipo de enemigo.
     * 
     * @return El nombre.
     */
    public String getName() {
        return name;
    }

    /**
     * Indica si este tipo de enemigo puede perseguir al jugador.
     * 
     * @return true si puede perseguir.
     */
    public boolean canChase() {
        return canChase;
    }

    /**
     * Indica si este tipo de enemigo puede romper bloques.
     * 
     * @return true si puede romper bloques.
     */
    public boolean canBreakBlocks() {
        return canBreakBlocks;
    }
}