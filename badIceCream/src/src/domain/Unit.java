package domain;

/**
 * Clase abstracta para unidades (entidades que pueden moverse y tienen vida)
 */
public abstract class Unit extends GameObject implements Movable, Alive {
    protected Direction direction;
    protected boolean alive;
    protected int speed;
    
    public Unit(Position position) {
        super(position);
        this.direction = Direction.RIGHT;
        this.alive = true;
        this.speed = 1;
    }
    
    @Override
    public void move(Position newPosition) {
        if (alive && newPosition != null) {
            this.position = newPosition;
        }
    }
    
    @Override
    public Direction getDirection() { return direction; }
    
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    @Override
    public boolean isAlive() { return alive; }
    
    @Override
    public void die() { this.alive = false; }
    
    @Override
    public void revive() { this.alive = true; }
    
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    
    @Override
    public String getType() { return "Unit"; }
}
