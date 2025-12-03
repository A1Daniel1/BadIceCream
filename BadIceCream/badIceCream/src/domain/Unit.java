package domain;

/**
 * Clase abstracta que representa una unidad en el juego.
 * Las unidades son entidades que tienen vida, velocidad y pueden moverse (como
 * el jugador y los enemigos).
 */
public abstract class Unit extends GameObject implements Movable, Alive {
    protected Direction direction;
    protected boolean alive;
    protected int speed;

    /**
     * Constructor de la clase Unit.
     * 
     * @param position La posición inicial de la unidad.
     */
    public Unit(Position position) {
        super(position);
        this.direction = Direction.RIGHT;
        this.alive = true;
        this.speed = 1;
    }

    /**
     * Mueve la unidad a una nueva posición si está viva.
     * 
     * @param newPosition La nueva posición.
     */
    @Override
    public void move(Position newPosition) {
        if (alive) {
            this.position = newPosition;
        }
    }

    /**
     * Obtiene la dirección actual de la unidad.
     * 
     * @return La dirección.
     */
    @Override
    public Direction getDirection() {
        return direction;
    }

    /**
     * Establece la dirección de la unidad.
     * 
     * @param direction La nueva dirección.
     */
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Verifica si la unidad está viva.
     * 
     * @return true si está viva.
     */
    @Override
    public boolean isAlive() {
        return alive;
    }

    /**
     * Mata a la unidad.
     */
    @Override
    public void die() {
        this.alive = false;
    }

    /**
     * Revive a la unidad.
     */
    @Override
    public void revive() {
        this.alive = true;
    }

    /**
     * Obtiene la velocidad de la unidad.
     * 
     * @return La velocidad.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Establece la velocidad de la unidad.
     * 
     * @param speed La nueva velocidad.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String getType() {
        return "Unit";
    }
}
