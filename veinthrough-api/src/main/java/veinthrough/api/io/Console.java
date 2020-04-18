package veinthrough.api.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author veinthrough
 * <p>
 * Implement a wrapped Console as System.console() will be null in eclipse/IntelliJ
 */
public class Console {
    private BufferedReader reader;
    // caution: System.console() will be null in eclipse/IntelliJ.
    private static boolean systemConsolesExists = System.console() != null;

    public Console() {
        if (!systemConsolesExists) {
            // [?] don't close, otherwise will close System.in?
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    public String readLine(String format, Object... args) throws IOException {
        if (systemConsolesExists) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        return reader.readLine();
    }

    public char[] readPassword(String format, Object... args) throws IOException {
        if (systemConsolesExists)
            return System.console().readPassword(format, args);
        return readLine(format, args).toCharArray();
    }
}
