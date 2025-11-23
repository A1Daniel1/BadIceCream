package domain;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);
    
    private final int offsetX;
    private final int offsetY;
    
    Direction(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public int getOffsetX() { return offsetX; }
    public int getOffsetY() { return offsetY; }
}
