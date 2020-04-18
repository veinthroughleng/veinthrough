package veinthrough.api.lang;

/**
 * @author Ian Darwin
 * <p>
 * A GetOptDesc describes one argument that may be accepted by the program.
 */
public class GetOptDesc {
    /**
     * The short-form option letter
     */
    private char argLetter;
    /**
     * The long-form option name
     */
    private String argName;
    /**
     * True if this option needs an argument after it
     */
    private boolean takesArgument;

    /**
     * Construct a GetOpt option.
     *
     * @param ch The single-character name for this option.
     * @param nm The word name for this option.
     * @param ta True if this option requires an argument after it.
     */
    public GetOptDesc(char ch, String nm, boolean ta) {
        if (!Character.isLetter(ch) && !Character.isDigit(ch)) {
            throw new IllegalArgumentException(ch + ": not letter or digit");
        }
        argLetter = ch;
        argName = nm; // may be null, meaning no long name.
        takesArgument = ta;
    }

    char getArgLetter() {
        return argLetter;
    }

    String getArgName() {
        return argName;
    }

    boolean takesArgument() {
        return takesArgument;
    }
}