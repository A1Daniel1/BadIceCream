package domain;

/**
 * Clase principal que gestiona la lógica y el estado del juego.
 * Controla el bucle de juego, las actualizaciones de estado, colisiones y
 * condiciones de victoria/derrota.
 */
public class Game {
    private static Level level;
    private static Player player;
    private static GameState state;
    private static int timeRemaining;
    private static final int MAX_TIME = 180;
    private static int updateCounter;
    private static final int UPDATES_PER_SECOND = 10;

    /**
     * Constructor de la clase Game.
     * Inicializa el estado del juego en el menú principal.
     */
    public Game() {
        Game.state = GameState.MENU;
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
    }

    /**
     * Inicia una nueva partida con el nivel y sabor de helado seleccionados.
     * 
     * @param levelNumber El número del nivel a jugar.
     * @param flavor      El sabor del helado elegido por el jugador.
     */
    public void startGame(int levelNumber, IceCreamFlavour flavor) {
        Game.level = new Level(levelNumber);
        Game.player = new Player(new Position(1, 1), flavor);
        Game.level.setPlayer(player);
        Game.state = GameState.PLAYING;
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
    }

    /**
     * Intenta mover al jugador en la dirección especificada.
     * Gestiona la recolección de frutas y la verificación de victoria.
     * 
     * @param direction La dirección hacia la cual mover al jugador.
     */
    public void movePlayer(Direction direction) {
        if (state != GameState.PLAYING || !player.isAlive())
            return;

        player.setDirection(direction);
        Position newPos = player.getPosition().add(direction);

        if (level.canMoveTo(newPos)) {
            player.move(newPos);

            // Recoger fruta
            Fruit fruit = level.getFruitAt(newPos);
            if (fruit != null) {
                fruit.collect();
                player.addScore(fruit.getPoints());
            }

            // Verificar victoria
            if (level.allFruitsCollected()) {
                state = GameState.VICTORY;
            }
        }
    }

    /**
     * Ordena al jugador crear un bloque de hielo.
     */
    public void createIceBlock() {
        if (state == GameState.PLAYING) {
            player.createIceBlock(level);
        }
    }

    /**
     * Ordena al jugador destruir una línea de bloques de hielo.
     */
    public void destroyIceBlock() {
        if (state == GameState.PLAYING) {
            player.destroyIceBlock(level);
        }
    }

    /**
     * Actualiza la lógica del juego (enemigos, tiempo, colisiones).
     * Se llama periódicamente desde el bucle principal.
     */
    public static void update() {
        if (state != GameState.PLAYING)
            return;

        level.update();
        checkCollisions();

        updateCounter++;
        if (updateCounter >= UPDATES_PER_SECOND) {
            updateCounter = 0;
            updateTime();
        }
    }

    /**
     * Verifica colisiones entre el jugador y los enemigos.
     * Si hay colisión, el jugador muere y el juego termina.
     */
    private static void checkCollisions() {
        if (!player.isAlive())
            return;

        Position playerPos = player.getPosition();

        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isAlive() && enemy.getPosition().equals(playerPos)) {
                player.die();
                state = GameState.GAME_OVER;
                return;
            }
        }
    }

    /**
     * Actualiza el temporizador del juego.
     * Si el tiempo llega a cero, el juego termina.
     */
    private static void updateTime() {
        timeRemaining--;
        if (timeRemaining <= 0) {
            state = GameState.GAME_OVER;
        }
    }

    /**
     * Pausa el juego si está en curso.
     */
    public void pause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
        }
    }

    /**
     * Reanuda el juego si está pausado.
     */
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Reinicia el nivel actual con el mismo jugador.
     */
    public void reset() {
        startGame(level.levelNumber, player.getFlavor());
    }

    // Getters

    /**
     * Obtiene el estado actual del juego.
     * 
     * @return El estado del juego (PLAYING, MENU, GAME_OVER, etc.).
     */
    public GameState getState() {
        return state;
    }

    /**
     * Obtiene el nivel actual.
     * 
     * @return El objeto Level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Obtiene el jugador actual.
     * 
     * @return El objeto Player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Obtiene el tiempo restante de la partida.
     * 
     * @return El tiempo en segundos (o unidades de juego).
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }
}