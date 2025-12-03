package domain;

/**
 * Clase abstracta que representa una fruta en el juego.
 * Las frutas son objetos recolectables que otorgan puntos al jugador.
 */
public abstract class Fruit extends GameObject implements Collectable {
    protected FruitType fruitType;
    protected boolean collected;

    /**
     * Constructor de la clase Fruit.
     * 
     * @param position  La posición inicial de la fruta.
     * @param fruitType El tipo de fruta.
     */
    public Fruit(Position position, FruitType fruitType) {
        super(position);
        this.fruitType = fruitType;
        this.collected = false;
        // Regenerar el ID después de asignar el fruitType
        this.id = generateId();
    }

    /**
     * Verifica si la fruta ha sido recolectada.
     * 
     * @return true si la fruta ha sido recolectada.
     */
    @Override
    public boolean isCollected() {
        return collected;
    }

    /**
     * Marca la fruta como recolectada.
     */
    @Override
    public void collect() {
        this.collected = true;
    }

    /**
     * Obtiene los puntos que otorga la fruta.
     * 
     * @return Los puntos definidos por el tipo de fruta.
     */
    @Override
    public int getPoints() {
        return fruitType.getPoints();
    }

    /**
     * Obtiene el tipo de fruta.
     * 
     * @return El tipo de fruta.
     */
    public FruitType getFruitType() {
        return fruitType;
    }

    @Override
    protected String generateId() {
        if (fruitType == null) {
            return "FRUIT_TEMP_" + System.nanoTime();
        }
        return "FRUIT_" + fruitType.name() + "_" + System.nanoTime();
    }

    @Override
    public String getType() {
        return "Fruit";
    }

    /**
     * Actualiza el estado de la fruta en el nivel.
     * 
     * @param level El nivel actual.
     */
    public abstract void update(Level level);
}