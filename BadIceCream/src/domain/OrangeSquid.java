package domain;

/**
 * Calamar Naranja - Persigue al jugador y destruye bloques de hielo uno a la vez
 */
public class OrangeSquid extends Enemy {
    private int moveCounter;
    private static final int MOVE_DELAY = 6;
    private boolean breakingIce;
    private int breakCounter;
    private static final int BREAK_DELAY = 15; // Tiempo para romper un bloque
    
    public OrangeSquid(Position position) {
        super(position, EnemyType.ORANGE_SQUID);
        this.moveCounter = 0;
        this.breakingIce = false;
        this.breakCounter = 0;
    }
    
    @Override
    public Position getNextPosition(Level level, Player player) {
        if (player == null || !player.isAlive()) {
            return position;
        }
        
        Position playerPos = player.getPosition();
        int currentX = position.getX();
        int currentY = position.getY();
        int targetX = playerPos.getX();
        int targetY = playerPos.getY();
        
        int deltaX = targetX - currentX;
        int deltaY = targetY - currentY;
        
        Position nextPos = null;
        
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                nextPos = position.add(Direction.RIGHT);
            } else if (deltaX < 0) {
                nextPos = position.add(Direction.LEFT);
            }
            
            if (nextPos == null || !isValidPosition(nextPos, level)) {
                if (deltaY > 0) {
                    nextPos = position.add(Direction.DOWN);
                } else if (deltaY < 0) {
                    nextPos = position.add(Direction.UP);
                }
            }
        } else {
            if (deltaY > 0) {
                nextPos = position.add(Direction.DOWN);
            } else if (deltaY < 0) {
                nextPos = position.add(Direction.UP);
            }
            
            if (nextPos == null || !isValidPosition(nextPos, level)) {
                if (deltaX > 0) {
                    nextPos = position.add(Direction.RIGHT);
                } else if (deltaX < 0) {
                    nextPos = position.add(Direction.LEFT);
                }
            }
        }
        
        if (nextPos == null || !isValidPosition(nextPos, level)) {
            return position;
        }
        
        return nextPos;
    }
    
    private boolean isValidPosition(Position pos, Level level) {
        if (pos.getX() < 1 || pos.getX() >= level.getWidth() - 1 ||
            pos.getY() < 1 || pos.getY() >= level.getHeight() - 1) {
            return false;
        }
        
        if (!level.canMoveTo(pos)) {
            return false;
        }
        
        // El calamar NO puede pasar por hielo (lo destruye primero)
        if (level.isIceBlock(pos)) {
            return false;
        }
        
        if (level.getEnemies() != null) {
            for (Enemy enemy : level.getEnemies()) {
                if (enemy != null && enemy != this && 
                    enemy.isAlive() && enemy.getPosition().equals(pos)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void updateBehavior(Level level, Player player) {
        if (breakingIce) {
            // Está rompiendo un bloque de hielo
            breakCounter++;
            if (breakCounter >= BREAK_DELAY) {
                breakingIce = false;
                breakCounter = 0;
            }
            return; // No se mueve mientras rompe hielo
        }
        
        moveCounter++;
        if (moveCounter >= MOVE_DELAY) {
            moveCounter = 0;
            Position nextPos = getNextPosition(level, player);
            
            // Verificar si hay hielo en el camino
            if (level.isIceBlock(nextPos)) {
                // Comenzar a romper el hielo
                IceBlock block = level.getIceBlockAt(nextPos);
                if (block != null) {
                    block.destroy();
                    breakingIce = true;
                    breakCounter = 0;
                }
            } else if (!nextPos.equals(position) && isValidPosition(nextPos, level)) {
                move(nextPos);
            }
        }
    }
    
    @Override
    public boolean canMoveTo(Position position, Level level) {
        return isValidPosition(position, level);
    }
    
    @Override
    public String getSymbol() {
        return "S"; // S de Squid
    }
}