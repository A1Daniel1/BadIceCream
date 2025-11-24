package domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {
    
    @Test
    public void testPlayerCreation() {
        Position startPos = new Position(5, 5);
        Player player = new Player(startPos, IceCreamFlavour.VANILLA);
        
        assertNotNull("Player should not be null", player);
        assertEquals("Position should match", startPos, player.getPosition());
        assertEquals("Flavor should be VANILLA", IceCreamFlavour.VANILLA, player.getFlavor());
        assertEquals("Initial score should be 0", 0, player.getScore());
    }
    
    @Test
    public void testPlayerScore() {
        Player player = new Player(new Position(5, 5), IceCreamFlavour.VANILLA);
        
        player.addScore(50);
        assertEquals("Score should be 50", 50, player.getScore());
        
        player.addScore(25);
        assertEquals("Score should be 75", 75, player.getScore());
    }
}