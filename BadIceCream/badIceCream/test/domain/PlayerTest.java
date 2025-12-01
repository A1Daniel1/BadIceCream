package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerCreation() {
        Position startPos = new Position(5, 5);
        Player player = new Player(startPos, IceCreamFlavour.VANILLA);

        assertNotNull(player, "Player should not be null");
        assertEquals(startPos, player.getPosition(), "Position should match");
        assertEquals(IceCreamFlavour.VANILLA, player.getFlavor(), "Flavor should be VANILLA");
        assertEquals(0, player.getScore(), "Initial score should be 0");
    }

    @Test
    public void testPlayerScore() {
        Player player = new Player(new Position(5, 5), IceCreamFlavour.VANILLA);

        player.addScore(50);
        assertEquals(50, player.getScore(), "Score should be 50");

        player.addScore(25);
        assertEquals(75, player.getScore(), "Score should be 75");
    }
}