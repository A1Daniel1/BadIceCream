package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerInteractionTest {

    @Test
    public void testCreateIceBlockStopsAtWall() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Level level = game.getLevel();
        Player player = game.getPlayer();

        level.getWalls().clear();
        level.getIceBlocks().clear();

        // Wall at (3,1)
        level.getWalls().add(new Wall(new Position(3, 1)));

        // Player at (1,1) facing RIGHT
        player.setDirection(Direction.RIGHT);

        // Create ice
        game.createIceBlock();

        // Should create ice at (2,1) only
        assertTrue(level.isIceBlock(new Position(2, 1)));
        assertFalse(level.isIceBlock(new Position(3, 1))); // Wall
    }

    @Test
    public void testDestroyIceBlockChain() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        Level level = game.getLevel();
        Player player = game.getPlayer();

        level.getWalls().clear();
        level.getIceBlocks().clear();

        // Line of ice: (2,1), (3,1), (4,1)
        level.addIceBlock(new IceBlock(new Position(2, 1)));
        level.addIceBlock(new IceBlock(new Position(3, 1)));
        level.addIceBlock(new IceBlock(new Position(4, 1)));

        // Player at (1,1) facing RIGHT
        player.setDirection(Direction.RIGHT);

        // Destroy ice
        game.destroyIceBlock();

        // All should be destroyed
        assertFalse(level.isIceBlock(new Position(2, 1)));
        assertFalse(level.isIceBlock(new Position(3, 1)));
        assertFalse(level.isIceBlock(new Position(4, 1)));
    }

    @Test
    public void testPlayerDeathState() {
        Player player = new Player(new Position(1, 1), IceCreamFlavour.VANILLA);
        assertTrue(player.isAlive());

        player.die();
        assertFalse(player.isAlive());

        player.revive();
        assertTrue(player.isAlive());
    }
}
