package domain;

/**
 * Representa al jugador (Helado) controlado por el usuario.
 * Puede moverse, crear y destruir bloques de hielo.
 */
public class Player extends Unit implements IceInteractor {
    private IceCreamFlavour flavor;
    private int score;

    /**
     * Constructor de la clase Player.
     * 
     * @param position La posición inicial del jugador.
     * @param flavor   El sabor del helado (determina la apariencia).
     */
    public Player(Position position, IceCreamFlavour flavor) {
        super(position);
        this.flavor = flavor;
        this.score = 0;
        this.id = generateId();
    }

    /**
     * Obtiene el sabor del helado.
     * 
     * @return El sabor del jugador.
     */
    public IceCreamFlavour getFlavor() {
        return flavor;
    }

    /**
     * Obtiene la puntuación actual del jugador.
     * 
     * @return La puntuación.
     */
    public int getScore() {
        return score;
    }

    /**
     * Añade puntos a la puntuación del jugador.
     * 
     * @param points La cantidad de puntos a añadir.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Crea una línea de bloques de hielo en la dirección que mira el jugador.
     * La creación se detiene si encuentra un obstáculo o un enemigo.
     * 
     * @param level El nivel actual donde se crearán los bloques.
     */
    @Override
    public void createIceBlock(Level level) {
        Position currentPos = position.add(direction);
        while (level.canPlaceIceBlock(currentPos) && !level.hasEnemyAt(currentPos)) {
            level.addIceBlock(new IceBlock(currentPos));
            currentPos = currentPos.add(direction);
        }
    }

    /**
     * Destruye una línea de bloques de hielo en la dirección que mira el jugador.
     * 
     * @param level El nivel actual donde se destruirán los bloques.
     */
    @Override
    public void destroyIceBlock(Level level) {
        Position currentPos = position.add(direction);

        // Destruir en cadena (efecto dominó)
        while (level.isIceBlock(currentPos)) {
            IceBlock block = level.getIceBlockAt(currentPos);
            if (block != null) {
                block.destroy();
            }
            currentPos = currentPos.add(direction);
        }
    }

    /**
     * Indica si el jugador tiene la capacidad de romper hielo.
     * 
     * @return true siempre, ya que el jugador puede romper hielo.
     */
    @Override
    public boolean canBreakIce() {
        return true;
    }

    /**
     * Verifica si el jugador puede moverse a una posición específica.
     * 
     * @param position La posición destino.
     * @param level    El nivel actual.
     * @return true si la posición es válida en el nivel.
     */
    @Override
    public boolean canMoveTo(Position position, Level level) {
        return level.canMoveTo(position);
    }

    @Override
    protected String generateId() {
        if (flavor == null) {
            return "PLAYER_TEMP_" + System.nanoTime();
        }
        return "PLAYER_" + flavor.name();
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}