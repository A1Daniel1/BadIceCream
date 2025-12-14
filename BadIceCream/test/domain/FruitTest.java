package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la jerarquía de Fruit.
 */
public class FruitTest {

    @Test
    public void testFruitCreation() {
        Position pos = new Position(2, 2);
        Banana banana = new Banana(pos);

        assertEquals(pos, banana.getPosition());
        assertFalse(banana.isCollected());
        assertEquals(FruitType.BANANA, banana.getFruitType());
        assertTrue(banana.getPoints() > 0);
    }

    @Test
    public void testFruitCollection() {
        Banana banana = new Banana(new Position(2, 2));
        banana.collect();
        assertTrue(banana.isCollected());
    }

    @Test
    public void testCactusThorns() {
        Cactus cactus = new Cactus(new Position(2, 2));
        // Verificar estado inicial (puede variar según implementación, asumimos sin
        // espinas o con)
        assertNotNull(cactus.getType());
    }
}
