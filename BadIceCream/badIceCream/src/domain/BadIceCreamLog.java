package domain;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    public static void log(String message) {
        logger.severe(message);
    }

    public static void log(Exception e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
    }
}
