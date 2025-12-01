package domain;

public class Game {
    private static Level level;
    private static Player player;
    private static GameState state;
    private static int timeRemaining;
    private static final int MAX_TIME = 180; // 3 minutos
    private static int updateCounter; // Contador para controlar actualizaciones del tiempo
    private static final int UPDATES_PER_SECOND = 10; // 10 actualizaciones por segundo (100ms cada una)
    
    public Game() {
        Game.state = GameState.MENU;
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
    }
    
    public void startGame(int levelNumber, IceCreamFlavour flavor) {
        Game.level = new Level(levelNumber);
        Game.player = new Player(new Position(1, 1), flavor);
        Game.level.setPlayer(player);
        Game.state = GameState.PLAYING;
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
    }
    
    public void movePlayer(Direction direction) {
        if (state != GameState.PLAYING || !player.isAlive()) return;
        
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
    
    public void createIceBlock() {
        if (state == GameState.PLAYING) {
            player.createIceBlock(level);
        }
    }
    
    public void destroyIceBlock() {
        if (state == GameState.PLAYING) {
            player.destroyIceBlock(level);
        }
    }
    
    public static void update() {
        if (state != GameState.PLAYING) return;
        
        // Actualizar lógica del juego (enemigos, frutas, etc.)
        level.update();
        checkCollisions();
        
        // Actualizar el tiempo solo cada segundo (60 actualizaciones)
        updateCounter++;
        if (updateCounter >= UPDATES_PER_SECOND) {
            updateCounter = 0;
            updateTime();
        }
    }
    
    private static void checkCollisions() {
        if (player == null || !player.isAlive()) return;
        
        Position playerPos = player.getPosition();
        
        if (level.getEnemies() != null) {
            for (Enemy enemy : level.getEnemies()) {
                if (enemy != null && enemy.isAlive() && 
                    enemy.getPosition() != null && 
                    enemy.getPosition().equals(playerPos)) {
                    player.die();
                    state = GameState.GAME_OVER;
                    return;
                }
            }
        }
    }
    
    private static void updateTime() {
        timeRemaining--;
        if (timeRemaining <= 0) {
            state = GameState.GAME_OVER;
        }
    }
    
    public void pause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
        }
    }
    
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
        }
    }
    
    public void reset() {
        startGame(level.levelNumber, player.getFlavor());
    }
    
    // Getters
    public GameState getState() { return state; }
    public Level getLevel() { return level; }
    public Player getPlayer() { return player; }
    public int getTimeRemaining() { return timeRemaining; }
}