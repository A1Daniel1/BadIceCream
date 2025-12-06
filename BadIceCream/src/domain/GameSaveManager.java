package domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de guardado y carga de partidas del juego.
 * Maneja la serialización y deserialización del estado del juego.
 */
public class GameSaveManager {
    
    private GameSaveManager() {
        // Constructor privado para clase de utilidad
    }

    /**
     * Guarda el estado completo del juego en un archivo binario.
     * 
     * @param game El juego a guardar.
     * @param file El archivo donde se guardará.
     * @throws BadIceCreamException Si ocurre un error al guardar.
     */
    public static void save(Game game, File file) throws BadIceCreamException {
        if (game == null) {
            throw new BadIceCreamException("No hay juego para guardar");
        }
        
        if (game.getState() == GameState.MENU) {
            throw new BadIceCreamException("No hay partida activa para guardar");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            GameData data = new GameData(game);
            oos.writeObject(data);
            BadIceCreamLog.log("Partida guardada exitosamente en: " + file.getAbsolutePath());
        } catch (IOException e) {
            BadIceCreamLog.log(e);
            throw new BadIceCreamException("Error al guardar la partida: " + e.getMessage(), e);
        }
    }

    /**
     * Carga un juego desde un archivo binario.
     * 
     * @param file El archivo a cargar.
     * @return El juego cargado.
     * @throws BadIceCreamException Si ocurre un error al cargar.
     */
    public static Game open(File file) throws BadIceCreamException {
        if (!file.exists()) {
            throw new BadIceCreamException("El archivo no existe: " + file.getAbsolutePath());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            GameData data = (GameData) ois.readObject();
            Game game = data.toGame();
            BadIceCreamLog.log("Partida cargada exitosamente desde: " + file.getAbsolutePath());
            return game;
        } catch (IOException | ClassNotFoundException e) {
            BadIceCreamLog.log(e);
            throw new BadIceCreamException("Error al cargar la partida: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta el nivel actual a un archivo de texto legible.
     * 
     * @param game El juego a exportar.
     * @param file El archivo donde se exportará.
     * @throws BadIceCreamException Si ocurre un error al exportar.
     */
    public static void export(Game game, File file) throws BadIceCreamException {
        if (game == null || game.getLevel() == null) {
            throw new BadIceCreamException("No hay nivel para exportar");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            Level level = game.getLevel();
            Player player = game.getPlayer();

            // Escribir encabezado
            writer.println("# Bad Dopo-Cream Level Export");
            writer.println("# Level: " + level.levelNumber);
            writer.println("# Dimensions: " + level.getWidth() + "x" + level.getHeight());
            writer.println();

            // Escribir configuración del jugador
            if (player != null) {
                writer.println("[PLAYER]");
                writer.println("flavor=" + player.getFlavor().name());
                writer.println("position=" + player.getPosition().getX() + "," + player.getPosition().getY());
                writer.println("score=" + player.getScore());
                writer.println("alive=" + player.isAlive());
                writer.println();
            }

            // Escribir estado del juego
            writer.println("[GAME]");
            writer.println("state=" + game.getState().name());
            writer.println("time=" + game.getTimeRemaining());
            writer.println();

            // Escribir mapa
            writer.println("[MAP]");
            for (int y = 0; y < level.getHeight(); y++) {
                for (int x = 0; x < level.getWidth(); x++) {
                    Position pos = new Position(x, y);
                    writer.print(getCellSymbol(pos, level, player));
                }
                writer.println();
            }
            writer.println();

            // Escribir frutas
            writer.println("[FRUITS]");
            for (Fruit fruit : level.getFruits()) {
                writer.println(fruit.getFruitType().name() + "," + 
                             fruit.getPosition().getX() + "," + 
                             fruit.getPosition().getY() + "," + 
                             fruit.isCollected());
            }
            writer.println();

            // Escribir enemigos
            writer.println("[ENEMIES]");
            for (Enemy enemy : level.getEnemies()) {
                String className = enemy.getClass().getSimpleName();
                writer.println(className + "," + 
                             enemy.getPosition().getX() + "," + 
                             enemy.getPosition().getY() + "," + 
                             enemy.isAlive());
            }
            writer.println();

            // Escribir fogatas
            if (!level.getCampfires().isEmpty()) {
                writer.println("[CAMPFIRES]");
                for (Campfire fire : level.getCampfires()) {
                    writer.println(fire.getPosition().getX() + "," + 
                                 fire.getPosition().getY() + "," + 
                                 fire.isLit());
                }
                writer.println();
            }

            // Escribir baldosas calientes
            if (!level.getHotTiles().isEmpty()) {
                writer.println("[HOTTILES]");
                for (HotTile tile : level.getHotTiles()) {
                    writer.println(tile.getPosition().getX() + "," + 
                                 tile.getPosition().getY());
                }
                writer.println();
            }

            BadIceCreamLog.log("Nivel exportado exitosamente a: " + file.getAbsolutePath());
        } catch (IOException e) {
            BadIceCreamLog.log(e);
            throw new BadIceCreamException("Error al exportar el nivel: " + e.getMessage(), e);
        }
    }

    /**
     * Importa un nivel desde un archivo de texto.
     * 
     * @param file El archivo a importar.
     * @return Un nuevo juego con el nivel importado.
     * @throws BadIceCreamException Si ocurre un error al importar.
     */
    public static Game importLevel(File file) throws BadIceCreamException {
        if (!file.exists()) {
            throw new BadIceCreamException("El archivo no existe: " + file.getAbsolutePath());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Game game = new Game();
            String line;
            String section = "";
            
            IceCreamFlavour flavor = IceCreamFlavour.VANILLA;
            Position playerPos = new Position(1, 1);
            int score = 0;
            boolean playerAlive = true;
            GameState state = GameState.PLAYING;
            int time = 180;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Ignorar comentarios y líneas vacías
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Identificar secciones
                if (line.startsWith("[") && line.endsWith("]")) {
                    section = line.substring(1, line.length() - 1);
                    continue;
                }

                // Procesar según la sección
                switch (section) {
                    case "PLAYER":
                        if (line.startsWith("flavor=")) {
                            flavor = IceCreamFlavour.valueOf(line.substring(7));
                        } else if (line.startsWith("position=")) {
                            String[] coords = line.substring(9).split(",");
                            playerPos = new Position(Integer.parseInt(coords[0]), 
                                                    Integer.parseInt(coords[1]));
                        } else if (line.startsWith("score=")) {
                            score = Integer.parseInt(line.substring(6));
                        } else if (line.startsWith("alive=")) {
                            playerAlive = Boolean.parseBoolean(line.substring(6));
                        }
                        break;

                    case "GAME":
                        if (line.startsWith("state=")) {
                            state = GameState.valueOf(line.substring(6));
                        } else if (line.startsWith("time=")) {
                            time = Integer.parseInt(line.substring(5));
                        }
                        break;
                }
            }

            // Crear el juego con la configuración importada
            game.startGame(1, flavor);
            Player player = game.getPlayer();
            if (player != null) {
                player.setPosition(playerPos);
                player.addScore(score);
                if (!playerAlive) {
                    player.die();
                }
            }

            BadIceCreamLog.log("Nivel importado exitosamente desde: " + file.getAbsolutePath());
            return game;

        } catch (IOException | NumberFormatException e) {
            BadIceCreamLog.log(e);
            throw new BadIceCreamException("Error al importar el nivel: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el símbolo de una celda para exportación.
     */
    private static char getCellSymbol(Position pos, Level level, Player player) {
        if (player != null && player.getPosition().equals(pos)) {
            return 'P';
        }
        
        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isAlive() && enemy.getPosition().equals(pos)) {
                return 'E';
            }
        }
        
        for (Fruit fruit : level.getFruits()) {
            if (!fruit.isCollected() && fruit.getPosition().equals(pos)) {
                return 'F';
            }
        }
        
        if (level.isWall(pos)) {
            return '#';
        }
        
        if (level.isIceBlock(pos)) {
            return 'I';
        }
        
        if (level.isCampfire(pos)) {
            Campfire fire = level.getCampfireAt(pos);
            return fire != null && fire.isLit() ? '*' : 'o';
        }
        
        if (level.isHotTile(pos)) {
            return 'H';
        }
        
        return '.';
    }

    /**
     * Clase interna para serializar el estado del juego.
     */
    private static class GameData implements Serializable {
        private static final long serialVersionUID = 1L;

        private int levelNumber;
        private IceCreamFlavour playerFlavor;
        private Position playerPosition;
        private Direction playerDirection;
        private int playerScore;
        private boolean playerAlive;
        private GameState gameState;
        private int timeRemaining;

        // Listas serializables
        private List<SerializableWall> walls;
        private List<SerializableIceBlock> iceBlocks;
        private List<SerializableFruit> fruits;
        private List<SerializableEnemy> enemies;
        private List<SerializableCampfire> campfires;
        private List<SerializableHotTile> hotTiles;

        public GameData(Game game) {
            Level level = game.getLevel();
            Player player = game.getPlayer();

            this.levelNumber = level.levelNumber;
            this.playerFlavor = player.getFlavor();
            this.playerPosition = player.getPosition();
            this.playerDirection = player.getDirection();
            this.playerScore = player.getScore();
            this.playerAlive = player.isAlive();
            this.gameState = game.getState();
            this.timeRemaining = game.getTimeRemaining();

            // Convertir objetos a formas serializables
            this.walls = new ArrayList<>();
            for (Wall wall : level.getWalls()) {
                walls.add(new SerializableWall(wall.getPosition()));
            }

            this.iceBlocks = new ArrayList<>();
            for (IceBlock block : level.getIceBlocks()) {
                iceBlocks.add(new SerializableIceBlock(block.getPosition(), block.isDestroyed()));
            }

            this.fruits = new ArrayList<>();
            for (Fruit fruit : level.getFruits()) {
                SerializableFruit sf = new SerializableFruit(
                    fruit.getFruitType(),
                    fruit.getPosition(),
                    fruit.isCollected()
                );
                
                // Guardar datos específicos de Cactus
                if (fruit instanceof Cactus) {
                    Cactus cactus = (Cactus) fruit;
                    sf.hasThorns = cactus.hasThorns();
                    sf.lastStateChange = System.currentTimeMillis() - cactus.getTimeUntilStateChange();
                }
                
                fruits.add(sf);
            }

            this.enemies = new ArrayList<>();
            for (Enemy enemy : level.getEnemies()) {
                enemies.add(new SerializableEnemy(
                    enemy.getClass().getSimpleName(),
                    enemy.getEnemyType(),
                    enemy.getPosition(),
                    enemy.getDirection(),
                    enemy.isAlive()
                ));
            }

            this.campfires = new ArrayList<>();
            for (Campfire fire : level.getCampfires()) {
                campfires.add(new SerializableCampfire(fire.getPosition(), fire.isLit()));
            }

            this.hotTiles = new ArrayList<>();
            for (HotTile tile : level.getHotTiles()) {
                hotTiles.add(new SerializableHotTile(tile.getPosition()));
            }
        }

        public Game toGame() {
            Game game = new Game();
            game.startGame(levelNumber, playerFlavor);

            Level level = game.getLevel();
            Player player = game.getPlayer();

            // Restaurar jugador
            player.setPosition(playerPosition);
            player.setDirection(playerDirection);
            player.addScore(playerScore - player.getScore());
            if (!playerAlive) {
                player.die();
            }

            // Restaurar bloques de hielo
            level.getIceBlocks().clear();
            for (SerializableIceBlock sib : iceBlocks) {
                IceBlock block = new IceBlock(sib.position);
                if (sib.destroyed) {
                    block.destroy();
                }
                level.getIceBlocks().add(block);
            }

            // Restaurar frutas
            level.getFruits().clear();
            for (SerializableFruit sf : fruits) {
                Fruit fruit = createFruit(sf.type, sf.position);
                if (fruit != null) {
                    if (sf.collected) {
                        fruit.collect();
                    }
                    
                    // Restaurar datos específicos de Cactus
                    if (fruit instanceof Cactus && sf.hasThorns != null) {
                        Cactus cactus = (Cactus) fruit;
                        cactus.setHasThorns(sf.hasThorns);
                        if (sf.lastStateChange != null) {
                            cactus.setLastStateChange(sf.lastStateChange);
                        }
                    }
                    
                    level.getFruits().add(fruit);
                }
            }

            // Restaurar enemigos
            level.getEnemies().clear();
            for (SerializableEnemy se : enemies) {
                Enemy enemy = createEnemy(se.className, se.position);
                if (enemy != null) {
                    enemy.setDirection(se.direction);
                    if (!se.alive) {
                        enemy.die();
                    }
                    level.getEnemies().add(enemy);
                }
            }

            // Restaurar fogatas
            level.getCampfires().clear();
            for (SerializableCampfire sc : campfires) {
                Campfire fire = new Campfire(sc.position);
                if (!sc.lit) {
                    fire.extinguish();
                }
                level.getCampfires().add(fire);
            }

            // Restaurar baldosas calientes
            level.getHotTiles().clear();
            for (SerializableHotTile st : hotTiles) {
                level.getHotTiles().add(new HotTile(st.position));
            }

            return game;
        }

        private Fruit createFruit(FruitType type, Position position) {
            switch (type) {
                case GRAPE: return new Grape(position);
                case BANANA: return new Banana(position);
                case PINEAPPLE: return new Pineapple(position);
                case CHERRY: return new Cherry(position);
                case CACTUS: return new Cactus(position);
                default: return null;
            }
        }

        private Enemy createEnemy(String className, Position position) {
            switch (className) {
                case "Troll": return new Troll(position);
                case "Pot": return new Pot(position);
                case "OrangeSquid": return new OrangeSquid(position);
                case "Narwhal": return new Narwhal(position);
                default: return null;
            }
        }
    }

    // Clases serializables internas
    private static class SerializableWall implements Serializable {
        private static final long serialVersionUID = 1L;
        Position position;
        SerializableWall(Position position) { this.position = position; }
    }

    private static class SerializableIceBlock implements Serializable {
        private static final long serialVersionUID = 1L;
        Position position;
        boolean destroyed;
        SerializableIceBlock(Position position, boolean destroyed) {
            this.position = position;
            this.destroyed = destroyed;
        }
    }

    private static class SerializableFruit implements Serializable {
        private static final long serialVersionUID = 1L;
        FruitType type;
        Position position;
        boolean collected;
        Boolean hasThorns; // Para Cactus
        Long lastStateChange; // Para Cactus
        
        SerializableFruit(FruitType type, Position position, boolean collected) {
            this.type = type;
            this.position = position;
            this.collected = collected;
        }
    }

    private static class SerializableEnemy implements Serializable {
        private static final long serialVersionUID = 1L;
        String className;
        EnemyType enemyType;
        Position position;
        Direction direction;
        boolean alive;
        
        SerializableEnemy(String className, EnemyType enemyType, Position position, 
                         Direction direction, boolean alive) {
            this.className = className;
            this.enemyType = enemyType;
            this.position = position;
            this.direction = direction;
            this.alive = alive;
        }
    }

    private static class SerializableCampfire implements Serializable {
        private static final long serialVersionUID = 1L;
        Position position;
        boolean lit;
        SerializableCampfire(Position position, boolean lit) {
            this.position = position;
            this.lit = lit;
        }
    }

    private static class SerializableHotTile implements Serializable {
        private static final long serialVersionUID = 1L;
        Position position;
        SerializableHotTile(Position position) { this.position = position; }
    }
}