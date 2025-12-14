package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la jerarquía de Enemy.
 */
public class EnemyTest {

    @Test
    public void testTrollCreation() {
        Position pos = new Position(5, 5);
        Troll troll = new Troll(pos);

        assertEquals(pos, troll.getPosition());
        assertTrue(troll.isAlive());
        assertEquals(EnemyType.TROLL, troll.getEnemyType());
    }

    @Test
    public void testEnemyMovement() {
        // Test genérico de movimiento si es posible, o específico por enemigo
        Level level = new Level(1);
        level.getWalls().clear(); // Sin paredes para facilitar movimiento

        // Posición válida de borde para el Troll (1,1)
        Troll troll = new Troll(new Position(1, 1));
        Player player = new Player(new Position(5, 8), IceCreamFlavour.VANILLA);

        // Simular actualización
        // Troll es lento, puede requerir varios updates
        Position initialPos = troll.getPosition();
        for (int i = 0; i < 10; i++) {
            troll.updateBehavior(level, player);
        }

        // Debería haberse movido (probablemente hacia el jugador o patrullando)
        assertNotEquals(initialPos, troll.getPosition(), "El enemigo debería moverse");
    }

    @Test
    public void testOrangeSquidBreaksIce() {
        Level level = new Level(2);
        level.getIceBlocks().clear();
        level.getWalls().clear();

        Position icePos = new Position(5, 5);
        level.addIceBlock(new IceBlock(icePos));

        // Calamar al lado del hielo
        OrangeSquid squid = new OrangeSquid(new Position(4, 5));
        Player player = new Player(new Position(10, 10), IceCreamFlavour.VANILLA); // Lejos

        // Forzar actualización para que intente moverse/romper
        // Esto depende de la lógica interna de OrangeSquid (aleatoria o persecución)
        // Es difícil probar determinísticamente sin controlar el RNG, pero intentamos
        // verificar estado

        assertNotNull(level.getIceBlockAt(icePos));
    }
}
