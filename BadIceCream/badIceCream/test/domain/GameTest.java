package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testGameCreation() {
        Game game = new Game();
        assertNotNull(game, "Game should not be null");
        assertEquals(GameState.MENU, game.getState(), "Initial state should be MENU");
    }

    @Test
    public void testStartGame() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        assertEquals(GameState.PLAYING, game.getState(), "State should be PLAYING after start");
        assertNotNull(game.getLevel(), "Level should be created");
        assertNotNull(game.getPlayer(), "Player should be created");
    }

    @Test
    public void testPauseResumeAfterStart() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        // Solo funciona después de empezar el juego
        game.pause();
        assertEquals(GameState.PAUSED, game.getState(), "Game should be paused");

        game.resume();
        assertEquals(GameState.PLAYING, game.getState(), "Game should be playing");
    }

    @Test
    public void testPlayerMovementAfterStart() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Player player = game.getPlayer();
        Position initialPos = player.getPosition();

        game.movePlayer(Direction.RIGHT);
        Position newPos = player.getPosition();

        assertNotEquals(initialPos, newPos, "Player should move from initial position");
    }

    @Test
    public void testTimeRemaining() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        int time = game.getTimeRemaining();
        assertTrue(time > 0, "Time should be positive: " + time);
    }

    @Test
    public void testGameStateTransitions() {
        Game game = new Game();

        // Estado inicial
        assertEquals(GameState.MENU, game.getState());

        // Al empezar juego
        game.startGame(1, IceCreamFlavour.VANILLA);
        assertEquals(GameState.PLAYING, game.getState());
    }
}