package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para LevelFactory.
 */
public class LevelFactoryTest {

    @Test
    public void testCreateLevel1() {
        Level level = new Level(1);
        // Level constructor calls LevelFactory.createLevel(this, 1) internally

        assertFalse(level.getWalls().isEmpty(), "El nivel 1 debería tener paredes");
        assertFalse(level.getFruits().isEmpty(), "El nivel 1 debería tener frutas");
        assertFalse(level.getEnemies().isEmpty(), "El nivel 1 debería tener enemigos");

        // Verificar elementos específicos del nivel 1
        // 2 Trolls y 1 Pot según LevelFactory
        long trolls = level.getEnemies().stream().filter(e -> e instanceof Troll).count();
        assertEquals(2, trolls);
    }

    @Test
    public void testCreateLevel2() {
        Level level = new Level(2);

        assertFalse(level.getIceBlocks().isEmpty(), "El nivel 2 debería tener bloques de hielo iniciales");
        // Verificar elementos específicos del nivel 2
        // 2 OrangeSquid y 1 Troll
        long squids = level.getEnemies().stream().filter(e -> e instanceof OrangeSquid).count();
        assertEquals(2, squids);
    }

    @Test
    public void testCreateLevel3() {
        Level level = new Level(3);

        // Verificar elementos específicos del nivel 3
        // 1 Narwhal
        long narwhals = level.getEnemies().stream().filter(e -> e instanceof Narwhal).count();
        assertEquals(1, narwhals);
    }
}
