package domain;

/**
 * Representa una posición en el tablero
 */

public class Position {
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public boolean equals(Position other) {
        if (other == null) return false;
        return this.x == other.x && this.y == other.y;
    }
    
    public Position clone() {
        return new Position(this.x, this.y);
    }
    
    public Position add(Direction direction) {
        return new Position(
            this.x + direction.getOffsetX(),
            this.y + direction.getOffsetY()
        );
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
}
