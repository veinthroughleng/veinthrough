package veinthrough.api.string;

import java.util.Optional;

public class Regex {
    // mac pattern: delim can be ":" or "-"
    private static final String macPattern = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}|([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";

    public static boolean isMacAddress(String str) {
        return str != null && str.matches(macPattern);
    }

    public static Optional<Character> delimOfMacAddress(String str) {
        return !isMacAddress(str) ? Optional.empty() :
                str.contains(":") ?
                        Optional.of(':') :
                        Optional.of('-');
    }
}
