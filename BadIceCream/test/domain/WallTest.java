package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WallTest {

    @Test
    public void testWallCreation() {
        Position pos = new Position(1, 1);
        Wall wall = new Wall(pos);

        assertEquals(pos, wall.getPosition());
        assertEquals("Wall", wall.getType());
        assertEquals("█", wall.getSymbol());
    }
}
