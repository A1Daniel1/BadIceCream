package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Level.
 * Verifica la carga de niveles, validación de movimientos y gestión de objetos.
 */
public class LevelTest {

    /**
     * Verifica la creación correcta de un nivel.
     */
    @Test
    public void testLevelCreation() {
        Level level = new Level(1);

        assertNotNull(level, "El nivel no debería ser null");
        assertTrue(level.getWidth() > 0, "El ancho debería ser positivo");
        assertTrue(level.getHeight() > 0, "El alto debería ser positivo");
    }

    /**
     * Verifica que las colecciones del nivel (muros, frutas, enemigos) no sean
     * nulas.
     */
    @Test
    public void testLevelCollections() {
        Level level = new Level(1);

        assertNotNull(level.getWalls(), "Los muros no deberían ser null");
        assertNotNull(level.getIceBlocks(), "Los bloques de hielo no deberían ser null");
        assertNotNull(level.getFruits(), "Las frutas no deberían ser null");
        assertNotNull(level.getEnemies(), "Los enemigos no deberían ser null");

        assertFalse(level.getWalls().isEmpty(), "El nivel 1 debería tener muros");
        assertFalse(level.getFruits().isEmpty(), "El nivel 1 debería tener frutas");
        assertFalse(level.getEnemies().isEmpty(), "El nivel 1 debería tener enemigos");
    }

    /**
     * Verifica la validación de movimientos (colisiones con muros).
     */
    @Test
    public void testMovementValidation() {
        Level level = new Level(1);

        // Prueba posiciones de borde (deberían ser muros)
        Position borderPos = new Position(0, 0);
        assertFalse(level.canMoveTo(borderPos), "No debería poder moverse hacia un muro del borde");

        // Prueba posición central (debería ser transitable)
        Position centerPos = new Position(7, 5);
        assertTrue(level.canMoveTo(centerPos), "Debería poder moverse a una posición central libre");
    }

    /**
     * Verifica la existencia y recuperación de frutas en el nivel.
     */
    @Test
    public void testFruitCollection() {
        Level level = new Level(1);

        // Obtener una posición de fruta
        Fruit firstFruit = level.getFruits().get(0);
        Position fruitPos = firstFruit.getPosition();

        // Verificar que la fruta existe en esa posición
        Fruit foundFruit = level.getFruitAt(fruitPos);
        assertNotNull(foundFruit, "Debería encontrar fruta en su posición");
        assertEquals(firstFruit, foundFruit, "La fruta encontrada debería ser la misma");
    }

    /**
     * Verifica la lógica de recolección de todas las frutas.
     */
    @Test
    public void testAllFruitsCollected() {
        Level level = new Level(1);

        // Inicialmente no todas las frutas deberían estar recolectadas
        assertFalse(level.allFruitsCollected(), "Inicialmente no todas las frutas deberían estar recolectadas");

        // Recolectar todas las frutas de la primera oleada
        for (Fruit fruit : new java.util.ArrayList<>(level.getFruits())) {
            fruit.collect();
        }

        // Verificar que aún no se ha ganado porque falta la segunda oleada
        level.update(); // Esto debería cargar la siguiente oleada
        assertFalse(level.allFruitsCollected(), "No debería ganar aún, falta la segunda oleada");

        // Recolectar frutas de la segunda oleada
        for (Fruit fruit : new java.util.ArrayList<>(level.getFruits())) {
            fruit.collect();
        }

        // Ahora sí debería haber ganado
        assertTrue(level.allFruitsCollected(), "Todas las frutas deberían estar recolectadas");
    }

    /**
     * Verifica la gestión de bloques de hielo (agregar y verificar existencia).
     */
    @Test
    public void testIceBlockManagement() {
        Level level = new Level(1);
        Position icePos = new Position(5, 5);

        // Agregar bloque de hielo
        IceBlock iceBlock = new IceBlock(icePos);
        level.addIceBlock(iceBlock);

        // Verificar existencia del bloque de hielo
        assertTrue(level.isIceBlock(icePos), "Debería haber un bloque de hielo en la posición");
        assertEquals(iceBlock, level.getIceBlockAt(icePos), "Debería recuperar el mismo bloque de hielo");
    }

    @Test
    public void testOutOfBoundsMovement() {
        Level level = new Level(1);
        assertFalse(level.canMoveTo(new Position(-1, 0)));
        assertFalse(level.canMoveTo(new Position(0, -1)));
        assertFalse(level.canMoveTo(new Position(100, 100)));
    }

    @Test
    public void testEntityRemoval() {
        Level level = new Level(1);

        // Remove fruit
        assertFalse(level.getFruits().isEmpty());
        Fruit fruit = level.getFruits().get(0);
        level.getFruits().remove(fruit);
        assertFalse(level.getFruits().contains(fruit));

        // Remove enemy
        assertFalse(level.getEnemies().isEmpty());
        Enemy enemy = level.getEnemies().get(0);
        level.getEnemies().remove(enemy);
        assertFalse(level.getEnemies().contains(enemy));
    }
}