package domain;

/**
 * Representa un enemigo de tipo "Pot" (Maceta) en el juego.
 * Este enemigo persigue al jugador intentando acercarse en el eje con mayor
 * distancia.
 */
public class Pot extends Enemy {
    private int moveCounter;
    private static final int MOVE_DELAY = 8;

    /**
     * Constructor de la clase Pot.
     * 
     * @param position La posición inicial del enemigo.
     */
    public Pot(Position position) {
        super(position, EnemyType.POT);
        this.moveCounter = 0;
    }

    /**
     * Calcula la siguiente posición del enemigo basándose en la posición del
     * jugador.
     * Intenta moverse en la dirección que reduce la mayor distancia (X o Y).
     * 
     * @param level  El nivel actual del juego.
     * @param player El jugador al que persigue.
     * @return La siguiente posición a la que se moverá, o la actual si no puede
     *         moverse.
     */
    @Override
    public Position getNextPosition(Level level, Player player) {
        // Si el jugador no está vivo, el enemigo no se mueve.
        if (!player.isAlive()) {
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

        // Prioriza el movimiento en el eje con mayor distancia
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                nextPos = position.add(Direction.RIGHT);
            } else if (deltaX < 0) {
                nextPos = position.add(Direction.LEFT);
            }

            // Si no puede moverse en X, intenta en Y
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

            // Si no puede moverse en Y, intenta en X
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

    /**
     * Verifica si una posición es válida para que el enemigo se mueva.
     * 
     * @param pos   La posición a verificar.
     * @param level El nivel actual.
     * @return true si la posición es válida, false en caso contrario.
     */
    private boolean isValidPosition(Position pos, Level level) {
        // Verifica límites del mapa
        if (pos.getX() < 1 || pos.getX() >= level.getWidth() - 1 ||
                pos.getY() < 1 || pos.getY() >= level.getHeight() - 1) {
            return false;
        }

        // Verifica colisiones con el entorno
        if (!level.canMoveTo(pos) || level.isIceBlock(pos)) {
            return false;
        }

        // Verifica colisiones con otros enemigos
        for (Enemy enemy : level.getEnemies()) {
            if (enemy != this && enemy.isAlive() && enemy.getPosition().equals(pos)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Actualiza el comportamiento del enemigo en cada frame.
     * Controla la velocidad de movimiento mediante un contador.
     * 
     * @param level  El nivel actual.
     * @param player El jugador.
     */
    @Override
    public void updateBehavior(Level level, Player player) {
        moveCounter++;
        if (moveCounter >= MOVE_DELAY) {
            moveCounter = 0;
            Position nextPos = getNextPosition(level, player);

            if (!nextPos.equals(position) && isValidPosition(nextPos, level)) {
                move(nextPos);
            }
        }
    }

    /**
     * Verifica si el enemigo puede moverse a una posición específica.
     * 
     * @param position La posición destino.
     * @param level    El nivel actual.
     * @return true si puede moverse, false en caso contrario.
     */
    @Override
    public boolean canMoveTo(Position position, Level level) {
        return isValidPosition(position, level);
    }

    @Override
    public String getSymbol() {
        return "M";
    }
}