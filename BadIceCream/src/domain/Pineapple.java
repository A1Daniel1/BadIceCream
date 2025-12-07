package domain;

/**
 * Representa una fruta de tipo Piña.
 * Es una fruta móvil que se mueve cuando el jugador se mueve.
 */
public class Pineapple extends Fruit {
    private int moveCounter;
    private static final int MOVE_DELAY = 15;
    private static final double DIRECTION_CHANGE_PROBABILITY = 0.3;
    private Direction currentDirection;

    /**
     * Constructor de la clase Pineapple.
     * 
     * @param position La posición inicial de la piña.
     */
    public Pineapple(Position position) {
        super(position, FruitType.PINEAPPLE);
        this.moveCounter = 0;
        this.currentDirection = getRandomDirection();
    }

    /**
     * Actualiza el estado de la piña.
     * La piña se mueve de forma errática/aleatoria.
     * 
     * @param level El nivel actual.
     */
    @Override
    public void update(Level level) {
        if (collected)
            return;

        moveCounter++;
        if (moveCounter >= MOVE_DELAY) {
            moveCounter = 0;

            // Cambiar dirección aleatoriamente a veces
            if (Math.random() < DIRECTION_CHANGE_PROBABILITY) {
                currentDirection = getRandomDirection();
            }

            // Intentar moverse en la dirección actual
            Position newPos = position.add(currentDirection);

            if (canMoveTo(newPos, level)) {
                position = newPos;
            } else {
                // Si está bloqueada, cambiar de dirección
                currentDirection = getRandomDirection();
                newPos = position.add(currentDirection);
                if (canMoveTo(newPos, level)) {
                    position = newPos;
                }
            }
        }
    }

    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     * Verifica si la piña puede moverse a una posición específica.
     * 
     * @param pos   La posición destino.
     * @param level El nivel actual.
     * @return true si la posición es válida y no está bloqueada.
     */
    private boolean canMoveTo(Position pos, Level level) {
        // Verificar límites del nivel
        if (pos.getX() < 1 || pos.getX() >= level.getWidth() - 1 ||
                pos.getY() < 1 || pos.getY() >= level.getHeight() - 1) {
            return false;
        }

        // Verificar colisiones con paredes y bloques de hielo
        if (level.isWall(pos) || level.isIceBlock(pos)) {
            return false;
        }

        // Verificar colisiones con otras frutas
        if (level.getFruitAt(pos) != null) {
            return false;
        }

        // Verificar colisiones con enemigos
        if (level.hasEnemyAt(pos)) {
            return false;
        }

        return true;
    }

    @Override
    public String getSymbol() {
        return "Pi"; // Símbolo único para Piña
    }
}