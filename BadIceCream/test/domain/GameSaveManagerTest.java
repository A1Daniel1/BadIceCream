package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;

/**
 * Pruebas unitarias para GameSaveManager.
 */
public class GameSaveManagerTest {

    @Test
    public void testSaveAndLoadGame(@TempDir Path tempDir) {
        // Usar un archivo temporal
        File saveFile = tempDir.resolve("savegame.dat").toFile();
        String savePath = saveFile.getAbsolutePath();

        Game game = new Game();
        game.startGame(1, IceCreamFlavour.VANILLA);
        // Modificar estado para verificar persistencia
        game.getPlayer().addScore(100);

        // Guardar
        try {
            GameSaveManager.save(game, saveFile);
        } catch (BadIceCreamException e) {
            fail("No debería fallar al guardar: " + e.getMessage());
        }

        assertTrue(saveFile.exists(), "El archivo de guardado debería existir");

        // Cargar
        Game loadedGame = null;
        try {
            loadedGame = GameSaveManager.open(saveFile);
        } catch (BadIceCreamException e) {
            fail("No debería fallar al cargar: " + e.getMessage());
        }

        assertNotNull(loadedGame, "El juego cargado no debería ser null");
        assertEquals(1, loadedGame.getLevel().levelNumber);
        assertEquals(100, loadedGame.getPlayer().getScore());
    }

    @Test
    public void testLoadNonExistentFile() {
        assertThrows(BadIceCreamException.class, () -> {
            GameSaveManager.open(new File("non_existent_file.dat"));
        });
    }

    @Test
    public void testLoadCorruptedFile(@TempDir Path tempDir) throws Exception {
        File corruptedFile = tempDir.resolve("corrupted.dat").toFile();
        // Write garbage data
        java.nio.file.Files.write(corruptedFile.toPath(), "Not a valid object stream".getBytes());

        assertThrows(BadIceCreamException.class, () -> {
            GameSaveManager.open(corruptedFile);
        });
    }
}
