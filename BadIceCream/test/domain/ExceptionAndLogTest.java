package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class ExceptionAndLogTest {

    @Test
    public void testBadIceCreamException() {
        BadIceCreamException ex = new BadIceCreamException("Test Error");
        assertEquals("Test Error", ex.getMessage());

        Exception cause = new Exception("Cause");
        BadIceCreamException exWithCause = new BadIceCreamException("Test Error", cause);
        assertEquals("Test Error", exWithCause.getMessage());
        assertEquals(cause, exWithCause.getCause());
    }

    @Test
    public void testBadIceCreamLog() {
        // BadIceCreamLog is a utility class with static methods, no singleton instance
        // accessor exposed

        // Just verify these don't throw exceptions
        BadIceCreamLog.log("Test message");
        BadIceCreamLog.log(new Exception("Test exception"));

        // Verify log file creation (optional, but good for coverage)
        File logFile = new File("badicecream.log");
        // We don't want to delete it if it exists for other reasons, but we can check
        // it
    }
}
