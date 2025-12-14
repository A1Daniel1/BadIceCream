package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapElementTest {

    @Test
    public void testHotTile() {
        HotTile tile = new HotTile(new Position(1, 1));
        assertEquals("HotTile", tile.getType());
        assertEquals(new Position(1, 1), tile.getPosition());
        assertEquals("H", tile.getSymbol());
    }

    @Test
    public void testIceBlock() {
        IceBlock block = new IceBlock(new Position(2, 2));
        assertEquals("IceBlock", block.getType());
        assertEquals(new Position(2, 2), block.getPosition());
        assertEquals("#", block.getSymbol());

        block.destroy();
        // Since destroy() usually removes it from the level, we can't check 'alive'
        // status easily
        // without a level context, but we can check if the method runs without error.
        // If IceBlock had an 'intact' state, we would check that.
    }
}
