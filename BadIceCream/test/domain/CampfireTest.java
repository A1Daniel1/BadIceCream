package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CampfireTest {

    @Test
    public void testCampfireCreation() {
        Position pos = new Position(1, 1);
        Campfire fire = new Campfire(pos);

        assertEquals(pos, fire.getPosition());
        assertTrue(fire.isLit());
        assertTrue(fire.isDeadly());
        assertEquals("F", fire.getSymbol());
    }

    @Test
    public void testStateToggling() throws InterruptedException {
        Position pos = new Position(1, 1);
        Campfire fire = new Campfire(pos);

        // Manually trigger extinguish for testing logic (waiting 5s in unit test is
        // bad)
        fire.extinguish();
        assertFalse(fire.isLit());
        assertFalse(fire.isDeadly());
        assertEquals("f", fire.getSymbol());

        fire.relight();
        assertTrue(fire.isLit());
    }
}
