package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerCreation() {
        Position startPos = new Position(5, 5);
        Player player = new Player(startPos, IceCreamFlavour.VANILLA);

        assertNotNull(player, "Player should not be null");
        assertEquals(startPos, player.getPosition(), "Position should match");
        assertEquals(IceCreamFlavour.VANILLA, player.getFlavor(), "Flavor should be VANILLA");
        assertEquals(0, player.getScore(), "Initial score should be 0");
    }

    @Test
    public void testPlayerScore() {
        Player player = new Player(new Position(5, 5), IceCreamFlavour.VANILLA);

        player.addScore(50);
        assertEquals(50, player.getScore(), "Score should be 50");

        player.addScore(25);
        assertEquals(75, player.getScore(), "Score should be 75");
    }

    @Test
    public void testIceBlockLineCreation() {
        Level level = new Level(1);
        Player player = new Player(new Position(1, 1), IceCreamFlavour.VANILLA);
        player.setDirection(Direction.RIGHT);

        // Level 1 has a Troll at (13, 1). Row 1 is otherwise clear of walls between 1
        // and 13.
        // Player is at (1, 1). Ice should be created from (2, 1) to (12, 1).

        player.createIceBlock(level);

        // Check a few positions
        assertTrue(level.isIceBlock(new Position(2, 1)), "Should have ice at (2,1)");
        assertTrue(level.isIceBlock(new Position(5, 1)), "Should have ice at (5,1)");
        assertTrue(level.isIceBlock(new Position(12, 1)), "Should have ice at (12,1)");

        // Check that it stopped at the enemy (Troll at 13,1)
        assertFalse(level.isIceBlock(new Position(13, 1)), "Should NOT have ice at enemy position (13,1)");

        // Test Destruction
        player.destroyIceBlock(level);
        assertFalse(level.isIceBlock(new Position(2, 1)), "Ice at (2,1) should be destroyed");
        assertFalse(level.isIceBlock(new Position(12, 1)), "Ice at (12,1) should be destroyed");
    }
}