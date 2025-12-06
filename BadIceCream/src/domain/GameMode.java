package domain;

/**
 * Enumeración que define las modalidades de juego disponibles.
 */
public enum GameMode {
    /** Un solo jugador contra la IA (enemigos del nivel) */
    PLAYER("Player", 1),
    
    /** Dos jugadores humanos compitiendo por puntos */
    PLAYER_VS_PLAYER("Player vs Player", 2),
    
    /** Un jugador humano contra una máquina (IA controlada) */
    PLAYER_VS_MACHINE("Player vs Machine", 2),
    
    /** Dos máquinas (IA) compitiendo entre sí */
    MACHINE_VS_MACHINE("Machine vs Machine", 2);

    private final String name;
    private final int playerCount;

    GameMode(String name, int playerCount) {
        this.name = name;
        this.playerCount = playerCount;
    }

    public String getName() {
        return name;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean isMultiplayer() {
        return playerCount > 1;
    }

    public boolean hasAI() {
        return this == PLAYER_VS_MACHINE || this == MACHINE_VS_MACHINE;
    }
}