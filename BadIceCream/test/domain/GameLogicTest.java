package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

    @Test
    public void testGameInitialization() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(180, game.getTimeRemaining());
        assertNotNull(game.getLevel());
        assertNotNull(game.getPlayer());
        assertEquals(GameMode.PLAYER, game.getGameMode());
    }

    @Test
    public void testUpdateLoopDecrementsTime() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        int initialTime = game.getTimeRemaining();

        // Simulate 1 second of updates (10 updates per second)
        for (int i = 0; i < 10; i++) {
            Game.update();
        }

        assertEquals(initialTime - 1, game.getTimeRemaining());
    }

    @Test
    public void testGameOverOnTimeExpiry() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);

        // Simulate time running out
        for (int i = 0; i < 180 * 10; i++) {
            Game.update();
        }

        assertEquals(GameState.GAME_OVER, game.getState());
    }

    @Test
    public void testPlayerDeathOnCampfireCollision() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Level level = game.getLevel();

        // Clear existing elements
        level.getWalls().clear();
        level.getEnemies().clear();
        level.getCampfires().clear();

        // Place deadly campfire at (2,1)
        Campfire fire = new Campfire(new Position(2, 1));
        level.getCampfires().add(fire);

        // Move player to campfire
        game.movePlayer(Direction.RIGHT);

        assertFalse(game.getPlayer().isAlive());
        assertEquals(GameState.GAME_OVER, game.getState());
    }

    @Test
    public void testVictoryCondition() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Level level = game.getLevel();

        // Clear everything
        level.getWalls().clear();
        level.getEnemies().clear();
        level.getFruits().clear();

        // Add one fruit at (2,1)
        Fruit fruit = new Banana(new Position(2, 1));
        level.getFruits().add(fruit);

        // Remove WaveManager to test simple victory condition
        level.setWaveManager(null);

        // Move player to fruit
        game.movePlayer(Direction.RIGHT);

        // Force update to check victory condition
        Game.update();

        assertTrue(fruit.isCollected());
        assertEquals(GameState.VICTORY, game.getState());
    }
}
