package DisplayUtils;

public class ConsoleOutputHandler implements OutputHandler {
    /**
     * Prints a message to the console without a newline.
     * @param message The message to be printed.
     */
    @Override
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Prints a message to the console followed by a newline.
     * @param message The message to be printed.
     */
    @Override
    public void println(String message) {
        System.out.println(message);
    }

    /**
     * Prints a formatted string to the console.
     * @param format The format string.
     * @param args The arguments to be formatted and printed.
     */
    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}