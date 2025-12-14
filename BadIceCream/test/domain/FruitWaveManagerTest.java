package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class FruitWaveManagerTest {

    @Test
    public void testWaveManagerCreation() {
        FruitWaveManager manager = new FruitWaveManager(3);
        assertEquals(0, manager.getCurrentWave()); // Starts at 0 or 1? Usually 0 until started.
        assertFalse(manager.isAllWavesCompleted());
    }

    @Test
    public void testStartWave() {
        FruitWaveManager manager = new FruitWaveManager(2);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, 1));

        manager.startWave(1, FruitType.BANANA, positions);

        assertEquals(1, manager.getCurrentWave());

        List<Fruit> fruits = manager.generateWaveFruits();
        assertEquals(1, fruits.size());
        assertEquals(FruitType.BANANA, fruits.get(0).getFruitType());
    }

    @Test
    public void testWaveCompletion() {
        FruitWaveManager manager = new FruitWaveManager(1);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, 1));

        manager.startWave(1, FruitType.BANANA, positions);
        List<Fruit> fruits = manager.generateWaveFruits();

        assertFalse(manager.isWaveCompleted(fruits));

        fruits.get(0).collect();
        assertTrue(manager.isWaveCompleted(fruits));
    }
}
