package domain;

/**
 * Interface for listening to game events.
 * Implements the Observer pattern to decouple Game logic from UI.
 */
public interface GameListener {
    /**
     * Called when the game state is updated (e.g., player moved, enemies moved).
     */
    void onGameUpdated();

    /**
     * Called when the game is over.
     */
    void onGameOver();

    /**
     * Called when the player wins.
     */
    void onVictory();
}
