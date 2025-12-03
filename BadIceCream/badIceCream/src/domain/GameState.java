package domain;

/**
 * Enumeración que define los posibles estados del juego.
 */
public enum GameState {
    /** Menú principal del juego. */
    MENU,
    /** El juego está en curso. */
    PLAYING,
    /** El juego está pausado. */
    PAUSED,
    /** El jugador ha perdido todas sus vidas. */
    GAME_OVER,
    /** El jugador ha completado el nivel. */
    VICTORY
}
