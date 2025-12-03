package domain;

/**
 * Representa una fruta de tipo Plátano.
 * Es una fruta estática que otorga puntos.
 */
public class Banana extends Fruit {
    /**
     * Constructor de la clase Banana.
     * 
     * @param position La posición inicial del plátano.
     */
    public Banana(Position position) {
        super(position, FruitType.BANANA);
    }

    /**
     * Actualiza el estado del plátano.
     * Los plátanos no tienen comportamiento activo.
     * 
     * @param level El nivel actual.
     */
    @Override
    public void update(Level level) {
        // Los plátanos no se mueven
    }

    @Override
    public String getSymbol() {
        return "B";
    }
}