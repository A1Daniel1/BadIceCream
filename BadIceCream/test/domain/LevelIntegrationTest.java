package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class LevelIntegrationTest {

    @Test
    public void testWaveProgression() {
        Level level = new Level(1);
        FruitWaveManager waveManager = new FruitWaveManager(2);
        level.setWaveManager(waveManager);

        // Clear initial fruits
        level.getFruits().clear();

        // Start wave 1 manually
        waveManager.startWave(1, FruitType.BANANA, new ArrayList<>());

        // Ensure fruits list is empty for the test
        level.getFruits().clear();

        // Simulate collecting all fruits
        assertTrue(level.getFruits().isEmpty()); // Should be true as list is empty

        // Check wave completion logic
        level.checkWaveCompletion();

        // Should have advanced to wave 2 (Level 1 has 2 waves)
        assertEquals(2, waveManager.getCurrentWave());

        // Verify fruits for wave 2 were loaded (Level 1 Wave 2 adds fruits)
        assertFalse(level.getFruits().isEmpty());
        assertEquals(FruitType.BANANA, level.getFruits().get(0).getFruitType());
    }

    @Test
    public void testLevelLoading() {
        Level level1 = new Level(1);
        assertEquals(1, level1.levelNumber);
        assertNotNull(level1.getWalls());

        Level level2 = new Level(2);
        assertEquals(2, level2.levelNumber);

        Level level3 = new Level(3);
        assertEquals(3, level3.levelNumber);
    }
}
