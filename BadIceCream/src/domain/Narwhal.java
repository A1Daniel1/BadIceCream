package domain;

/**
 * Narval - Patrulla y embiste al jugador cuando se alinea
 */
public class Narwhal extends Enemy {
    private Direction currentDirection;
    private int moveCounter;
    private static final int MOVE_DELAY = 8;
    private boolean charging; // Está embistiendo
    private int chargeSpeed;
    private static final int CHARGE_SPEED = 2; // Más rápido al embestir

    public Narwhal(Position position) {
        super(position, EnemyType.POT); // Usamos POT temporalmente
        this.moveCounter = 0;
        this.charging = false;
        this.chargeSpeed = 0;
        this.currentDirection = determineRandomDirection();
    }

    private Direction determineRandomDirection() {
        Direction[] dirs = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
        return dirs[(int) (Math.random() * 4)];
    }

    @Override
    public Position getNextPosition(Level level, Player player) {
        if (player == null || !player.isAlive()) {
            return position;
        }

        Position playerPos = player.getPosition();

        // Verificar si el jugador está alineado horizontal o verticalmente
        boolean alignedHorizontally = position.getY() == playerPos.getY();
        boolean alignedVertically = position.getX() == playerPos.getX();

        if (!charging && (alignedHorizontally || alignedVertically)) {
            // ¡Iniciar embestida!
            charging = true;
            chargeSpeed = 0;

            if (alignedHorizontally) {
                currentDirection = (playerPos.getX() > position.getX()) ? Direction.RIGHT : Direction.LEFT;
            } else {
                currentDirection = (playerPos.getY() > position.getY()) ? Direction.DOWN : Direction.UP;
            }
        }

        Position nextPos = position.add(currentDirection);

        // Verificar si puede moverse
        if (!isValidMovePosition(nextPos, level)) {
            // Si está cargando y choca, detener la carga
            if (charging) {
                charging = false;
            }
            // Cambiar de dirección
            changeDirection(level);
            nextPos = position.add(currentDirection);

            if (!isValidMovePosition(nextPos, level)) {
                return position;
            }
        }

        return nextPos;
    }

    private boolean isValidMovePosition(Position pos, Level level) {
        if (pos.getX() < 1 || pos.getX() >= level.getWidth() - 1 ||
                pos.getY() < 1 || pos.getY() >= level.getHeight() - 1) {
            return false;
        }

        // Check walls (always blocking)
        if (level.isWall(pos)) {
            return false;
        }

        // Durante la carga, destruye hielo pero no se detiene
        // En movimiento normal, el hielo lo bloquea
        if (!charging && level.isIceBlock(pos)) {
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

    private void changeDirection(Level level) {
        Direction[] options = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
        for (Direction dir : options) {
            if (dir != getOppositeDirection(currentDirection)) {
                Position testPos = position.add(dir);
                if (isValidMovePosition(testPos, level)) {
                    currentDirection = dir;
                    return;
                }
            }
        }
        currentDirection = getOppositeDirection(currentDirection);
    }

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

    @Override
    public void updateBehavior(Level level, Player player) {
        int delay = charging ? CHARGE_SPEED : MOVE_DELAY;

        moveCounter++;
        if (moveCounter >= delay) {
            moveCounter = 0;
            Position nextPos = getNextPosition(level, player);

            // Si está cargando y hay hielo, destruirlo
            if (charging && level.isIceBlock(nextPos)) {
                IceBlock block = level.getIceBlockAt(nextPos);
                if (block != null) {
                    block.destroy();
                }
            }

            if (!nextPos.equals(position) && isValidMovePosition(nextPos, level)) {
                move(nextPos);
            } else if (charging) {
                // Si chocó durante la carga, detenerla
                charging = false;
            }
        }
    }

    @Override
    public boolean canMoveTo(Position position, Level level) {
        return isValidMovePosition(position, level);
    }

    @Override
    public String getSymbol() {
        return charging ? "N!" : "N";
    }
}