package domain;

/**
 * Representa una fruta de tipo Uva.
 * Es una fruta estática que otorga puntos.
 */
public class Grape extends Fruit {
    /**
     * Constructor de la clase Grape.
     * 
     * @param position La posición inicial de la uva.
     */
    public Grape(Position position) {
        super(position, FruitType.GRAPE);
    }

    /**
     * Actualiza el estado de la uva.
     * Las uvas no tienen comportamiento activo.
     * 
     * @param level El nivel actual.
     */
    @Override
    public void update(Level level) {
        // Las uvas no se mueven
    }

    @Override
    public String getSymbol() {
        return "G";
    }
}
