package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnumTest {

    @Test
    public void testAIProfileEnum() {
        assertEquals(3, AIProfile.values().length);
        assertEquals(AIProfile.HUNGRY, AIProfile.valueOf("HUNGRY"));
        assertEquals(AIProfile.FEARFUL, AIProfile.valueOf("FEARFUL"));
        assertEquals(AIProfile.EXPERT, AIProfile.valueOf("EXPERT"));
    }

    @Test
    public void testDirectionEnum() {
        assertEquals(4, Direction.values().length);
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
    }

    @Test
    public void testEnemyTypeEnum() {
        assertEquals(3, EnemyType.values().length);
        assertEquals(EnemyType.TROLL, EnemyType.valueOf("TROLL"));
        assertEquals(EnemyType.POT, EnemyType.valueOf("POT"));
        assertEquals(EnemyType.ORANGE_SQUID, EnemyType.valueOf("ORANGE_SQUID"));

        assertTrue(EnemyType.POT.canChase());
        assertFalse(EnemyType.TROLL.canChase());
        assertTrue(EnemyType.ORANGE_SQUID.canBreakBlocks());
        assertFalse(EnemyType.TROLL.canBreakBlocks());
    }

    @Test
    public void testFruitTypeEnum() {
        assertEquals(5, FruitType.values().length);
        assertEquals(FruitType.BANANA, FruitType.valueOf("BANANA"));
        assertEquals(FruitType.GRAPE, FruitType.valueOf("GRAPE"));
        assertEquals(FruitType.PINEAPPLE, FruitType.valueOf("PINEAPPLE"));
        assertEquals(FruitType.CHERRY, FruitType.valueOf("CHERRY"));
        assertEquals(FruitType.CACTUS, FruitType.valueOf("CACTUS"));
    }

    @Test
    public void testGameModeEnum() {
        assertEquals(4, GameMode.values().length);
        assertEquals(GameMode.PLAYER, GameMode.valueOf("PLAYER"));
        assertEquals(GameMode.PLAYER_VS_PLAYER, GameMode.valueOf("PLAYER_VS_PLAYER"));

        assertFalse(GameMode.PLAYER.isMultiplayer());
        assertTrue(GameMode.PLAYER_VS_PLAYER.isMultiplayer());
        assertTrue(GameMode.PLAYER_VS_MACHINE.isMultiplayer());
        assertTrue(GameMode.MACHINE_VS_MACHINE.isMultiplayer());
    }

    @Test
    public void testGameStateEnum() {
        assertEquals(5, GameState.values().length);
        assertEquals(GameState.MENU, GameState.valueOf("MENU"));
        assertEquals(GameState.PLAYING, GameState.valueOf("PLAYING"));
        assertEquals(GameState.PAUSED, GameState.valueOf("PAUSED"));
        assertEquals(GameState.GAME_OVER, GameState.valueOf("GAME_OVER"));
        assertEquals(GameState.VICTORY, GameState.valueOf("VICTORY"));
    }

    @Test
    public void testIceCreamFlavourEnum() {
        assertEquals(3, IceCreamFlavour.values().length);
        assertEquals(IceCreamFlavour.VANILLA, IceCreamFlavour.valueOf("VANILLA"));
        assertEquals(IceCreamFlavour.CHOCOLATE, IceCreamFlavour.valueOf("CHOCOLATE"));
        assertEquals(IceCreamFlavour.STRAWBERRY, IceCreamFlavour.valueOf("STRAWBERRY"));
    }
}
