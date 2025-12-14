package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NarwhalTest {

    @Test
    public void testNarwhalCreation() {
        Position pos = new Position(3, 3);
        Narwhal narwhal = new Narwhal(pos);

        assertEquals(pos, narwhal.getPosition());
        // Narwhal currently uses POT type in constructor as per code view, checking
        // that behavior or fixing it?
        // Code said: super(position, EnemyType.POT); // Usamos POT temporalmente
        // So we expect POT type for now.
        assertEquals(EnemyType.POT, narwhal.getEnemyType());
        assertFalse(narwhal.getSymbol().contains("!")); // Not charging initially
    }

    @Test
    public void testChargeInitiation() {
        Level level = new Level(1);
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getEnemies().clear();

        Narwhal narwhal = new Narwhal(new Position(5, 5));
        level.getEnemies().add(narwhal);

        // Player aligned horizontally
        Player player = new Player(new Position(9, 5), IceCreamFlavour.VANILLA);

        // Update behavior enough times to trigger move (MOVE_DELAY = 8)
        for (int i = 0; i < 10; i++) {
            narwhal.updateBehavior(level, player);
        }

        // Should be charging now
        assertEquals("N!", narwhal.getSymbol());
    }

    @Test
    public void testIceBreakingWhileCharging() {
        Level level = new Level(1);
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getEnemies().clear();

        Narwhal narwhal = new Narwhal(new Position(5, 5));
        level.getEnemies().add(narwhal);

        // Ice block in path
        Position icePos = new Position(6, 5);
        level.getIceBlocks().add(new IceBlock(icePos));

        // Player behind ice
        Player player = new Player(new Position(9, 5), IceCreamFlavour.VANILLA);

        // Trigger charge (needs to pass MOVE_DELAY)
        for (int i = 0; i < 10; i++) {
            narwhal.updateBehavior(level, player);
        }
        assertTrue(narwhal.getSymbol().equals("N!"));

        // Move ticks (CHARGE_SPEED = 2)
        // Needs multiple updates to move
        for (int i = 0; i < 5; i++) {
            narwhal.updateBehavior(level, player);
        }

        // Should have destroyed ice
        assertFalse(level.isIceBlock(icePos));
    }
}
