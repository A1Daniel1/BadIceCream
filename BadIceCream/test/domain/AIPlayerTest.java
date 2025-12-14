package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias para la clase AIPlayer.
 * Verifica el comportamiento de la IA bajo diferentes perfiles y situaciones.
 */
public class AIPlayerTest {

    /**
     * Verifica la creación correcta del AIPlayer.
     */
    @Test
    public void testAIPlayerCreation() {
        Position startPos = new Position(1, 1);
        AIPlayer ai = new AIPlayer(startPos, IceCreamFlavour.CHOCOLATE, AIProfile.HUNGRY);

        assertNotNull(ai);
        assertEquals(startPos, ai.getPosition());
        assertEquals(IceCreamFlavour.CHOCOLATE, ai.getFlavor());
        assertEquals(AIProfile.HUNGRY, ai.getProfile());
    }

    /**
     * Verifica que el perfil por defecto sea EXPERT si se pasa null.
     */
    @Test
    public void testAIPlayerDefaultProfile() {
        AIPlayer ai = new AIPlayer(new Position(1, 1), IceCreamFlavour.CHOCOLATE, null);
        assertEquals(AIProfile.EXPERT, ai.getProfile());
    }

    /**
     * Verifica que la IA (HUNGRY) se mueva hacia la fruta más cercana.
     */
    @Test
    public void testHungryProfileMovesToFruit() {
        Level level = new Level(1);
        // Limpiar el nivel para tener un escenario controlado
        level.getFruits().clear();
        level.getEnemies().clear();
        level.getWalls().clear();
        level.getIceBlocks().clear();

        // Jugador en (1,1)
        AIPlayer ai = new AIPlayer(new Position(1, 1), IceCreamFlavour.CHOCOLATE, AIProfile.HUNGRY);

        // Fruta en (3,1) - A la derecha
        Fruit fruit = new Banana(new Position(3, 1));
        level.getFruits().add(fruit);

        // La IA debería decidir moverse a la DERECHA
        // Nota: decideAction puede devolver WAIT si thinkCounter < thinkDelay
        // Forzamos que piense varias veces si es necesario o asumimos el delay inicial

        AIAction action = null;
        // Simular varios ticks hasta que decida moverse
        for (int i = 0; i < 10; i++) {
            action = ai.decideAction(level);
            if (action.getType() != AIActionType.WAIT)
                break;
        }

        assertEquals(AIActionType.MOVE, action.getType());
        assertEquals(Direction.RIGHT, action.getDirection());
    }

    /**
     * Verifica que la IA (FEARFUL) huya de un enemigo cercano.
     */
    @Test
    public void testFearfulProfileRunsFromEnemy() {
        Level level = new Level(1);
        level.getFruits().clear();
        level.getEnemies().clear();
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getCampfires().clear();
        level.getHotTiles().clear();

        // Jugador en (5,5)
        AIPlayer ai = new AIPlayer(new Position(5, 5), IceCreamFlavour.CHOCOLATE, AIProfile.FEARFUL);

        // Enemigo en (4,5) - A la izquierda
        Enemy enemy = new BasicEnemy(new Position(4, 5)); // Usamos una subclase concreta o mock
        level.getEnemies().add(enemy);

        AIAction action = null;
        for (int i = 0; i < 10; i++) {
            action = ai.decideAction(level);
            if (action.getType() != AIActionType.WAIT)
                break;
        }

        assertEquals(Direction.RIGHT, action.getDirection());
    }

    /**
     * Verifica que la IA (EXPERT) evite riesgos y busque frutas.
     */
    @Test
    public void testExpertProfileAvoidsRisk() {
        Level level = new Level(1);
        level.getFruits().clear();
        level.getEnemies().clear();
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getCampfires().clear();
        level.getHotTiles().clear();

        // Jugador en (5,5)
        AIPlayer ai = new AIPlayer(new Position(5, 5), IceCreamFlavour.CHOCOLATE, AIProfile.EXPERT);

        // Enemigo muy cerca en (6,5) - Peligro inmediato
        Enemy enemy = new BasicEnemy(new Position(6, 5));
        level.getEnemies().add(enemy);

        // Fruta en (4,5) - Seguro
        level.getFruits().add(new Banana(new Position(4, 5)));

        AIAction action = null;
        for (int i = 0; i < 10; i++) {
            action = ai.decideAction(level);
            if (action.getType() != AIActionType.WAIT)
                break;
        }

        // Debería moverse a la IZQUIERDA (alejarse del enemigo y hacia la fruta)
        assertEquals(AIActionType.MOVE, action.getType());
        assertEquals(Direction.LEFT, action.getDirection());
    }

    /**
     * Verifica que la IA no se mueva si está completamente bloqueada.
     */
    @Test
    public void testAIPlayerBlocked() {
        Level level = new Level(1);
        level.getFruits().clear();
        level.getEnemies().clear();
        level.getWalls().clear();
        level.getIceBlocks().clear();

        // Jugador en (1,1)
        AIPlayer ai = new AIPlayer(new Position(1, 1), IceCreamFlavour.CHOCOLATE, AIProfile.HUNGRY);

        // Rodear al jugador de muros
        level.getWalls().add(new Wall(new Position(1, 0))); // Arriba
        level.getWalls().add(new Wall(new Position(1, 2))); // Abajo
        level.getWalls().add(new Wall(new Position(0, 1))); // Izquierda
        level.getWalls().add(new Wall(new Position(2, 1))); // Derecha

        // Fruta inalcanzable
        level.getFruits().add(new Banana(new Position(3, 1)));

        AIAction action = ai.decideAction(level);

        // Debería esperar porque no puede moverse
        assertEquals(AIActionType.WAIT, action.getType());
    }

    /**
     * Verifica que la IA (EXPERT) evite Baldosas Calientes si es posible.
     */
    @Test
    public void testExpertAvoidsHotTiles() {
        Level level = new Level(1);
        level.getFruits().clear();
        level.getEnemies().clear();
        level.getWalls().clear();
        level.getIceBlocks().clear();
        level.getCampfires().clear();
        level.getHotTiles().clear();

        // Jugador en (1,1)
        AIPlayer ai = new AIPlayer(new Position(1, 1), IceCreamFlavour.CHOCOLATE, AIProfile.EXPERT);

        // Baldosa caliente en (2,1) - Derecha
        level.getHotTiles().add(new HotTile(new Position(2, 1)));

        // Fruta en (3,1) - Detrás de la baldosa caliente
        level.getFruits().add(new Banana(new Position(3, 1)));

        // Camino alternativo seguro: (1,1) -> (1,2) -> (2,2) -> (3,2) -> (3,1)
        // Pero la IA simple podría simplemente esperar o moverse a otro lado si el
        // camino directo es peligroso
        // Para este test, aseguramos que NO vaya a la derecha inmediatamente

        AIAction action = ai.decideAction(level);

        assertNotEquals(Direction.RIGHT, action.getDirection(), "No debería moverse a la baldosa caliente");
    }

    // Clase auxiliar para probar Enemy abstracta si no hay concreta simple
    private class BasicEnemy extends Enemy {
        public BasicEnemy(Position pos) {
            super(pos, EnemyType.TROLL);
        }

        @Override
        public void updateBehavior(Level level, Player player) {
        }

        @Override
        public String getSymbol() {
            return "E";
        }

        @Override
        public boolean canMoveTo(Position pos, Level level) {
            return true;
        }

        @Override
        public Position getNextPosition(Level level, Player player) {
            return position;
        }
    }
}
