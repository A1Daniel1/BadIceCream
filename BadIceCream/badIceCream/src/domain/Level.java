package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un nivel del juego.
 * Contiene toda la información sobre el estado actual del juego: paredes,
 * frutas, enemigos, bloques de hielo y el jugador.
 */
public class Level {
    int levelNumber;
    private int width;
    private int height;
    private List<Wall> walls;
    private List<IceBlock> iceBlocks;
    private List<Fruit> fruits;
    private List<Enemy> enemies;
    private Player player;

    /**
     * Constructor de la clase Level.
     * Inicializa las listas de objetos y carga la configuración del nivel
     * especificado.
     * 
     * @param levelNumber El número del nivel a cargar.
     */
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

    /**
     * Inicializa el contenido del nivel (paredes, frutas, enemigos) según el número
     * de nivel.
     */
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

    /**
     * Crea las paredes que delimitan el borde del tablero.
     */
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

        // 8 Uvas (50 puntos cada una)
        fruits.add(new Grape(new Position(2, 2)));
        fruits.add(new Grape(new Position(4, 2)));
        fruits.add(new Grape(new Position(6, 2)));
        fruits.add(new Grape(new Position(8, 2)));
        fruits.add(new Grape(new Position(10, 2)));
        fruits.add(new Grape(new Position(12, 2)));
        fruits.add(new Grape(new Position(13, 3)));
        fruits.add(new Grape(new Position(13, 5)));

        // 8 Plátanos (100 puntos cada uno)
        fruits.add(new Banana(new Position(2, 9)));
        fruits.add(new Banana(new Position(4, 9)));
        fruits.add(new Banana(new Position(6, 9)));
        fruits.add(new Banana(new Position(8, 9)));
        fruits.add(new Banana(new Position(10, 9)));
        fruits.add(new Banana(new Position(12, 9)));
        fruits.add(new Banana(new Position(1, 7)));
        fruits.add(new Banana(new Position(1, 5)));

        // 2 Trolls (patrullan el borde)
        enemies.add(new Troll(new Position(13, 1)));
        enemies.add(new Troll(new Position(1, 9)));

        // 1 Maceta (persigue al jugador)
        enemies.add(new Pot(new Position(7, 5)));
    }

    private void createLevel2() {
        // Implementar nivel 2
        createLevel1(); // Por ahora usa el nivel 1
    }

    private void createLevel3() {
        // Implementar nivel 3
        createLevel1(); // Por ahora usa el nivel 1
    }

    /**
     * Verifica si es posible moverse a una posición dada.
     * 
     * @param position La posición a verificar.
     * @return true si la posición está dentro de los límites y no es un obstáculo
     *         sólido.
     */
    public boolean canMoveTo(Position position) {
        if (position == null)
            return false;

        if (position.getX() < 0 || position.getX() >= width ||
                position.getY() < 0 || position.getY() >= height) {
            return false;
        }

        if (isWall(position))
            return false;
        if (isIceBlock(position))
            return false;

        return true;
    }

    /**
     * Verifica si se puede colocar un bloque de hielo en una posición.
     * 
     * @param position La posición a verificar.
     * @return true si se puede colocar un bloque de hielo.
     */
    public boolean canPlaceIceBlock(Position position) {
        return canMoveTo(position);
    }

    /**
     * Verifica si hay una pared en la posición dada.
     * 
     * @param position La posición a verificar.
     * @return true si hay una pared.
     */
    public boolean isWall(Position position) {
        if (position == null)
            return false;
        return walls.stream().anyMatch(wall -> wall != null && wall.getPosition().equals(position));
    }

    /**
     * Verifica si hay un bloque de hielo en la posición dada.
     * 
     * @param position La posición a verificar.
     * @return true si hay un bloque de hielo activo.
     */
    public boolean isIceBlock(Position position) {
        if (position == null)
            return false;
        return iceBlocks.stream()
                .anyMatch(block -> block != null && !block.isDestroyed() && block.getPosition().equals(position));
    }

    /**
     * Obtiene el bloque de hielo en una posición específica.
     * 
     * @param position La posición a buscar.
     * @return El bloque de hielo si existe, o null.
     */
    public IceBlock getIceBlockAt(Position position) {
        if (position == null)
            return null;
        return iceBlocks.stream()
                .filter(block -> block != null && !block.isDestroyed() && block.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene la fruta en una posición específica.
     * 
     * @param position La posición a buscar.
     * @return La fruta si existe y no ha sido recolectada, o null.
     */
    public Fruit getFruitAt(Position position) {
        if (position == null)
            return null;
        return fruits.stream()
                .filter(fruit -> fruit != null && !fruit.isCollected() && fruit.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica si hay un enemigo en la posición dada.
     * 
     * @param position La posición a verificar.
     * @return true si hay un enemigo vivo en esa posición.
     */
    public boolean hasEnemyAt(Position position) {
        if (position == null)
            return false;
        return enemies.stream()
                .anyMatch(enemy -> enemy != null && enemy.isAlive() && enemy.getPosition().equals(position));
    }

    /**
     * Añade un nuevo bloque de hielo al nivel.
     * 
     * @param block El bloque de hielo a añadir.
     */
    public void addIceBlock(IceBlock block) {
        if (block != null) {
            iceBlocks.add(block);
        }
    }

    /**
     * Verifica si todas las frutas del nivel han sido recolectadas.
     * 
     * @return true si no quedan frutas por recolectar.
     */
    public boolean allFruitsCollected() {
        return fruits.stream().allMatch(Fruit::isCollected);
    }

    /**
     * Actualiza el estado del nivel (frutas y enemigos).
     */
    public void update() {
        // Actualizar frutas móviles
        fruits.stream()
                .filter(f -> f != null && !f.isCollected())
                .forEach(f -> f.update(this));

        // Actualizar enemigos
        if (player != null) {
            enemies.stream()
                    .filter(e -> e != null && e.isAlive())
                    .forEach(e -> e.updateBehavior(this, player));
        }
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<IceBlock> getIceBlocks() {
        return iceBlocks;
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}