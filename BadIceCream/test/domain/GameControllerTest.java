package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.event.KeyEvent;
import javax.swing.JButton; // Dummy component for KeyEvent

public class GameControllerTest {

    @Test
    public void testGameControllerWASD() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        GameController controller = new GameController(game);

        Player player = game.getPlayer();
        Position startPos = player.getPosition();

        // Simulate 'D' (Right)
        KeyEvent rightEvent = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_D, 'd');
        controller.keyPressed(rightEvent);

        assertEquals(Direction.RIGHT, player.getDirection());
        // Position might change if move is valid
    }

    @Test
    public void testGameControllerSpace() {
        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        GameController controller = new GameController(game);

        // Simulate 'Space' (Create Ice)
        KeyEvent spaceEvent = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_SPACE, ' ');
        controller.keyPressed(spaceEvent);

        // Check if ice was created in front of player
        Position target = game.getPlayer().getPosition().add(game.getPlayer().getDirection());
        assertTrue(game.getLevel().isIceBlock(target));
    }

    @Test
    public void testGameController2Arrows() {
        Game game = new Game();
        // Must be in PvP mode for Controller 2 to work
        game.startGame(1, IceCreamFlavour.VANILLA, GameMode.PLAYER_VS_PLAYER, null, null);
        GameController2 controller2 = new GameController2(game);

        Player player2 = game.getPlayer2();
        assertNotNull(player2);

        // Simulate 'Left' Arrow
        KeyEvent leftEvent = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        controller2.keyPressed(leftEvent);

        assertEquals(Direction.LEFT, player2.getDirection());
    }
}
