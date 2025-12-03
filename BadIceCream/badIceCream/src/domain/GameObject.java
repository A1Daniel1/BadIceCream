package domain;

/**
 * Clase abstracta que representa todas las entidades del juego.
 * Define las propiedades y comportamientos básicos compartidos por todos los
 * objetos.
 */
public abstract class GameObject {
    /** Posición actual del objeto en el tablero */
    protected Position position;
    /** Identificador único del objeto */
    protected String id;

    /**
     * Constructor de la clase GameObject.
     * 
     * @param position La posición inicial del objeto.
     */
    public GameObject(Position position) {
        this.position = position;
        this.id = "TEMP_" + System.nanoTime();
    }

    /**
     * Obtiene la posición actual del objeto.
     * 
     * @return La posición del objeto.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Establece una nueva posición para el objeto.
     * 
     * @param position La nueva posición a establecer.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Obtiene el identificador único del objeto.
     * 
     * @return El ID del objeto.
     */
    public String getId() {
        return id;
    }

    /**
     * Genera un identificador único para el objeto.
     * 
     * @return Una cadena con el ID generado.
     */
    protected abstract String generateId();

    /**
     * Obtiene el símbolo que representa al objeto (usado para depuración o
     * visualización en texto).
     * 
     * @return El carácter o cadena que representa al objeto.
     */
    public abstract String getSymbol();

    /**
     * Obtiene el tipo de objeto.
     * 
     * @return El tipo de objeto como cadena.
     */
    public abstract String getType();
}