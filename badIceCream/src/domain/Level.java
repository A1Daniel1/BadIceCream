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
    private Player player;
    
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.width = 15;
        this.height = 11;
        this.walls = new ArrayList<>();
        this.iceBlocks = new ArrayList<>();
        this.fruits = new ArrayList<>();
        this.enemies = new ArrayList<>();
        initializeLevel();
    }
    
    private void initializeLevel() {
        createBorderWalls();
        
        switch(levelNumber) {
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
        
        // 8 Uvas
        fruits.add(new Grape(new Position(2, 2)));
        fruits.add(new Grape(new Position(4, 2)));
        fruits.add(new Grape(new Position(6, 2)));
        fruits.add(new Grape(new Position(8, 2)));
        fruits.add(new Grape(new Position(10, 2)));
        fruits.add(new Grape(new Position(12, 2)));
        fruits.add(new Grape(new Position(13, 3)));
        fruits.add(new Grape(new Position(13, 5)));
        
        // 8 Plátanos
        fruits.add(new Banana(new Position(2, 9)));
        fruits.add(new Banana(new Position(4, 9)));
        fruits.add(new Banana(new Position(6, 9)));
        fruits.add(new Banana(new Position(8, 9)));
        fruits.add(new Banana(new Position(10, 9)));
        fruits.add(new Banana(new Position(12, 9)));
        fruits.add(new Banana(new Position(1, 7)));
        fruits.add(new Banana(new Position(1, 5)));
        
        // 2 Trolls
        enemies.add(new Troll(new Position(13, 1)));
        enemies.add(new Troll(new Position(1, 9)));
    }
    
    private void createLevel2() {
        // Implementar nivel 2
        createLevel1(); // Por ahora usa el nivel 1
    }
    
    private void createLevel3() {
        // Implementar nivel 3
        createLevel1(); // Por ahora usa el nivel 1
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
        return canMoveTo(position);
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
    
    public void addIceBlock(IceBlock block) {
        if (block != null) {
            iceBlocks.add(block);
        }
    }
    
    public boolean allFruitsCollected() {
        if (fruits == null || fruits.isEmpty()) return false;
        return fruits.stream().allMatch(Fruit::isCollected);
    }
    
    public void update() {
        // Actualizar frutas móviles
        if (fruits != null) {
            fruits.stream()
                .filter(f -> f != null && !f.isCollected())
                .forEach(f -> f.update(this));
        }
        
        // Actualizar enemigos
        if (enemies != null && player != null) {
            enemies.stream()
                .filter(e -> e != null && e.isAlive())
                .forEach(e -> e.updateBehavior(this, player));
        }
    }
    
    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Wall> getWalls() { return walls != null ? walls : new ArrayList<>(); }
    public List<IceBlock> getIceBlocks() { return iceBlocks != null ? iceBlocks : new ArrayList<>(); }
    public List<Fruit> getFruits() { return fruits != null ? fruits : new ArrayList<>(); }
    public List<Enemy> getEnemies() { return enemies != null ? enemies : new ArrayList<>(); }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
}