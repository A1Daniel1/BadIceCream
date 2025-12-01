package domain;

/**
 * Interface para entidades que pueden moverse
 */
public interface Movable {
    void move(Position newPosition);
    Position getPosition();
    Direction getDirection();
    void setDirection(Direction direction);
    boolean canMoveTo(Position position, Level level);
}