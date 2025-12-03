package domain;

/**
 * Representa un enemigo de tipo "Troll".
 * Este enemigo patrulla los bordes del nivel y cambia de dirección al encontrar
 * esquinas u obstáculos.
 */
public class Troll extends Enemy {
    private Direction currentDirection;
    private int moveCounter;
    private static final int MOVE_DELAY = 10; // Movimiento más lento
    private int blockedCounter; // Contador para cuando está bloqueado

    /**
     * Constructor de la clase Troll.
     * 
     * @param position La posición inicial del troll.
     */
    public Troll(Position position) {
        super(position, EnemyType.TROLL);
        this.moveCounter = 0;
        this.blockedCounter = 0;
        // Determinar dirección inicial basada en la posición en el borde
        this.currentDirection = determineInitialDirection(position);
    }

    /**
     * Determina la dirección inicial basada en la posición del troll en el borde.
     * 
     * @param pos La posición inicial.
     * @return La dirección inicial.
     */
    private Direction determineInitialDirection(Position pos) {
        // Esquina superior izquierda -> derecha
        if (pos.getX() <= 1 && pos.getY() <= 1)
            return Direction.RIGHT;
        // Esquina superior derecha -> abajo
        if (pos.getX() >= 13 && pos.getY() <= 1)
            return Direction.DOWN;
        // Esquina inferior derecha -> izquierda
        if (pos.getX() >= 13 && pos.getY() >= 9)
            return Direction.LEFT;
        // Esquina inferior izquierda -> arriba
        if (pos.getX() <= 1 && pos.getY() >= 9)
            return Direction.UP;

        // Borde superior -> derecha
        if (pos.getY() <= 1)
            return Direction.RIGHT;
        // Borde inferior -> izquierda
        if (pos.getY() >= 9)
            return Direction.LEFT;
        // Borde izquierdo -> abajo
        if (pos.getX() <= 1)
            return Direction.DOWN;
        // Borde derecho -> arriba
        if (pos.getX() >= 13)
            return Direction.UP;

        return Direction.RIGHT;
    }

    /**
     * Calcula la siguiente posición del troll.
     * Intenta seguir patrullando el borde. Si se bloquea, espera o cambia de
     * dirección.
     * 
     * @param level  El nivel actual.
     * @param player El jugador (no usado para el movimiento del troll).
     * @return La siguiente posición.
     */
    @Override
    public Position getNextPosition(Level level, Player player) {
        Position nextPos = position.add(currentDirection);

        // Verificar si la siguiente posición es válida
        if (!isValidBorderPosition(nextPos, level)) {
            // Si está bloqueado por hielo, esperar
            if (level.isIceBlock(nextPos)) {
                blockedCounter++;
                // Si ha estado bloqueado por mucho tiempo, intentar retroceder
                if (blockedCounter > 5) {
                    currentDirection = getOppositeDirection(currentDirection);
                    blockedCounter = 0;
                }
                return position; // Quedarse en la posición actual
            }

            // Cambiar dirección en la esquina o cuando encuentra obstáculo
            changeDirectionAtCorner();
            nextPos = position.add(currentDirection);

            // Si aún no es válida, intentar otra dirección
            if (!isValidBorderPosition(nextPos, level)) {
                changeDirectionAtCorner();
                nextPos = position.add(currentDirection);
            }
        } else {
            // Si se puede mover, resetear el contador de bloqueo
            blockedCounter = 0;
        }

        return nextPos;
    }

    /**
     * Verifica si una posición es válida para el troll (debe estar en el borde y
     * libre de obstáculos).
     * 
     * @param pos   La posición a verificar.
     * @param level El nivel actual.
     * @return true si la posición es válida.
     */
    private boolean isValidBorderPosition(Position pos, Level level) {
        // Verificar que está dentro de los límites
        if (pos.getX() < 1 || pos.getX() > 13 || pos.getY() < 1 || pos.getY() > 9) {
            return false;
        }

        // Verificar que está en el borde (perímetro del área jugable)
        boolean isOnBorder = pos.getX() == 1 || pos.getX() == 13 ||
                pos.getY() == 1 || pos.getY() == 9;

        if (!isOnBorder) {
            return false;
        }

        // Verificar que no hay paredes u obstáculos (incluyendo bloques de hielo)
        if (!level.canMoveTo(pos)) {
            return false;
        }

        // Verificar que no hay bloques de hielo
        if (level.isIceBlock(pos)) {
            return false;
        }

        // Verificar que no hay otro enemigo en esa posición
        for (Enemy enemy : level.getEnemies()) {
            if (enemy != this && enemy.isAlive() && enemy.getPosition().equals(pos)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Obtiene la dirección opuesta a la dada.
     * 
     * @param dir La dirección original.
     * @return La dirección opuesta.
     */
    private Direction getOppositeDirection(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return Direction.RIGHT;
        }
    }

    /**
     * Cambia la dirección del troll cuando llega a una esquina o borde.
     */
    private void changeDirectionAtCorner() {
        int x = position.getX();
        int y = position.getY();

        // Esquinas
        if (x == 1 && y == 1) {
            // Esquina superior izquierda
            currentDirection = (currentDirection == Direction.UP) ? Direction.RIGHT : Direction.DOWN;
        } else if (x == 13 && y == 1) {
            // Esquina superior derecha
            currentDirection = (currentDirection == Direction.RIGHT) ? Direction.DOWN : Direction.LEFT;
        } else if (x == 13 && y == 9) {
            // Esquina inferior derecha
            currentDirection = (currentDirection == Direction.DOWN) ? Direction.LEFT : Direction.UP;
        } else if (x == 1 && y == 9) {
            // Esquina inferior izquierda
            currentDirection = (currentDirection == Direction.LEFT) ? Direction.UP : Direction.RIGHT;
        }
        // Bordes
        else if (y == 1) {
            // Borde superior
            if (currentDirection == Direction.UP || currentDirection == Direction.LEFT) {
                currentDirection = Direction.RIGHT;
            }
        } else if (y == 9) {
            // Borde inferior
            if (currentDirection == Direction.DOWN || currentDirection == Direction.RIGHT) {
                currentDirection = Direction.LEFT;
            }
        } else if (x == 1) {
            // Borde izquierdo
            if (currentDirection == Direction.LEFT || currentDirection == Direction.UP) {
                currentDirection = Direction.DOWN;
            }
        } else if (x == 13) {
            // Borde derecho
            if (currentDirection == Direction.RIGHT || currentDirection == Direction.DOWN) {
                currentDirection = Direction.UP;
            }
        }
    }

    @Override
    public void updateBehavior(Level level, Player player) {
        moveCounter++;
        if (moveCounter >= MOVE_DELAY) {
            moveCounter = 0;
            Position nextPos = getNextPosition(level, player);

            // Solo moverse si la posición es diferente a la actual y es válida
            if (!nextPos.equals(position) && isValidBorderPosition(nextPos, level)) {
                move(nextPos);
            }
        }
    }

    @Override
    public boolean canMoveTo(Position position, Level level) {
        return isValidBorderPosition(position, level);
    }

    @Override
    public String getSymbol() {
        return "T";
    }
}