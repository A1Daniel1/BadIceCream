package domain;

public class Pot extends Enemy {
    private int moveCounter;
    private static final int MOVE_DELAY = 8; // Velocidad ajustada para ser esquivable
    
    public Pot(Position position) {
        super(position, EnemyType.POT);
        this.moveCounter = 0;
    }
    
    @Override
    public Position getNextPosition(Level level, Player player) {
        if (player == null || !player.isAlive()) {
            return position; // No moverse si no hay jugador
        }
        
        Position playerPos = player.getPosition();
        int currentX = position.getX();
        int currentY = position.getY();
        int targetX = playerPos.getX();
        int targetY = playerPos.getY();
        
        // Calcular diferencias
        int deltaX = targetX - currentX;
        int deltaY = targetY - currentY;
        
        // Intentar moverse primero en la dirección con mayor diferencia
        Position nextPos = null;
        
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Moverse horizontalmente primero
            if (deltaX > 0) {
                nextPos = position.add(Direction.RIGHT);
            } else if (deltaX < 0) {
                nextPos = position.add(Direction.LEFT);
            }
            
            // Si no puede moverse horizontalmente, intentar verticalmente
            if (nextPos == null || !isValidPosition(nextPos, level)) {
                if (deltaY > 0) {
                    nextPos = position.add(Direction.DOWN);
                } else if (deltaY < 0) {
                    nextPos = position.add(Direction.UP);
                }
            }
        } else {
            // Moverse verticalmente primero
            if (deltaY > 0) {
                nextPos = position.add(Direction.DOWN);
            } else if (deltaY < 0) {
                nextPos = position.add(Direction.UP);
            }
            
            // Si no puede moverse verticalmente, intentar horizontalmente
            if (nextPos == null || !isValidPosition(nextPos, level)) {
                if (deltaX > 0) {
                    nextPos = position.add(Direction.RIGHT);
                } else if (deltaX < 0) {
                    nextPos = position.add(Direction.LEFT);
                }
            }
        }
        
        // Si aún no hay una posición válida, quedarse en el lugar
        if (nextPos == null || !isValidPosition(nextPos, level)) {
            return position;
        }
        
        return nextPos;
    }
    
    private boolean isValidPosition(Position pos, Level level) {
        // Verificar límites del mapa
        if (pos.getX() < 1 || pos.getX() >= level.getWidth() - 1 ||
            pos.getY() < 1 || pos.getY() >= level.getHeight() - 1) {
            return false;
        }
        
        // Verificar que no hay paredes ni bloques de hielo
        if (!level.canMoveTo(pos) || level.isIceBlock(pos)) {
            return false;
        }
        
        // Verificar que no hay otro enemigo en esa posición
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
        moveCounter++;
        if (moveCounter >= MOVE_DELAY) {
            moveCounter = 0;
            Position nextPos = getNextPosition(level, player);
            
            // Solo moverse si la posición es diferente a la actual y es válida
            if (!nextPos.equals(position) && isValidPosition(nextPos, level)) {
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
        return "M"; // M de Maceta
    }
}