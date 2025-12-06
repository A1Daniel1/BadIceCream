package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Player.
 * Verifica la creación del jugador, puntuación y creación/destrucción de hielo.
 */
public class PlayerTest {

    /**
     * Verifica la creación correcta del jugador con sus atributos iniciales.
     */
    @Test
    public void testPlayerCreation() {
        Position startPos = new Position(5, 5);
        Player player = new Player(startPos, IceCreamFlavour.VANILLA);

        assertNotNull(player, "El jugador no debería ser null");
        assertEquals(startPos, player.getPosition(), "La posición debería coincidir");
        assertEquals(IceCreamFlavour.VANILLA, player.getFlavor(), "El sabor debería ser VAINILLA");
        assertEquals(0, player.getScore(), "El puntaje inicial debería ser 0");
    }

    /**
     * Verifica la acumulación correcta de puntos.
     */
    @Test
    public void testPlayerScore() {
        Player player = new Player(new Position(5, 5), IceCreamFlavour.VANILLA);

        player.addScore(50);
        assertEquals(50, player.getScore(), "El puntaje debería ser 50");

        player.addScore(25);
        assertEquals(75, player.getScore(), "El puntaje debería ser 75");
    }

    /**
     * Verifica la creación y destrucción de líneas de bloques de hielo.
     */
    @Test
    public void testIceBlockLineCreation() {
        Level level = new Level(1);
        Player player = new Player(new Position(1, 1), IceCreamFlavour.VANILLA);
        player.setDirection(Direction.RIGHT);

        // El Nivel 1 tiene un Troll en (13, 1). La fila 1 está libre de muros entre 1 y
        // 13.
        // El jugador está en (1, 1). El hielo debería crearse desde (2, 1) hasta (12,
        // 1).

        player.createIceBlock(level);

        // Verificar algunas posiciones
        assertTrue(level.isIceBlock(new Position(2, 1)), "Debería haber hielo en (2,1)");
        assertTrue(level.isIceBlock(new Position(5, 1)), "Debería haber hielo en (5,1)");
        assertTrue(level.isIceBlock(new Position(12, 1)), "Debería haber hielo en (12,1)");

        // Verificar que se detuvo en el enemigo (Troll en 13,1)
        assertFalse(level.isIceBlock(new Position(13, 1)), "NO debería haber hielo en la posición del enemigo (13,1)");

        // Prueba de Destrucción
        player.destroyIceBlock(level);
        assertFalse(level.isIceBlock(new Position(2, 1)), "El hielo en (2,1) debería ser destruido");
        assertFalse(level.isIceBlock(new Position(12, 1)), "El hielo en (12,1) debería ser destruido");
    }
}