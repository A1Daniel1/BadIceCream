package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpecificEnemyTest {

    @Test
    public void testTrollBehavior() {
        Troll troll = new Troll(new Position(1, 1));
        assertEquals(EnemyType.TROLL, troll.getEnemyType());
        assertFalse(troll.canChasePlayer());
        assertFalse(troll.canBreakBlocks());

        // Test patrolling logic requires a Level context
        Level level = new Level(1);
        // Clear walls to allow movement
        level.getWalls().clear();

        Position initialPos = troll.getPosition();
        troll.updateBehavior(level, null); // Player not needed for Troll patrol

        // Troll has a MOVE_DELAY of 10, so we need to update multiple times
        for (int i = 0; i < 15; i++) {
            troll.updateBehavior(level, null);
        }

        // Troll should have moved
        assertNotEquals(initialPos, troll.getPosition());
    }
}
