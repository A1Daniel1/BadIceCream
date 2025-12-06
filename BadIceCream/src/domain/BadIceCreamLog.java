package domain;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Clase de utilidad para registrar errores y eventos del juego.
 * Utiliza java.util.logging para escribir en un archivo de registro.
 */
public class BadIceCreamLog {

    private static final Logger logger = Logger.getLogger(BadIceCreamLog.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(BadIceCreamException.LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra un mensaje de error severo.
     * 
     * @param message El mensaje a registrar.
     */
    public static void log(String message) {
        logger.severe(message);
    }

    /**
     * Registra una excepción con su traza.
     * 
     * @param e La excepción a registrar.
     */
    public static void log(Exception e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
    }
}
