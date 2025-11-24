package domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {
    
    @Test
    public void testPositionCreation() {
        Position pos = new Position(3, 7);
        
        assertEquals("X coordinate should match", 3, pos.getX());
        assertEquals("Y coordinate should match", 7, pos.getY());
    }
    
    @Test
    public void testPositionEquality() {
        Position pos1 = new Position(2, 4);
        Position pos2 = new Position(2, 4);
        Position pos3 = new Position(2, 5);
        
        assertTrue("Equal positions should be equal", pos1.equals(pos2));
        assertFalse("Different positions should not be equal", pos1.equals(pos3));
    }
    
    @Test
    public void testPositionMovement() {
        Position start = new Position(5, 5);
        
        Position right = start.add(Direction.RIGHT);
        assertEquals("Moving right should increase X", 6, right.getX());
        assertEquals("Moving right should not change Y", 5, right.getY());
        
        Position down = start.add(Direction.DOWN);
        assertEquals("Moving down should not change X", 5, down.getX());
        assertEquals("Moving down should increase Y", 6, down.getY());
        
        Position left = start.add(Direction.LEFT);
        assertEquals("Moving left should decrease X", 4, left.getX());
        assertEquals("Moving left should not change Y", 5, left.getY());
        
        Position up = start.add(Direction.UP);
        assertEquals("Moving up should not change X", 5, up.getX());
        assertEquals("Moving up should decrease Y", 4, up.getY());
    }
    
    @Test
    public void testPositionCloning() {
        Position original = new Position(3, 4);
        Position clone = original.clone();
        
        assertEquals("Clone should have same X", original.getX(), clone.getX());
        assertEquals("Clone should have same Y", original.getY(), clone.getY());
        assertNotSame("Clone should be different object", original, clone);
    }
    
    @Test
    public void testPositionStringRepresentation() {
        Position pos = new Position(2, 3);
        String str = pos.toString();
        
        assertTrue("String should contain coordinates", str.contains("2") && str.contains("3"));
    }
}