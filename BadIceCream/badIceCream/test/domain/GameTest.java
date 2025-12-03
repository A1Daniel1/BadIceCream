package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Game.
 * Verifica la creación del juego, transiciones de estado y lógica principal.
 */
public class GameTest {

    /**
     * Verifica que el juego se cree correctamente en el estado inicial MENU.
     */
    @Test
    public void testGameCreation() {
        Game game = new Game();
        assertNotNull(game, "El juego no debería ser null");
        assertEquals(GameState.MENU, game.getState(), "El estado inicial debería ser MENU");
    }

    /**
     * Verifica el inicio del juego, cambio de estado a PLAYING y creación de
     * componentes.
     */
    @Test
    public void testStartGame() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        assertEquals(GameState.PLAYING, game.getState(), "El estado debería ser PLAYING después de iniciar");
        assertNotNull(game.getLevel(), "El nivel debería haber sido creado");
        assertNotNull(game.getPlayer(), "El jugador debería haber sido creado");
    }

    /**
     * Verifica la funcionalidad de pausar y reanudar el juego.
     */
    @Test
    public void testPauseResumeAfterStart() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        // Solo funciona después de empezar el juego
        game.pause();
        assertEquals(GameState.PAUSED, game.getState(), "El juego debería estar pausado");

        game.resume();
        assertEquals(GameState.PLAYING, game.getState(), "El juego debería estar en ejecución");
    }

    /**
     * Verifica que el jugador se mueva correctamente después de iniciar el juego.
     */
    @Test
    public void testPlayerMovementAfterStart() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Player player = game.getPlayer();
        Position initialPos = player.getPosition();

        game.movePlayer(Direction.RIGHT);
        Position newPos = player.getPosition();

        assertNotEquals(initialPos, newPos, "El jugador debería moverse de la posición inicial");
    }

    /**
     * Verifica que el tiempo restante sea positivo al iniciar.
     */
    @Test
    public void testTimeRemaining() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        int time = game.getTimeRemaining();
        assertTrue(time > 0, "El tiempo debería ser positivo: " + time);
    }

    /**
     * Verifica las transiciones de estado del juego.
     */
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