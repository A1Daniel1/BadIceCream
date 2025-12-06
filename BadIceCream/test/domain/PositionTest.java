package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Position.
 * Verifica la creación, igualdad, movimiento y clonación de posiciones.
 */
public class PositionTest {

    /**
     * Verifica la creación correcta de una posición con coordenadas X e Y.
     */
    @Test
    public void testPositionCreation() {
        Position pos = new Position(3, 7);

        assertEquals(3, pos.getX(), "La coordenada X debería coincidir");
        assertEquals(7, pos.getY(), "La coordenada Y debería coincidir");
    }

    /**
     * Verifica la igualdad entre dos objetos Position.
     */
    @Test
    public void testPositionEquality() {
        Position pos1 = new Position(2, 4);
        Position pos2 = new Position(2, 4);
        Position pos3 = new Position(2, 5);

        assertTrue(pos1.equals(pos2), "Posiciones iguales deberían ser iguales");
        assertFalse(pos1.equals(pos3), "Posiciones diferentes no deberían ser iguales");
    }

    /**
     * Verifica el cálculo de nuevas posiciones al moverse en una dirección.
     */
    @Test
    public void testPositionMovement() {
        Position start = new Position(5, 5);

        Position right = start.add(Direction.RIGHT);
        assertEquals(6, right.getX(), "Moverse a la derecha debería incrementar X");
        assertEquals(5, right.getY(), "Moverse a la derecha no debería cambiar Y");

        Position down = start.add(Direction.DOWN);
        assertEquals(5, down.getX(), "Moverse abajo no debería cambiar X");
        assertEquals(6, down.getY(), "Moverse abajo debería incrementar Y");

        Position left = start.add(Direction.LEFT);
        assertEquals(4, left.getX(), "Moverse a la izquierda debería decrementar X");
        assertEquals(5, left.getY(), "Moverse a la izquierda no debería cambiar Y");

        Position up = start.add(Direction.UP);
        assertEquals(5, up.getX(), "Moverse arriba no debería cambiar X");
        assertEquals(4, up.getY(), "Moverse arriba debería decrementar Y");
    }

    /**
     * Verifica que la clonación de una posición cree un nuevo objeto con los mismos
     * valores.
     */
    @Test
    public void testPositionCloning() {
        Position original = new Position(3, 4);
        Position clone = original.clone();

        assertEquals(original.getX(), clone.getX(), "El clon debería tener la misma X");
        assertEquals(original.getY(), clone.getY(), "El clon debería tener la misma Y");
        assertNotSame(original, clone, "El clon debería ser un objeto diferente");
    }

    /**
     * Verifica la representación en cadena de la posición.
     */
    @Test
    public void testPositionStringRepresentation() {
        Position pos = new Position(2, 3);
        String str = pos.toString();

        assertTrue(str.contains("2") && str.contains("3"), "La cadena debería contener las coordenadas");
    }
}