package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {

    @Test
    public void testLevelCreation() {
        Level level = new Level(1);

        assertNotNull(level, "Level should not be null");
        assertTrue(level.getWidth() > 0, "Width should be positive");
        assertTrue(level.getHeight() > 0, "Height should be positive");
    }

    @Test
    public void testLevelCollections() {
        Level level = new Level(1);

        assertNotNull(level.getWalls(), "Walls should not be null");
        assertNotNull(level.getIceBlocks(), "Ice blocks should not be null");
        assertNotNull(level.getFruits(), "Fruits should not be null");
        assertNotNull(level.getEnemies(), "Enemies should not be null");

        assertFalse(level.getWalls().isEmpty(), "Level 1 should have walls");
        assertFalse(level.getFruits().isEmpty(), "Level 1 should have fruits");
        assertFalse(level.getEnemies().isEmpty(), "Level 1 should have enemies");
    }

    @Test
    public void testMovementValidation() {
        Level level = new Level(1);

        // Test border positions (should be walls)
        Position borderPos = new Position(0, 0);
        assertFalse(level.canMoveTo(borderPos), "Should not be able to move into border wall");

        // Test center position (should be movable)
        Position centerPos = new Position(7, 5);
        assertTrue(level.canMoveTo(centerPos), "Should be able to move to center position");
    }

    @Test
    public void testFruitCollection() {
        Level level = new Level(1);

        // Get a fruit position
        Fruit firstFruit = level.getFruits().get(0);
        Position fruitPos = firstFruit.getPosition();

        // Verify fruit exists at that position
        Fruit foundFruit = level.getFruitAt(fruitPos);
        assertNotNull(foundFruit, "Should find fruit at its position");
        assertEquals(firstFruit, foundFruit, "Found fruit should be the same");
    }

    @Test
    public void testAllFruitsCollected() {
        Level level = new Level(1);

        // Initially not all fruits should be collected
        assertFalse(level.allFruitsCollected(), "Initially not all fruits should be collected");

        // Collect all fruits
        for (Fruit fruit : level.getFruits()) {
            fruit.collect();
        }

        assertTrue(level.allFruitsCollected(), "All fruits should be collected");
    }

    @Test
    public void testIceBlockManagement() {
        Level level = new Level(1);
        Position icePos = new Position(5, 5);

        // Add ice block
        IceBlock iceBlock = new IceBlock(icePos);
        level.addIceBlock(iceBlock);

        // Verify ice block exists
        assertTrue(level.isIceBlock(icePos), "Should have ice block at position");
        assertEquals(iceBlock, level.getIceBlockAt(icePos), "Should retrieve the same ice block");
    }
}