package domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class LevelTest {
    
    @Test
    public void testLevelCreation() {
        Level level = new Level(1);
        
        assertNotNull("Level should not be null", level);
        assertTrue("Width should be positive", level.getWidth() > 0);
        assertTrue("Height should be positive", level.getHeight() > 0);
    }
    
    @Test
    public void testLevelCollections() {
        Level level = new Level(1);
        
        assertNotNull("Walls should not be null", level.getWalls());
        assertNotNull("Ice blocks should not be null", level.getIceBlocks());
        assertNotNull("Fruits should not be null", level.getFruits());
        assertNotNull("Enemies should not be null", level.getEnemies());
        
        assertFalse("Level 1 should have walls", level.getWalls().isEmpty());
        assertFalse("Level 1 should have fruits", level.getFruits().isEmpty());
        assertFalse("Level 1 should have enemies", level.getEnemies().isEmpty());
    }
    
    @Test
    public void testMovementValidation() {
        Level level = new Level(1);
        
        // Test border positions (should be walls)
        Position borderPos = new Position(0, 0);
        assertFalse("Should not be able to move into border wall", level.canMoveTo(borderPos));
        
        // Test center position (should be movable)
        Position centerPos = new Position(7, 5);
        assertTrue("Should be able to move to center position", level.canMoveTo(centerPos));
    }
    
    @Test
    public void testFruitCollection() {
        Level level = new Level(1);
        
        // Get a fruit position
        Fruit firstFruit = level.getFruits().get(0);
        Position fruitPos = firstFruit.getPosition();
        
        // Verify fruit exists at that position
        Fruit foundFruit = level.getFruitAt(fruitPos);
        assertNotNull("Should find fruit at its position", foundFruit);
        assertEquals("Found fruit should be the same", firstFruit, foundFruit);
    }
    
    @Test
    public void testAllFruitsCollected() {
        Level level = new Level(1);
        
        // Initially not all fruits should be collected
        assertFalse("Initially not all fruits should be collected", level.allFruitsCollected());
        
        // Collect all fruits
        for (Fruit fruit : level.getFruits()) {
            fruit.collect();
        }
        
        assertTrue("All fruits should be collected", level.allFruitsCollected());
    }
    
    @Test
    public void testIceBlockManagement() {
        Level level = new Level(1);
        Position icePos = new Position(5, 5);
        
        // Add ice block
        IceBlock iceBlock = new IceBlock(icePos);
        level.addIceBlock(iceBlock);
        
        // Verify ice block exists
        assertTrue("Should have ice block at position", level.isIceBlock(icePos));
        assertEquals("Should retrieve the same ice block", iceBlock, level.getIceBlockAt(icePos));
    }
}