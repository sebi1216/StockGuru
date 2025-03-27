package DisplayUtils;

/**
 * Interface for output handling.
 * This interface defines methods for printing messages to the console or any other output stream.
 */
public interface OutputHandler {
    void print(String message);
    void println(String message);
    void printf(String format, Object... args);
}