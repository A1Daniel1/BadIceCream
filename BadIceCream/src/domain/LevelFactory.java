package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating levels.
 * Encapsulates the logic for setting up walls, fruits, enemies, etc. for each
 * level.
 */
public class LevelFactory {

    public static void createLevel(Level level, int levelNumber) {
        createBorderWalls(level);

        switch (levelNumber) {
            case 1:
                createLevel1(level);
                break;
            case 2:
                createLevel2(level);
                break;
            case 3:
                createLevel3(level);
                break;
            default:
                createLevel1(level);
        }
    }

    private static void createBorderWalls(Level level) {
        int width = level.getWidth();
        int height = level.getHeight();
        List<Wall> walls = level.getWalls();

        for (int x = 0; x < width; x++) {
            walls.add(new Wall(new Position(x, 0)));
            walls.add(new Wall(new Position(x, height - 1)));
        }
        for (int y = 0; y < height; y++) {
            walls.add(new Wall(new Position(0, y)));
            walls.add(new Wall(new Position(width - 1, y)));
        }
    }

    private static void createLevel1(Level level) {
        List<Wall> walls = level.getWalls();
        List<Fruit> fruits = level.getFruits();
        List<Enemy> enemies = level.getEnemies();
        List<Campfire> campfires = level.getCampfires();
        List<HotTile> hotTiles = level.getHotTiles();

        // Paredes internas
        for (int i = 3; i < 12; i += 2) {
            for (int j = 2; j < 9; j += 2) {
                walls.add(new Wall(new Position(i, j)));
            }
        }

        // Sistema de oleadas - 2 oleadas
        FruitWaveManager waveManager = new FruitWaveManager(2);
        level.setWaveManager(waveManager);

        // Oleada 1: Uvas
        List<Position> wave1Positions = new ArrayList<>();
        wave1Positions.add(new Position(2, 2));
        wave1Positions.add(new Position(4, 2));
        wave1Positions.add(new Position(6, 2));
        wave1Positions.add(new Position(8, 2));
        wave1Positions.add(new Position(10, 2));
        wave1Positions.add(new Position(12, 2));
        wave1Positions.add(new Position(13, 3));
        wave1Positions.add(new Position(13, 5));

        waveManager.startWave(1, FruitType.GRAPE, wave1Positions);
        fruits.addAll(waveManager.generateWaveFruits());

        // 2 Trolls (patrullan el borde)
        enemies.add(new Troll(new Position(13, 1)));
        enemies.add(new Troll(new Position(1, 9)));

        // 1 Maceta (persigue al jugador)
        enemies.add(new Pot(new Position(7, 5)));

        // Fogatas
        campfires.add(new Campfire(new Position(5, 5)));
        campfires.add(new Campfire(new Position(9, 5)));

        // Baldosas calientes
        hotTiles.add(new HotTile(new Position(2, 5)));
        hotTiles.add(new HotTile(new Position(12, 5)));
    }

    private static void createLevel2(Level level) {
        List<Wall> walls = level.getWalls();
        List<Fruit> fruits = level.getFruits();
        List<Enemy> enemies = level.getEnemies();
        List<Campfire> campfires = level.getCampfires();
        List<HotTile> hotTiles = level.getHotTiles();
        List<IceBlock> iceBlocks = level.getIceBlocks();

        // NIVEL 2: Introduce Piñas (frutas móviles) y Calamares (rompen hielo)

        // Paredes en forma de cruz
        for (int i = 1; i < 14; i++) {
            if (i != 6) {
                iceBlocks.add(new IceBlock(new Position(i, 6)));
                iceBlocks.add(new IceBlock(new Position(6, i)));
            }
        }

        // Sistema de oleadas - 3 oleadas
        FruitWaveManager waveManager = new FruitWaveManager(3);
        level.setWaveManager(waveManager);

        // Oleada 1: Plátanos (estáticos)
        List<Position> wave1Positions = new ArrayList<>();
        wave1Positions.add(new Position(2, 2));
        wave1Positions.add(new Position(12, 2));
        wave1Positions.add(new Position(2, 8));
        wave1Positions.add(new Position(12, 8));
        wave1Positions.add(new Position(3, 3));
        wave1Positions.add(new Position(11, 3));

        waveManager.startWave(1, FruitType.BANANA, wave1Positions);
        fruits.addAll(waveManager.generateWaveFruits());

        // 2 Calamares Naranjas (persiguen y rompen hielo)
        enemies.add(new OrangeSquid(new Position(3, 7)));
        enemies.add(new OrangeSquid(new Position(11, 7)));

        // 1 Troll (patrulla el borde)
        enemies.add(new Troll(new Position(13, 1)));

        // Fogatas en las esquinas
        campfires.add(new Campfire(new Position(2, 2)));
        campfires.add(new Campfire(new Position(12, 2)));
        campfires.add(new Campfire(new Position(2, 8)));
        campfires.add(new Campfire(new Position(12, 8)));

        // Baldosas calientes en el centro
        hotTiles.add(new HotTile(new Position(6, 5)));
        hotTiles.add(new HotTile(new Position(8, 5)));
        hotTiles.add(new HotTile(new Position(7, 4)));
        hotTiles.add(new HotTile(new Position(7, 6)));
    }

    private static void createLevel3(Level level) {
        List<Wall> walls = level.getWalls();
        List<Fruit> fruits = level.getFruits();
        List<Enemy> enemies = level.getEnemies();
        List<Campfire> campfires = level.getCampfires();
        List<HotTile> hotTiles = level.getHotTiles();

        // NIVEL 3: Introduce Cerezas, Cactus (peligrosos) y Narval (embiste)

        // Paredes en laberinto
        for (int y = 2; y <= 8; y += 2) {
            for (int x = 2; x <= 12; x += 3) {
                walls.add(new Wall(new Position(x, y)));
            }
        }

        // Paredes adicionales para hacer más complejo
        walls.add(new Wall(new Position(4, 3)));
        walls.add(new Wall(new Position(4, 7)));
        walls.add(new Wall(new Position(10, 3)));
        walls.add(new Wall(new Position(10, 7)));

        // Sistema de oleadas - 3 oleadas
        FruitWaveManager waveManager = new FruitWaveManager(3);
        level.setWaveManager(waveManager);

        // Oleada 1: Cerezas (móviles aleatorias)
        List<Position> wave1Positions = new ArrayList<>();
        wave1Positions.add(new Position(3, 1));
        wave1Positions.add(new Position(7, 1));
        wave1Positions.add(new Position(11, 1));
        wave1Positions.add(new Position(1, 5));
        wave1Positions.add(new Position(13, 5));

        waveManager.startWave(1, FruitType.CHERRY, wave1Positions);
        fruits.addAll(waveManager.generateWaveFruits());

        // 1 Narval (embiste cuando se alinea)
        enemies.add(new Narwhal(new Position(7, 5)));

        // 2 Macetas (persiguen)
        enemies.add(new Pot(new Position(3, 3)));
        enemies.add(new Pot(new Position(11, 7)));

        // 1 Calamar (rompe hielo)
        enemies.add(new OrangeSquid(new Position(7, 9)));

        // Fogatas estratégicamente colocadas
        campfires.add(new Campfire(new Position(1, 2)));
        campfires.add(new Campfire(new Position(13, 1)));
        campfires.add(new Campfire(new Position(1, 9)));
        campfires.add(new Campfire(new Position(12, 9)));

        // Baldosas calientes en pasillos
        hotTiles.add(new HotTile(new Position(5, 5)));
        hotTiles.add(new HotTile(new Position(9, 5)));
        hotTiles.add(new HotTile(new Position(7, 3)));
        hotTiles.add(new HotTile(new Position(7, 7)));
    }
}
