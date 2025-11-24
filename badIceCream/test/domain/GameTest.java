package domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGameCreation() {
        Game game = new Game();
        assertNotNull("Game should not be null", game);
        assertEquals("Initial state should be MENU", GameState.MENU, game.getState());
    }
    
    @Test
    public void testStartGame() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        
        assertEquals("State should be PLAYING after start", GameState.PLAYING, game.getState());
        assertNotNull("Level should be created", game.getLevel());
        assertNotNull("Player should be created", game.getPlayer());
    }
    
    @Test
    public void testPauseResumeAfterStart() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        
        // Solo funciona después de empezar el juego
        game.pause();
        assertEquals("Game should be paused", GameState.PAUSED, game.getState());
        
        game.resume();
        assertEquals("Game should be playing", GameState.PLAYING, game.getState());
    }
    
    @Test
    public void testPlayerMovementAfterStart() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Player player = game.getPlayer();
        Position initialPos = player.getPosition();
        
        game.movePlayer(Direction.RIGHT);
        Position newPos = player.getPosition();
        
        assertNotEquals("Player should move from initial position", initialPos, newPos);
    }
    
    @Test
    public void testTimeRemaining() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        
        int time = game.getTimeRemaining();
        assertTrue("Time should be positive: " + time, time > 0);
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