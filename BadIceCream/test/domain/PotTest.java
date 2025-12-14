package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PotTest {

    @Test
    public void testPotCreation() {
        Position pos = new Position(2, 2);
        Pot pot = new Pot(pos);

        assertEquals(pos, pot.getPosition());
        assertEquals(EnemyType.POT, pot.getEnemyType());
        assertEquals("M", pot.getSymbol());
    }

    @Test
    public void testAxisPrioritization() {
        Level level = new Level(1);
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getEnemies().clear();

        Pot pot = new Pot(new Position(5, 5));
        level.getEnemies().add(pot);

        // Player far away in X, close in Y
        // DeltaX = 10 - 5 = 5
        // DeltaY = 6 - 5 = 1
        // Should prioritize X movement
        Player player = new Player(new Position(10, 6), IceCreamFlavour.VANILLA);

        // Simulate ticks (MOVE_DELAY = 8)
        for (int i = 0; i < 10; i++) {
            pot.updateBehavior(level, player);
        }

        // Should have moved RIGHT (X axis)
        assertEquals(new Position(6, 5), pot.getPosition());
    }

    @Test
    public void testBlockedMovement() {
        Level level = new Level(1);
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getEnemies().clear();

        Pot pot = new Pot(new Position(5, 5));
        level.getEnemies().add(pot);

        // Wall to the right
        level.getWalls().add(new Wall(new Position(6, 5)));

        // Player to the right
        Player player = new Player(new Position(8, 5), IceCreamFlavour.VANILLA);

        // Simulate ticks
        for (int i = 0; i < 10; i++) {
            pot.updateBehavior(level, player);
        }

        // Should NOT move right because of wall.
        // Might try Y if player is also offset in Y, but here only X diff.
        // If only X diff and blocked, stays put.
        assertEquals(new Position(5, 5), pot.getPosition());
    }
}
