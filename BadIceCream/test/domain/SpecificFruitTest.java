package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpecificFruitTest {

    @Test
    public void testBanana() {
        Banana banana = new Banana(new Position(1, 1));
        assertEquals(FruitType.BANANA, banana.getFruitType());
        assertEquals(100, banana.getPoints());
        assertEquals("Fruit", banana.getType());
    }

    @Test
    public void testGrape() {
        Grape grape = new Grape(new Position(1, 1));
        assertEquals(FruitType.GRAPE, grape.getFruitType());
        assertEquals(50, grape.getPoints());
        assertEquals("Fruit", grape.getType());
    }

    @Test
    public void testPineapple() {
        Pineapple pineapple = new Pineapple(new Position(1, 1));
        assertEquals(FruitType.PINEAPPLE, pineapple.getFruitType());
        assertEquals(200, pineapple.getPoints());
        assertEquals("Fruit", pineapple.getType());
    }

    @Test
    public void testCherry() {
        Cherry cherry = new Cherry(new Position(1, 1));
        assertEquals(FruitType.CHERRY, cherry.getFruitType());
        assertEquals(200, cherry.getPoints());
        assertEquals("Fruit", cherry.getType());
    }

    @Test
    public void testCactus() {
        Cactus cactus = new Cactus(new Position(1, 1));
        assertEquals(FruitType.CACTUS, cactus.getFruitType());
        assertEquals(250, cactus.getPoints());
        assertEquals("Fruit", cactus.getType());

        // Verify thorns logic (if exposed or testable via interaction)
        // Assuming thorns are checked via collision in Game/Level, but properties can
        // be checked here
    }
}
