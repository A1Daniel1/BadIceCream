package domain;

/**
 * Representa una fruta de tipo Cereza.
 * Es una fruta móvil que otorga puntos.
 */
public class Cherry extends Fruit {
    private int moveCounter;
    private static final int MOVE_DELAY = 15;
    private Direction currentDirection;
    
    /**
     * Constructor de la clase Cherry.
     * 
     * @param position La posición inicial de la cereza.
     */
    public Cherry(Position position) {
        super(position, FruitType.CHERRY);
        this.moveCounter = 0;
        this.currentDirection = getRandomDirection();
    }

    /**
     * Actualiza el estado de la cereza.
     * Las cerezas se mueven aleatoriamente.
     * 
     * @param level El nivel actual.
     */
    @Override
    public void update(Level level) {
        if (collected) return;
        
        moveCounter++;
        if (moveCounter >= MOVE_DELAY) {
            moveCounter = 0;
            
            // Intentar moverse en la dirección actual
            Position newPos = position.add(currentDirection);
            
            if (canMoveTo(newPos, level)) {
                position = newPos;
            } else {
                // Cambiar de dirección si está bloqueada
                currentDirection = getRandomDirection();
            }
        }
    }
    
    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[(int)(Math.random() * directions.length)];
    }
    
    private boolean canMoveTo(Position pos, Level level) {
        if (pos.getX() < 1 || pos.getX() >= level.getWidth() - 1 ||
            pos.getY() < 1 || pos.getY() >= level.getHeight() - 1) {
            return false;
        }
        
        if (level.isWall(pos) || level.isIceBlock(pos)) {
            return false;
        }
        
        if (level.getFruitAt(pos) != null) {
            return false;
        }
        
        if (level.hasEnemyAt(pos)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String getSymbol() {
        return "Ch";
    }
}