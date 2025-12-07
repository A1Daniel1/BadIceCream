package domain;

import java.util.*;

/**
 * Representa un jugador controlado por IA.
 * Implementa diferentes estrategias según el perfil asignado.
 */
public class AIPlayer extends Player {
    private AIProfile profile;
    private int thinkDelay;
    private int thinkCounter;
    private Direction lastDecision;
    private Position targetFruit;

    /**
     * Constructor de la clase AIPlayer.
     * 
     * @param position La posición inicial del jugador.
     * @param flavor   El sabor del helado.
     * @param profile  El perfil de comportamiento de la IA.
     */
    public AIPlayer(Position position, IceCreamFlavour flavor, AIProfile profile) {
        super(position, flavor);
        // IMPORTANTE: Asignar el perfil ANTES de generar el ID
        this.profile = profile != null ? profile : AIProfile.EXPERT;
        this.thinkDelay = 5;
        this.thinkCounter = 0;
        this.lastDecision = Direction.RIGHT;
        this.targetFruit = null;
        // Regenerar ID después de establecer el perfil
        this.id = generateId();
    }

    /**
     * Obtiene el perfil de comportamiento de la IA.
     * 
     * @return El perfil de la IA.
     */
    public AIProfile getProfile() {
        return profile;
    }

    /**
     * Decide la siguiente acción a realizar por la IA basándose en el estado del
     * nivel y su perfil.
     * 
     * @param level El nivel actual del juego.
     * @return La acción decidida por la IA.
     */
    public AIAction decideAction(Level level) {
        thinkCounter++;
        if (thinkCounter < thinkDelay) {
            return new AIAction(AIActionType.WAIT, null);
        }
        thinkCounter = 0;

        switch (profile) {
            case HUNGRY:
                return decideHungryAction(level);
            case FEARFUL:
                return decideFearfulAction(level);
            case EXPERT:
                return decideExpertAction(level);
            default:
                return decideHungryAction(level);
        }
    }

    /**
     * Estrategia HUNGRY: Busca la fruta más cercana agresivamente.
     * 
     * @param level El nivel actual.
     * @return La acción decidida.
     */
    private AIAction decideHungryAction(Level level) {
        // Buscar fruta más cercana NO RECOLECTADA
        Fruit closestFruit = findClosestFruit(level);

        if (closestFruit == null || closestFruit.isCollected()) {
            // No hay frutas, explorar
            Direction exploreDir = getRandomValidDirection(level);
            setDirection(exploreDir);
            lastDecision = exploreDir;
            return new AIAction(AIActionType.MOVE, exploreDir);
        }

        Position fruitPos = closestFruit.getPosition();

        // Calcular diferencias
        int dx = fruitPos.getX() - position.getX();
        int dy = fruitPos.getY() - position.getY();

        // Probar todas las direcciones que nos acercan
        Direction[] tryDirections = new Direction[4];
        int index = 0;

        // Agregar direcciones que reducen la distancia
        if (dx > 0)
            tryDirections[index++] = Direction.RIGHT;
        if (dx < 0)
            tryDirections[index++] = Direction.LEFT;
        if (dy > 0)
            tryDirections[index++] = Direction.DOWN;
        if (dy < 0)
            tryDirections[index++] = Direction.UP;

        // Intentar cada dirección
        for (int i = 0; i < index; i++) {
            Direction dir = tryDirections[i];
            if (dir == null)
                continue;

            Position nextPos = position.add(dir);

            // Si hay hielo, destruirlo
            if (level.isIceBlock(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.DESTROY_ICE, dir);
            }

            // Si puede moverse, hacerlo
            if (level.canMoveTo(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.MOVE, dir);
            }
        }

        // Si ninguna dirección hacia la fruta funciona, buscar ruta alternativa
        for (Direction dir : Direction.values()) {
            Position nextPos = position.add(dir);

            if (level.isIceBlock(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.DESTROY_ICE, dir);
            }

            if (level.canMoveTo(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.MOVE, dir);
            }
        }

        // Última opción: mantener dirección anterior
        return new AIAction(AIActionType.MOVE, lastDecision);
    }

    /**
     * Estrategia FEARFUL: Evita enemigos y peligros, priorizando la supervivencia.
     * 
     * @param level El nivel actual.
     * @return La acción decidida.
     */
    private AIAction decideFearfulAction(Level level) {
        List<Position> dangers = findNearbyDangers(level);

        // Si hay peligro cercano, huir
        if (!dangers.isEmpty()) {
            Position closestDanger = getClosestPosition(dangers);
            int dangerDistance = manhattanDistance(position, closestDanger);

            if (dangerDistance <= 4) {
                // Calcular dirección de escape (alejarse del peligro)
                int dx = position.getX() - closestDanger.getX();
                int dy = position.getY() - closestDanger.getY();

                // Probar direcciones que alejan del peligro
                Direction[] escapeDirections = new Direction[4];
                int index = 0;

                if (dx > 0)
                    escapeDirections[index++] = Direction.RIGHT;
                if (dx < 0)
                    escapeDirections[index++] = Direction.LEFT;
                if (dy > 0)
                    escapeDirections[index++] = Direction.DOWN;
                if (dy < 0)
                    escapeDirections[index++] = Direction.UP;

                // Intentar cada dirección de escape
                for (int i = 0; i < index; i++) {
                    Direction dir = escapeDirections[i];
                    if (dir == null)
                        continue;

                    Position nextPos = position.add(dir);

                    if (level.isIceBlock(nextPos)) {
                        setDirection(dir);
                        lastDecision = dir;
                        return new AIAction(AIActionType.DESTROY_ICE, dir);
                    }

                    if (level.canMoveTo(nextPos)) {
                        setDirection(dir);
                        lastDecision = dir;
                        return new AIAction(AIActionType.MOVE, dir);
                    }
                }

                // Si no puede escapar directamente, moverse en cualquier dirección que no
                // acerque
                for (Direction dir : Direction.values()) {
                    Position nextPos = position.add(dir);
                    if (level.canMoveTo(nextPos)) {
                        int newDist = manhattanDistance(nextPos, closestDanger);
                        // Aceptar movimientos que mantienen o aumentan distancia
                        if (newDist >= dangerDistance - 1) {
                            setDirection(dir);
                            lastDecision = dir;
                            return new AIAction(AIActionType.MOVE, dir);
                        }
                    }
                }
            }
        }

        // Si no hay peligro cercano, buscar frutas seguras (lejos de enemigos)
        Fruit safeFruit = findClosestSafeFruit(level);
        if (safeFruit != null && !safeFruit.isCollected()) {
            Position fruitPos = safeFruit.getPosition();
            int dx = fruitPos.getX() - position.getX();
            int dy = fruitPos.getY() - position.getY();

            Direction[] tryDirections = new Direction[4];
            int index = 0;

            if (dx > 0)
                tryDirections[index++] = Direction.RIGHT;
            if (dx < 0)
                tryDirections[index++] = Direction.LEFT;
            if (dy > 0)
                tryDirections[index++] = Direction.DOWN;
            if (dy < 0)
                tryDirections[index++] = Direction.UP;

            for (int i = 0; i < index; i++) {
                Direction dir = tryDirections[i];
                if (dir == null)
                    continue;

                Position nextPos = position.add(dir);

                if (level.isIceBlock(nextPos)) {
                    setDirection(dir);
                    lastDecision = dir;
                    return new AIAction(AIActionType.DESTROY_ICE, dir);
                }

                if (level.canMoveTo(nextPos)) {
                    setDirection(dir);
                    lastDecision = dir;
                    return new AIAction(AIActionType.MOVE, dir);
                }
            }
        }

        // Movimiento aleatorio seguro
        Direction safeDir = getSafeRandomDirection(level);
        setDirection(safeDir);
        lastDecision = safeDir;
        return new AIAction(AIActionType.MOVE, safeDir);
    }

    /**
     * Estrategia EXPERT: Balancea riesgo y recompensa para tomar decisiones
     * óptimas.
     * 
     * @param level El nivel actual.
     * @return La acción decidida.
     */
    private AIAction decideExpertAction(Level level) {
        List<Position> dangers = findNearbyDangers(level);

        // Si hay peligro muy cercano (distancia <= 2), escapar
        if (!dangers.isEmpty()) {
            Position closestDanger = getClosestPosition(dangers);
            int dangerDistance = manhattanDistance(position, closestDanger);

            if (dangerDistance <= 2) {
                return decideFearfulAction(level);
            }
        }

        // Buscar la mejor fruta (considera valor y distancia)
        Fruit bestFruit = findBestFruitTarget(level);

        if (bestFruit == null || bestFruit.isCollected()) {
            // Sin frutas, moverse de forma segura
            Direction safeDir = getSafeRandomDirection(level);
            setDirection(safeDir);
            lastDecision = safeDir;
            return new AIAction(AIActionType.MOVE, safeDir);
        }

        Position fruitPos = bestFruit.getPosition();
        int dx = fruitPos.getX() - position.getX();
        int dy = fruitPos.getY() - position.getY();

        // Probar direcciones que acercan a la fruta
        Direction[] tryDirections = new Direction[4];
        int index = 0;

        if (dx > 0)
            tryDirections[index++] = Direction.RIGHT;
        if (dx < 0)
            tryDirections[index++] = Direction.LEFT;
        if (dy > 0)
            tryDirections[index++] = Direction.DOWN;
        if (dy < 0)
            tryDirections[index++] = Direction.UP;

        // Intentar cada dirección verificando seguridad
        for (int i = 0; i < index; i++) {
            Direction dir = tryDirections[i];
            if (dir == null)
                continue;

            Position nextPos = position.add(dir);

            // Verificar si es seguro
            boolean isSafe = true;
            for (Position danger : dangers) {
                if (manhattanDistance(nextPos, danger) <= 1) {
                    isSafe = false;
                    break;
                }
            }

            if (!isSafe)
                continue;

            // Si hay hielo y es seguro, destruirlo
            if (level.isIceBlock(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.DESTROY_ICE, dir);
            }

            // Si puede moverse y es seguro, hacerlo
            if (level.canMoveTo(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.MOVE, dir);
            }
        }

        // Si ninguna dirección hacia la fruta es segura, buscar cualquier movimiento
        // seguro
        for (Direction dir : Direction.values()) {
            Position nextPos = position.add(dir);

            if (!level.canMoveTo(nextPos))
                continue;

            // Verificar seguridad
            boolean isSafe = true;
            for (Position danger : dangers) {
                if (manhattanDistance(nextPos, danger) <= 1) {
                    isSafe = false;
                    break;
                }
            }

            if (isSafe) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.MOVE, dir);
            }
        }

        // Última opción: moverse a cualquier lugar válido (situación desesperada)
        for (Direction dir : Direction.values()) {
            Position nextPos = position.add(dir);
            if (level.canMoveTo(nextPos)) {
                setDirection(dir);
                lastDecision = dir;
                return new AIAction(AIActionType.MOVE, dir);
            }
        }

        return new AIAction(AIActionType.MOVE, lastDecision);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Encuentra la fruta más cercana al jugador.
     * 
     * @param level El nivel actual.
     * @return La fruta más cercana o null si no hay.
     */
    private Fruit findClosestFruit(Level level) {
        Fruit closest = null;
        int minDist = Integer.MAX_VALUE;

        for (Fruit fruit : level.getFruits()) {
            if (!fruit.isCollected()) {
                int dist = manhattanDistance(position, fruit.getPosition());
                if (dist < minDist) {
                    minDist = dist;
                    closest = fruit;
                }
            }
        }

        return closest;
    }

    /**
     * Encuentra la fruta más cercana que se encuentra en una posición segura.
     * 
     * @param level El nivel actual.
     * @return La fruta segura más cercana o null si no hay.
     */
    private Fruit findClosestSafeFruit(Level level) {
        Fruit closest = null;
        int minDist = Integer.MAX_VALUE;

        for (Fruit fruit : level.getFruits()) {
            if (!fruit.isCollected() && isSafePosition(level, fruit.getPosition())) {
                int dist = manhattanDistance(position, fruit.getPosition());
                if (dist < minDist) {
                    minDist = dist;
                    closest = fruit;
                }
            }
        }

        return closest;
    }

    /**
     * Encuentra la mejor fruta objetivo basándose en una puntuación de
     * valor/riesgo.
     * 
     * @param level El nivel actual.
     * @return La mejor fruta objetivo.
     */
    private Fruit findBestFruitTarget(Level level) {
        Fruit best = null;
        double bestScore = 0;

        for (Fruit fruit : level.getFruits()) {
            if (!fruit.isCollected()) {
                int dist = manhattanDistance(position, fruit.getPosition());
                int value = fruit.getPoints();
                int risk = evaluatePathRisk(level, fruit.getPosition());

                double score = value / (double) (dist + risk + 1);

                if (score > bestScore) {
                    bestScore = score;
                    best = fruit;
                }
            }
        }

        return best;
    }

    /**
     * Encuentra todos los peligros cercanos (enemigos, fogatas, cactus) dentro de
     * un radio.
     * 
     * @param level El nivel actual.
     * @return Lista de posiciones peligrosas.
     */
    private List<Position> findNearbyDangers(Level level) {
        List<Position> dangers = new ArrayList<>();
        int dangerRadius = 3;

        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isAlive()) {
                int dist = manhattanDistance(position, enemy.getPosition());
                if (dist <= dangerRadius) {
                    dangers.add(enemy.getPosition());
                }
            }
        }

        for (Campfire fire : level.getCampfires()) {
            if (fire.isLit()) {
                int dist = manhattanDistance(position, fire.getPosition());
                if (dist <= dangerRadius) {
                    dangers.add(fire.getPosition());
                }
            }
        }

        for (Fruit fruit : level.getFruits()) {
            if (fruit instanceof Cactus && ((Cactus) fruit).hasThorns()) {
                int dist = manhattanDistance(position, fruit.getPosition());
                if (dist <= dangerRadius) {
                    dangers.add(fruit.getPosition());
                }
            }
        }

        return dangers;
    }

    /**
     * Verifica si el jugador está en peligro inmediato (adyacente a un peligro).
     * 
     * @param level   El nivel actual.
     * @param dangers Lista de posiciones peligrosas.
     * @return true si está en peligro inmediato, false en caso contrario.
     */
    private boolean isInImmediateDanger(Level level, List<Position> dangers) {
        for (Position danger : dangers) {
            if (manhattanDistance(position, danger) <= 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si una posición es segura (sin enemigos cercanos ni peligros
     * estáticos).
     * 
     * @param level El nivel actual.
     * @param pos   La posición a verificar.
     * @return true si es segura, false en caso contrario.
     */
    private boolean isSafePosition(Level level, Position pos) {
        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isAlive() && manhattanDistance(pos, enemy.getPosition()) <= 2) {
                return false;
            }
        }

        if (level.isDeadlyCampfire(pos)) {
            return false;
        }

        return true;
    }

    /**
     * Evalúa el riesgo de un camino hacia un objetivo.
     * 
     * @param level  El nivel actual.
     * @param target La posición objetivo.
     * @return Un valor numérico representando el riesgo.
     */
    private int evaluatePathRisk(Level level, Position target) {
        int risk = 0;

        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isAlive()) {
                Position enemyPos = enemy.getPosition();
                if (isInPath(enemyPos, position, target)) {
                    risk += 2;
                }
            }
        }

        return risk;
    }

    /**
     * Verifica si un punto está dentro del rectángulo formado por dos posiciones.
     * 
     * @param point El punto a verificar.
     * @param start Posición inicial.
     * @param end   Posición final.
     * @return true si está en el camino, false en caso contrario.
     */
    private boolean isInPath(Position point, Position start, Position end) {
        int minX = Math.min(start.getX(), end.getX());
        int maxX = Math.max(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());
        int maxY = Math.max(start.getY(), end.getY());

        return point.getX() >= minX && point.getX() <= maxX &&
                point.getY() >= minY && point.getY() <= maxY;
    }

    /**
     * Determina si se debe crear hielo defensivo ante peligros cercanos.
     * 
     * @param level   El nivel actual.
     * @param dangers Lista de peligros.
     * @return true si se debe crear hielo, false en caso contrario.
     */
    private boolean shouldCreateDefensiveIce(Level level, List<Position> dangers) {
        for (Position danger : dangers) {
            if (manhattanDistance(position, danger) == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene la dirección hacia donde crear hielo defensivo.
     * 
     * @param dangers Lista de peligros.
     * @return La dirección hacia el peligro más cercano.
     */
    private Direction getDefensiveIceDirection(List<Position> dangers) {
        Position closest = getClosestPosition(dangers);
        return getDirectionTowards(closest);
    }

    /**
     * Calcula la mejor dirección estratégica evaluando múltiples factores.
     * 
     * @param level   El nivel actual.
     * @param dangers Lista de peligros.
     * @return La mejor dirección calculada.
     */
    private Direction getStrategicDirection(Level level, List<Position> dangers) {
        Direction best = Direction.RIGHT;
        double bestScore = -1000;

        for (Direction dir : Direction.values()) {
            Position next = position.add(dir);
            if (level.canMoveTo(next)) {
                double score = evaluatePositionScore(level, next, dangers);
                if (score > bestScore) {
                    bestScore = score;
                    best = dir;
                }
            }
        }

        return best;
    }

    /**
     * Evalúa una posición y le asigna una puntuación basada en seguridad y cercanía
     * a frutas.
     * 
     * @param level   El nivel actual.
     * @param pos     La posición a evaluar.
     * @param dangers Lista de peligros.
     * @return La puntuación de la posición.
     */
    private double evaluatePositionScore(Level level, Position pos, List<Position> dangers) {
        double score = 0;

        for (Position danger : dangers) {
            int dist = manhattanDistance(pos, danger);
            score -= 10.0 / (dist + 1);
        }

        for (Fruit fruit : level.getFruits()) {
            if (!fruit.isCollected()) {
                int dist = manhattanDistance(pos, fruit.getPosition());
                score += fruit.getPoints() / (double) (dist + 1);
            }
        }

        return score;
    }

    /**
     * Obtiene la dirección que acerca más al objetivo.
     * 
     * @param target La posición objetivo.
     * @return La dirección hacia el objetivo.
     */
    private Direction getDirectionTowards(Position target) {
        int dx = target.getX() - position.getX();
        int dy = target.getY() - position.getY();

        // Priorizar el eje con mayor distancia
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else if (Math.abs(dy) > Math.abs(dx)) {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        } else {
            // Si las distancias son iguales, elegir basado en el signo
            if (dx != 0) {
                return dx > 0 ? Direction.RIGHT : Direction.LEFT;
            } else if (dy != 0) {
                return dy > 0 ? Direction.DOWN : Direction.UP;
            } else {
                // Estamos en el objetivo, mantener última dirección
                return lastDecision != null ? lastDecision : Direction.RIGHT;
            }
        }
    }

    /**
     * Obtiene la dirección que aleja más de un peligro.
     * 
     * @param danger La posición del peligro.
     * @return La dirección de escape.
     */
    private Direction getDirectionAwayFrom(Position danger) {
        int dx = position.getX() - danger.getX();
        int dy = position.getY() - danger.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    /**
     * Busca una dirección alternativa para llegar al objetivo si la directa está
     * bloqueada.
     * 
     * @param level  El nivel actual.
     * @param target La posición objetivo.
     * @return Una dirección válida alternativa.
     */
    private Direction findAlternativeDirection(Level level, Position target) {
        Direction[] dirs = Direction.values();
        Direction best = null;
        int bestDist = Integer.MAX_VALUE;

        // Primero intentar encontrar una dirección válida que nos acerque al objetivo
        for (Direction dir : dirs) {
            Position next = position.add(dir);
            if (level.canMoveTo(next)) {
                int dist = manhattanDistance(next, target);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = dir;
                }
            }
        }

        // Si encontramos una dirección válida, usarla
        if (best != null) {
            return best;
        }

        // Si no hay direcciones válidas hacia el objetivo, buscar cualquier dirección
        // válida
        for (Direction dir : dirs) {
            Position next = position.add(dir);
            if (level.canMoveTo(next)) {
                return dir;
            }
        }

        // Si no hay ninguna dirección válida, mantener la dirección actual
        return lastDecision != null ? lastDecision : Direction.RIGHT;
    }

    /**
     * Obtiene una dirección aleatoria válida (sin obstáculos inmediatos).
     * 
     * @param level El nivel actual.
     * @return Una dirección válida.
     */
    private Direction getRandomValidDirection(Level level) {
        List<Direction> valid = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Position next = position.add(dir);
            if (level.canMoveTo(next)) {
                valid.add(dir);
            }
        }

        if (valid.isEmpty()) {
            return Direction.RIGHT;
        }

        return valid.get((int) (Math.random() * valid.size()));
    }

    /**
     * Obtiene una dirección aleatoria que sea segura (sin peligros inmediatos).
     * 
     * @param level El nivel actual.
     * @return Una dirección segura.
     */
    private Direction getSafeRandomDirection(Level level) {
        List<Direction> safe = new ArrayList<>();
        List<Position> dangers = findNearbyDangers(level);

        for (Direction dir : Direction.values()) {
            Position next = position.add(dir);
            if (level.canMoveTo(next) && isSafeFromDangers(next, dangers)) {
                safe.add(dir);
            }
        }

        if (safe.isEmpty()) {
            return getRandomValidDirection(level);
        }

        return safe.get((int) (Math.random() * safe.size()));
    }

    /**
     * Verifica si una posición es segura respecto a una lista de peligros.
     * 
     * @param pos     La posición a verificar.
     * @param dangers Lista de peligros.
     * @return true si es segura, false si hay peligro adyacente.
     */
    private boolean isSafeFromDangers(Position pos, List<Position> dangers) {
        for (Position danger : dangers) {
            if (manhattanDistance(pos, danger) <= 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Encuentra la posición más cercana de una lista.
     * 
     * @param positions Lista de posiciones.
     * @return La posición más cercana.
     */
    private Position getClosestPosition(List<Position> positions) {
        Position closest = null;
        int minDist = Integer.MAX_VALUE;

        for (Position pos : positions) {
            int dist = manhattanDistance(position, pos);
            if (dist < minDist) {
                minDist = dist;
                closest = pos;
            }
        }

        return closest;
    }

    /**
     * Calcula la distancia Manhattan entre dos posiciones.
     * 
     * @param a Posición A.
     * @param b Posición B.
     * @return La distancia Manhattan.
     */
    private int manhattanDistance(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }
}

// ==================== CLASES AUXILIARES ====================

/**
 * Representa una acción decidida por la IA
 */
class AIAction {
    private AIActionType type;
    private Direction direction;

    /**
     * Constructor de AIAction.
     * 
     * @param type      Tipo de acción.
     * @param direction Dirección de la acción (puede ser null para WAIT).
     */
    public AIAction(AIActionType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    /**
     * Obtiene el tipo de acción.
     * 
     * @return El tipo de acción.
     */
    public AIActionType getType() {
        return type;
    }

    /**
     * Obtiene la dirección de la acción.
     * 
     * @return La dirección.
     */
    public Direction getDirection() {
        return direction;
    }
}

/**
 * Tipos de acciones que puede realizar la IA
 */
enum AIActionType {
    MOVE,
    CREATE_ICE,
    DESTROY_ICE,
    WAIT
}