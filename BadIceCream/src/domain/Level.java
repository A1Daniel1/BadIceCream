package domain;

import java.util.ArrayList;
import java.util.List;

public class Level {
    int levelNumber;
    private int width;
    private int height;
    private List<Wall> walls;
    private List<IceBlock> iceBlocks;
    private List<Fruit> fruits;
    private List<Enemy> enemies;
    private List<Campfire> campfires;
    private List<HotTile> hotTiles;
    private Player player;
    private Player player2; // Para modo PvsP
    private FruitWaveManager waveManager;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.width = 15;
        this.height = 11;
        this.walls = new ArrayList<>();
        this.iceBlocks = new ArrayList<>();
        this.fruits = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.campfires = new ArrayList<>();
        this.hotTiles = new ArrayList<>();
        initializeLevel();
    }

    private void initializeLevel() {
        createBorderWalls();

        switch (levelNumber) {
            case 1:
                createLevel1();
                break;
            case 2:
                createLevel2();
                break;
            case 3:
                createLevel3();
                break;
            default:
                createLevel1();
        }
    }

    private void createBorderWalls() {
        for (int x = 0; x < width; x++) {
            walls.add(new Wall(new Position(x, 0)));
            walls.add(new Wall(new Position(x, height - 1)));
        }
        for (int y = 0; y < height; y++) {
            walls.add(new Wall(new Position(0, y)));
            walls.add(new Wall(new Position(width - 1, y)));
        }
    }

    private void createLevel1() {
        // Paredes internas
        for (int i = 3; i < 12; i += 2) {
            for (int j = 2; j < 9; j += 2) {
                walls.add(new Wall(new Position(i, j)));
            }
        }

        // Sistema de oleadas - 2 oleadas
        waveManager = new FruitWaveManager(2);
        
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

    private void createLevel2() {
        // NIVEL 2: Introduce Piñas (frutas móviles) y Calamares (rompen hielo)
        
        // Paredes en forma de cruz
        for (int i = 1; i < 14; i++) {
            if (i != 7) {
                walls.add(new Wall(new Position(i, 5)));
                walls.add(new Wall(new Position(7, i)));
            }
        }
        
        // Sistema de oleadas - 3 oleadas
        waveManager = new FruitWaveManager(3);
        
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

    private void createLevel3() {
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
        waveManager = new FruitWaveManager(3);
        
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
        campfires.add(new Campfire(new Position(1, 1)));
        campfires.add(new Campfire(new Position(13, 1)));
        campfires.add(new Campfire(new Position(1, 9)));
        campfires.add(new Campfire(new Position(13, 9)));
        
        // Baldosas calientes en pasillos
        hotTiles.add(new HotTile(new Position(5, 5)));
        hotTiles.add(new HotTile(new Position(9, 5)));
        hotTiles.add(new HotTile(new Position(7, 3)));
        hotTiles.add(new HotTile(new Position(7, 7)));
    }
    
    /**
     * Verifica si se completó la oleada actual y carga la siguiente
     */
    public void checkWaveCompletion() {
        if (waveManager != null && waveManager.isWaveCompleted(fruits)) {
            if (waveManager.hasNextWave()) {
                waveManager.nextWave();
                
                // Cargar oleadas según el nivel
                if (levelNumber == 1 && waveManager.getCurrentWave() == 2) {
                    loadLevel1Wave2();
                } else if (levelNumber == 2) {
                    if (waveManager.getCurrentWave() == 2) {
                        loadLevel2Wave2();
                    } else if (waveManager.getCurrentWave() == 3) {
                        loadLevel2Wave3();
                    }
                } else if (levelNumber == 3) {
                    if (waveManager.getCurrentWave() == 2) {
                        loadLevel3Wave2();
                    } else if (waveManager.getCurrentWave() == 3) {
                        loadLevel3Wave3();
                    }
                }
            }
        }
    }
    
    private void loadLevel1Wave2() {
        List<Position> wave2Positions = new ArrayList<>();
        wave2Positions.add(new Position(2, 9));
        wave2Positions.add(new Position(4, 9));
        wave2Positions.add(new Position(6, 9));
        wave2Positions.add(new Position(8, 9));
        wave2Positions.add(new Position(10, 9));
        wave2Positions.add(new Position(12, 9));
        wave2Positions.add(new Position(1, 7));
        wave2Positions.add(new Position(1, 5));
        
        waveManager.startWave(2, FruitType.BANANA, wave2Positions);
        fruits.addAll(waveManager.generateWaveFruits());
    }
    
    private void loadLevel2Wave2() {
        List<Position> wave2Positions = new ArrayList<>();
        wave2Positions.add(new Position(1, 3));
        wave2Positions.add(new Position(1, 7));
        wave2Positions.add(new Position(13, 3));
        wave2Positions.add(new Position(13, 7));
        wave2Positions.add(new Position(5, 1));
        wave2Positions.add(new Position(9, 1));
        
        waveManager.startWave(2, FruitType.PINEAPPLE, wave2Positions);
        fruits.addAll(waveManager.generateWaveFruits());
    }
    
    private void loadLevel2Wave3() {
        List<Position> wave3Positions = new ArrayList<>();
        wave3Positions.add(new Position(5, 9));
        wave3Positions.add(new Position(9, 9));
        wave3Positions.add(new Position(3, 5));
        wave3Positions.add(new Position(11, 5));
        
        waveManager.startWave(3, FruitType.GRAPE, wave3Positions);
        fruits.addAll(waveManager.generateWaveFruits());
    }
    
    private void loadLevel3Wave2() {
        List<Position> wave2Positions = new ArrayList<>();
        wave2Positions.add(new Position(3, 9));
        wave2Positions.add(new Position(7, 9));
        wave2Positions.add(new Position(11, 9));
        wave2Positions.add(new Position(5, 5));
        wave2Positions.add(new Position(9, 5));
        
        waveManager.startWave(2, FruitType.CACTUS, wave2Positions);
        fruits.addAll(waveManager.generateWaveFruits());
    }
    
    private void loadLevel3Wave3() {
        List<Position> wave3Positions = new ArrayList<>();
        wave3Positions.add(new Position(2, 5));
        wave3Positions.add(new Position(12, 5));
        wave3Positions.add(new Position(7, 2));
        wave3Positions.add(new Position(7, 8));
        
        waveManager.startWave(3, FruitType.PINEAPPLE, wave3Positions);
        fruits.addAll(waveManager.generateWaveFruits());
    }

    public boolean canMoveTo(Position position) {
        if (position == null) return false;
        if (position.getX() < 0 || position.getX() >= width ||
            position.getY() < 0 || position.getY() >= height) {
            return false;
        }
        if (isWall(position)) return false;
        if (isIceBlock(position)) return false;
        return true;
    }

    public boolean canPlaceIceBlock(Position position) {
        if (!canMoveTo(position)) return false;
        
        if (isCampfire(position)) {
            Campfire fire = getCampfireAt(position);
            if (fire != null && fire.isLit()) {
                fire.extinguish();
                return false;
            }
        }
        
        return true;
    }
    
    public void addIceBlock(IceBlock block) {
        if (block != null) {
            if (isHotTile(block.getPosition())) {
                block.destroy();
            }
            iceBlocks.add(block);
        }
    }

    public boolean isWall(Position position) {
        if (position == null || walls == null) return false;
        return walls.stream().anyMatch(wall -> wall != null && wall.getPosition().equals(position));
    }

    public boolean isIceBlock(Position position) {
        if (position == null || iceBlocks == null) return false;
        return iceBlocks.stream()
            .anyMatch(block -> block != null && !block.isDestroyed() && block.getPosition().equals(position));
    }
    
    public boolean isCampfire(Position position) {
        if (position == null || campfires == null) return false;
        return campfires.stream().anyMatch(fire -> fire != null && fire.getPosition().equals(position));
    }
    
    public boolean isHotTile(Position position) {
        if (position == null || hotTiles == null) return false;
        return hotTiles.stream().anyMatch(tile -> tile != null && tile.getPosition().equals(position));
    }
    
    public boolean isDeadlyCampfire(Position position) {
        Campfire fire = getCampfireAt(position);
        return fire != null && fire.isDeadly();
    }

    public IceBlock getIceBlockAt(Position position) {
        if (position == null || iceBlocks == null) return null;
        return iceBlocks.stream()
            .filter(block -> block != null && !block.isDestroyed() && block.getPosition().equals(position))
            .findFirst()
            .orElse(null);
    }

    public Fruit getFruitAt(Position position) {
        if (position == null || fruits == null) return null;
        return fruits.stream()
            .filter(fruit -> fruit != null && !fruit.isCollected() && fruit.getPosition().equals(position))
            .findFirst()
            .orElse(null);
    }
    
    public Campfire getCampfireAt(Position position) {
        if (position == null || campfires == null) return null;
        return campfires.stream()
            .filter(fire -> fire != null && fire.getPosition().equals(position))
            .findFirst()
            .orElse(null);
    }

    public boolean hasEnemyAt(Position position) {
        if (position == null || enemies == null) return false;
        return enemies.stream()
            .anyMatch(enemy -> enemy != null && enemy.isAlive() && enemy.getPosition().equals(position));
    }

    public boolean allFruitsCollected() {
        if (waveManager != null) {
            boolean currentWaveComplete = waveManager.isWaveCompleted(fruits);
            
            if (currentWaveComplete && waveManager.hasNextWave()) {
                return false;
            }
            
            return waveManager.isAllWavesCompleted();
        }
        
        if (fruits == null || fruits.isEmpty()) return false;
        return fruits.stream().allMatch(Fruit::isCollected);
    }

    public void update() {
        if (campfires != null) {
            campfires.forEach(Campfire::update);
        }
        
        if (fruits != null) {
            fruits.stream()
                .filter(f -> f != null && !f.isCollected())
                .forEach(f -> f.update(this));
        }

        if (enemies != null && player != null) {
            enemies.stream()
                .filter(e -> e != null && e.isAlive())
                .forEach(e -> e.updateBehavior(this, player));
        }
        
        checkWaveCompletion();
    }

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Wall> getWalls() { return walls != null ? walls : new ArrayList<>(); }
    public List<IceBlock> getIceBlocks() { return iceBlocks != null ? iceBlocks : new ArrayList<>(); }
    public List<Fruit> getFruits() { return fruits != null ? fruits : new ArrayList<>(); }
    public List<Enemy> getEnemies() { return enemies != null ? enemies : new ArrayList<>(); }
    public List<Campfire> getCampfires() { return campfires != null ? campfires : new ArrayList<>(); }
    public List<HotTile> getHotTiles() { return hotTiles != null ? hotTiles : new ArrayList<>(); }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer2() { return player2; }
    public void setPlayer2(Player player2) { this.player2 = player2; }
    public FruitWaveManager getWaveManager() { return waveManager; }
}