package domain;

import java.io.File;

public class Game {
    private static Level level;
    private static Player player;
    private static Player player2; // Para PvsP y PvsM
    private static AIPlayer aiPlayer1; // Para MvsM
    private static AIPlayer aiPlayer2; // Para MvsM
    private static GameMode gameMode;
    private static AIProfile aiProfile1;
    private static AIProfile aiProfile2;
    private static GameState state;
    private static int timeRemaining;
    private static final int MAX_TIME = 180;
    private static int updateCounter;
    private static final int UPDATES_PER_SECOND = 10;
    
    public Game() {
        Game.state = GameState.MENU;
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
        Game.gameMode = GameMode.PLAYER;
    }
    
    /**
     * Inicia juego en modo Player (un solo jugador)
     */
    public void startGame(int levelNumber, IceCreamFlavour flavor) {
        Game.gameMode = GameMode.PLAYER;
        Game.level = new Level(levelNumber);
        Game.player = new Player(new Position(1, 1), flavor);
        Game.level.setPlayer(player);
        Game.player2 = null;
        Game.aiPlayer1 = null;
        Game.aiPlayer2 = null;
        Game.state = GameState.PLAYING;
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
    }
    
    /**
     * Inicia juego con modalidad específica
     */
    public void startGame(int levelNumber, IceCreamFlavour flavor, GameMode mode, 
                         AIProfile profile1, AIProfile profile2) {
        Game.gameMode = mode;
        Game.level = new Level(levelNumber);
        Game.timeRemaining = MAX_TIME;
        Game.updateCounter = 0;
        Game.state = GameState.PLAYING;
        Game.aiProfile1 = profile1;
        Game.aiProfile2 = profile2;
        
        switch (mode) {
            case PLAYER:
                initPlayerMode(flavor);
                break;
            case PLAYER_VS_PLAYER:
                initPlayerVsPlayerMode(flavor);
                break;
            case PLAYER_VS_MACHINE:
                initPlayerVsMachineMode(flavor, profile1);
                break;
            case MACHINE_VS_MACHINE:
                initMachineVsMachineMode(profile1, profile2);
                break;
        }
    }
    
    private void initPlayerMode(IceCreamFlavour flavor) {
        Game.player = new Player(new Position(1, 1), flavor);
        Game.level.setPlayer(player);
        Game.player2 = null;
        Game.aiPlayer1 = null;
        Game.aiPlayer2 = null;
    }
    
    private void initPlayerVsPlayerMode(IceCreamFlavour flavor) {
        Game.player = new Player(new Position(1, 1), flavor);
        IceCreamFlavour flavor2 = (flavor == IceCreamFlavour.VANILLA) ? 
                                  IceCreamFlavour.STRAWBERRY : IceCreamFlavour.VANILLA;
        Game.player2 = new Player(new Position(13, 9), flavor2);
        Game.level.setPlayer(player);
        Game.level.setPlayer2(player2);
        Game.aiPlayer1 = null;
        Game.aiPlayer2 = null;
    }
    
    private void initPlayerVsMachineMode(IceCreamFlavour flavor, AIProfile aiProfile) {
        Game.aiProfile1 = aiProfile != null ? aiProfile : AIProfile.EXPERT;
        Game.player = new Player(new Position(1, 1), flavor);
        IceCreamFlavour aiFlavor = (flavor == IceCreamFlavour.VANILLA) ? 
                                   IceCreamFlavour.CHOCOLATE : IceCreamFlavour.VANILLA;
        Game.aiPlayer1 = new AIPlayer(new Position(13, 9), aiFlavor, Game.aiProfile1);
        Game.player2 = aiPlayer1; // Para compatibilidad
        Game.level.setPlayer(player);
        Game.level.setPlayer2(aiPlayer1);
        Game.aiPlayer2 = null;
    }
    
    private void initMachineVsMachineMode(AIProfile profile1, AIProfile profile2) {
        Game.aiProfile1 = profile1 != null ? profile1 : AIProfile.EXPERT;
        Game.aiProfile2 = profile2 != null ? profile2 : AIProfile.HUNGRY;
        
        Game.aiPlayer1 = new AIPlayer(new Position(1, 1), IceCreamFlavour.VANILLA, Game.aiProfile1);
        Game.aiPlayer2 = new AIPlayer(new Position(13, 9), IceCreamFlavour.CHOCOLATE, Game.aiProfile2);
        
        Game.player = aiPlayer1; // Para compatibilidad con el sistema actual
        Game.player2 = aiPlayer2;
        Game.level.setPlayer(aiPlayer1);
        Game.level.setPlayer2(aiPlayer2);
    }
    
    public void movePlayer(Direction direction) {
        if (state != GameState.PLAYING) return;
        
        // Solo mover si es modo jugador y el jugador está vivo
        if (gameMode == GameMode.PLAYER || gameMode == GameMode.PLAYER_VS_PLAYER || 
            gameMode == GameMode.PLAYER_VS_MACHINE) {
            if (player != null && player.isAlive() && !(player instanceof AIPlayer)) {
                movePlayerInternal(player, direction);
            }
        }
    }
    
    public void movePlayer2(Direction direction) {
        if (state != GameState.PLAYING) return;
        
        // Solo en modo PvsP puede el jugador 2 moverse manualmente
        if (gameMode == GameMode.PLAYER_VS_PLAYER) {
            if (player2 != null && player2.isAlive() && !(player2 instanceof AIPlayer)) {
                movePlayerInternal(player2, direction);
            }
        }
    }
    
    private void movePlayerInternal(Player p, Direction direction) {
        p.setDirection(direction);
        Position newPos = p.getPosition().add(direction);
        
        if (level.canMoveTo(newPos)) {
            p.move(newPos);
            
            // Verificar fogata INMEDIATAMENTE después de moverse
            if (level.isDeadlyCampfire(newPos)) {
                p.die();
                checkGameOver();
                forceRepaint();
                return;
            }
            
            Fruit fruit = level.getFruitAt(newPos);
            if (fruit != null && !fruit.isCollected()) {
                fruit.collect();
                p.addScore(fruit.getPoints());
            }
            
            if (level.allFruitsCollected()) {
                checkVictory();
            }
        }
    }
    
    public void createIceBlock() {
        if (state == GameState.PLAYING && player != null && player.isAlive()) {
            player.createIceBlock(level);
        }
    }
    
    public void createIceBlockPlayer2() {
        if (state == GameState.PLAYING && gameMode == GameMode.PLAYER_VS_PLAYER && 
            player2 != null && player2.isAlive()) {
            player2.createIceBlock(level);
        }
    }
    
    public void destroyIceBlock() {
        if (state == GameState.PLAYING && player != null && player.isAlive()) {
            player.destroyIceBlock(level);
        }
    }
    
    public void destroyIceBlockPlayer2() {
        if (state == GameState.PLAYING && gameMode == GameMode.PLAYER_VS_PLAYER && 
            player2 != null && player2.isAlive()) {
            player2.destroyIceBlock(level);
        }
    }
    
    public static void update() {
        if (state != GameState.PLAYING) return;
        
        // Actualizar IA
        updateAI();
        
        // Actualizar nivel
        level.update();
        checkCollisions();
        
        updateCounter++;
        if (updateCounter >= UPDATES_PER_SECOND) {
            updateCounter = 0;
            updateTime();
        }
    }
    
    private static void updateAI() {
        if (aiPlayer1 != null && aiPlayer1.isAlive()) {
            AIAction action = aiPlayer1.decideAction(level);
            executeAIAction(aiPlayer1, action);
        }
        
        if (aiPlayer2 != null && aiPlayer2.isAlive()) {
            AIAction action = aiPlayer2.decideAction(level);
            executeAIAction(aiPlayer2, action);
        }
    }
    
    private static void executeAIAction(AIPlayer aiPlayer, AIAction action) {
        switch (action.getType()) {
            case MOVE:
                Direction dir = action.getDirection();
                aiPlayer.setDirection(dir);
                Position newPos = aiPlayer.getPosition().add(dir);
                
                if (level.canMoveTo(newPos)) {
                    aiPlayer.move(newPos);
                    
                    // Verificar fogata
                    if (level.isDeadlyCampfire(newPos)) {
                        aiPlayer.die();
                        checkGameOver();
                        forceRepaint();
                        return;
                    }
                    
                    // Recoger fruta
                    Fruit fruit = level.getFruitAt(newPos);
                    if (fruit != null && !fruit.isCollected()) {
                        fruit.collect();
                        aiPlayer.addScore(fruit.getPoints());
                    }
                    
                    // Verificar victoria
                    if (level.allFruitsCollected()) {
                        checkVictory();
                    }
                }
                break;
            case CREATE_ICE:
                aiPlayer.setDirection(action.getDirection());
                aiPlayer.createIceBlock(level);
                break;
            case DESTROY_ICE:
                aiPlayer.setDirection(action.getDirection());
                aiPlayer.destroyIceBlock(level);
                break;
            case WAIT:
                // No hacer nada
                break;
        }
    }
    
    private static void checkCollisions() {
        checkPlayerCollisions(player);
        if (player2 != null) {
            checkPlayerCollisions(player2);
        }
    }
    
    private static void checkPlayerCollisions(Player p) {
        if (p == null || !p.isAlive() || state != GameState.PLAYING) {
            return;
        }
        
        Position playerPos = p.getPosition();
        
        // Verificar colisión con fogatas - PRIORIDAD MÁXIMA
        if (level.isDeadlyCampfire(playerPos)) {
            p.die();
            checkGameOver();
            forceRepaint();
            return;
        }
        
        // Verificar colisión con enemigos
        if (level.getEnemies() != null) {
            for (Enemy enemy : level.getEnemies()) {
                if (enemy != null && enemy.isAlive() && 
                    enemy.getPosition() != null && 
                    enemy.getPosition().equals(playerPos)) {
                    p.die();
                    checkGameOver();
                    forceRepaint();
                    return;
                }
            }
        }
    }
    
    private static void checkGameOver() {
        if (gameMode == GameMode.PLAYER) {
            // En modo single player, termina cuando el jugador muere
            if (player != null && !player.isAlive()) {
                state = GameState.GAME_OVER;
            }
        } else if (gameMode.isMultiplayer()) {
            // En modo multijugador, verificar si ambos jugadores existen
            if (player == null || player2 == null) {
                return; // No verificar si faltan jugadores
            }
            
            // Solo termina cuando AMBOS están muertos
            if (!player.isAlive() && !player2.isAlive()) {
                state = GameState.GAME_OVER;
            }
            // Si al menos uno está vivo, el juego continúa
        }
    }
    
    private static void checkVictory() {
        if (level != null && level.allFruitsCollected()) {
            state = GameState.VICTORY;
        }
    }
    
    private static void updateTime() {
        timeRemaining--;
        if (timeRemaining <= 0) {
            state = GameState.GAME_OVER;
        }
    }
    
    private static void forceRepaint() {
        try {
            Class<?> boardClass = Class.forName("presentation.GameBoardPanel");
            java.lang.reflect.Method method = boardClass.getMethod("forceRepaint");
            method.invoke(null);
        } catch (Exception e) {
            // Ignorar
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
        if (gameMode.isMultiplayer()) {
            startGame(level.levelNumber, player.getFlavor(), gameMode, aiProfile1, aiProfile2);
        } else {
            startGame(level.levelNumber, player.getFlavor());
        }
    }
    
    // Getters
    public GameState getState() { return state; }
    public Level getLevel() { return level; }
    public Player getPlayer() { return player; }
    public Player getPlayer2() { return player2; }
    public int getTimeRemaining() { return timeRemaining; }
    public GameMode getGameMode() { return gameMode; }
    public AIPlayer getAIPlayer1() { return aiPlayer1; }
    public AIPlayer getAIPlayer2() { return aiPlayer2; }

    // Métodos de guardado/cargado
    public void save(File file) throws BadIceCreamException {
        GameSaveManager.save(this, file);
    }

    public static Game open(File file) throws BadIceCreamException {
        return GameSaveManager.open(file);
    }

    public void export(File file) throws BadIceCreamException {
        GameSaveManager.export(this, file);
    }

    public static Game importLevel(File file) throws BadIceCreamException {
        return GameSaveManager.importLevel(file);
    }
}