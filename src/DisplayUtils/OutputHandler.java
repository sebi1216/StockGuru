package DisplayUtils;

public interface OutputHandler {
    void print(String message);
    void println(String message);
    void printf(String format, Object... args);
}