package domain;

public class BadIceCreamException extends Exception {

    public static final String LOG_FILE = "error.log";

    public BadIceCreamException(String message) {
        super(message);
    }

    public BadIceCreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
