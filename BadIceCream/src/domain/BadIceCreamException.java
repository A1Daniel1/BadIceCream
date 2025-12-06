package domain;

/**
 * Excepción personalizada para errores específicos del juego Bad Ice Cream.
 */
public class BadIceCreamException extends Exception {

    /** Nombre del archivo de registro de errores. */
    public static final String LOG_FILE = "error.log";

    /**
     * Constructor con mensaje de error.
     * 
     * @param message El mensaje de error.
     */
    public BadIceCreamException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje de error y causa.
     * 
     * @param message El mensaje de error.
     * @param cause   La causa original de la excepción.
     */
    public BadIceCreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
