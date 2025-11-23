package domain;

public class Troll extends Enemy {
    private Position[] patrolPath;
    private int pathIndex;
    
    public Troll(Position position) {
        super(position, EnemyType.TROLL);
        this.pathIndex = 0;
        initializePatrolPath();
    }
    
    private void initializePatrolPath() {
        // Patrullaje simple en forma de cuadrado
        patrolPath = new Position[] {
            position.clone(),
            new Position(position.getX(), position.getY() + 1),
            new Position(position.getX(), position.getY() + 2),
            new Position(position.getX(), position.getY() + 3),
            new Position(position.getX(), position.getY() + 2),
            new Position(position.getX(), position.getY() + 1)
        };
    }
    
    @Override
    public Position getNextPosition(Level level, Player player) {
        if (patrolPath == null || patrolPath.length == 0) {
            return position;
        }
        
        Position nextPos = patrolPath[pathIndex];
        pathIndex = (pathIndex + 1) % patrolPath.length;
        
        if (level.canMoveTo(nextPos)) {
            return nextPos;
        }
        return position;
    }
    
    @Override
    public void updateBehavior(Level level, Player player) {
        Position nextPos = getNextPosition(level, player);
        if (level.canMoveTo(nextPos)) {
            move(nextPos);
        }
    }
    
    @Override
    public boolean canMoveTo(Position position, Level level) {
        return level.canMoveTo(position);
    }
    
    @Override
    public String getSymbol() { return "T"; }
}