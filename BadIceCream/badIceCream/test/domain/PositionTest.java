package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testPositionCreation() {
        Position pos = new Position(3, 7);

        assertEquals(3, pos.getX(), "X coordinate should match");
        assertEquals(7, pos.getY(), "Y coordinate should match");
    }

    @Test
    public void testPositionEquality() {
        Position pos1 = new Position(2, 4);
        Position pos2 = new Position(2, 4);
        Position pos3 = new Position(2, 5);

        assertTrue(pos1.equals(pos2), "Equal positions should be equal");
        assertFalse(pos1.equals(pos3), "Different positions should not be equal");
    }

    @Test
    public void testPositionMovement() {
        Position start = new Position(5, 5);

        Position right = start.add(Direction.RIGHT);
        assertEquals(6, right.getX(), "Moving right should increase X");
        assertEquals(5, right.getY(), "Moving right should not change Y");

        Position down = start.add(Direction.DOWN);
        assertEquals(5, down.getX(), "Moving down should not change X");
        assertEquals(6, down.getY(), "Moving down should increase Y");

        Position left = start.add(Direction.LEFT);
        assertEquals(4, left.getX(), "Moving left should decrease X");
        assertEquals(5, left.getY(), "Moving left should not change Y");

        Position up = start.add(Direction.UP);
        assertEquals(5, up.getX(), "Moving up should not change X");
        assertEquals(4, up.getY(), "Moving up should decrease Y");
    }

    @Test
    public void testPositionCloning() {
        Position original = new Position(3, 4);
        Position clone = original.clone();

        assertEquals(original.getX(), clone.getX(), "Clone should have same X");
        assertEquals(original.getY(), clone.getY(), "Clone should have same Y");
        assertNotSame(original, clone, "Clone should be different object");
    }

    @Test
    public void testPositionStringRepresentation() {
        Position pos = new Position(2, 3);
        String str = pos.toString();

        assertTrue(str.contains("2") && str.contains("3"), "String should contain coordinates");
    }
}