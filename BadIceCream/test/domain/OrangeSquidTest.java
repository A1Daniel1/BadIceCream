package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrangeSquidTest {

    @Test
    public void testOrangeSquidCreation() {
        Position pos = new Position(5, 5);
        OrangeSquid squid = new OrangeSquid(pos);

        assertEquals(pos, squid.getPosition());
        assertEquals(EnemyType.ORANGE_SQUID, squid.getEnemyType());
        assertEquals("S", squid.getSymbol());
    }

    @Test
    public void testMovementTowardsPlayer() {
        Level level = new Level(1);
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getEnemies().clear();

        OrangeSquid squid = new OrangeSquid(new Position(5, 5));
        level.getEnemies().add(squid);

        // Player to the right
        Player player = new Player(new Position(7, 5), IceCreamFlavour.VANILLA);

        // Simulate enough ticks for movement (MOVE_DELAY = 6)
        for (int i = 0; i < 10; i++) {
            squid.updateBehavior(level, player);
        }

        // Should have moved right
        assertTrue(squid.getPosition().getX() > 5);
    }

    @Test
    public void testIceBreakingBehavior() {
        Level level = new Level(1);
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getEnemies().clear();

        OrangeSquid squid = new OrangeSquid(new Position(5, 5));
        level.getEnemies().add(squid);

        // Ice block to the right
        Position icePos = new Position(6, 5);
        level.getIceBlocks().add(new IceBlock(icePos));

        // Player behind ice
        Player player = new Player(new Position(8, 5), IceCreamFlavour.VANILLA);

        // Simulate ticks. Squid should move to (5,5) -> detect ice at (6,5) -> start
        // breaking
        // It needs MOVE_DELAY to try to move, then BREAK_DELAY to break.

        // Initial state
        assertTrue(level.isIceBlock(icePos));

        // Run enough updates to trigger move attempt and break start
        for (int i = 0; i < 10; i++) {
            squid.updateBehavior(level, player);
        }

        // Should still be at (5,5) because it's breaking ice
        assertEquals(new Position(5, 5), squid.getPosition());

        // Run more updates to complete breaking (BREAK_DELAY = 15)
        for (int i = 0; i < 20; i++) {
            squid.updateBehavior(level, player);
        }

        // Ice should be gone
        assertFalse(level.isIceBlock(icePos));
    }
}
