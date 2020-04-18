package veinthrough.api.lang;

import lombok.Getter;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * @author Ian Darwin
 * Bare-minimum String formatter.
 */
@SuppressWarnings("unused")
public class StringAlign extends Format {

    private static final long serialVersionUID = 1L;

    public enum Justify {
        /* Constant for left justification. */
        LEFT,
        /* Constant for centering. */
        CENTER,
        /* Constant for right-justified Strings. */
        RIGHT,
    }

    /* Current justification */
    @Getter private Justify just;
    /* Current max length */
    @Getter private int maxChars;

    /**
     * Construct a StringAlign formatter; length and alignment are
     * passed to the Constructor instead of each format() call as the
     * expected common use is in repetitive formatting e.g., page numbers.
     *
     * @param maxChars - the maximum length of the output
     * @param just     - one of the enum values LEFT, CENTER or RIGHT
     */
    public StringAlign(int maxChars, Justify just) {
        switch (just) {
            case LEFT:
            case CENTER:
            case RIGHT:
                this.just = just;
                break;
            default:
                throw new IllegalArgumentException("invalid justification arg.");
        }
        if (maxChars < 0) {
            throw new IllegalArgumentException("maxChars must be positive.");
        }
        this.maxChars = maxChars;
    }

    /**
     * Format a String.
     *
     * @param input  - the methodLog to be aligned.
     * @param ignore - a FieldPosition (may be null, not used but
     *               specified by the general contract of Format).
     * @param where - the StringBuffer to append it to.
     */
    public StringBuffer format(
            Object input, StringBuffer where, FieldPosition ignore) {

        String s = input.toString();
        String wanted = s.substring(0, Math.min(s.length(), maxChars));

        // Get the spaces in the right place.
        switch (just) {
            case RIGHT:
                pad(where, maxChars - wanted.length());
                where.append(wanted);
                break;
            case CENTER:
                int toAdd = maxChars - wanted.length();
                pad(where, toAdd / 2);
                where.append(wanted);
                pad(where, toAdd - toAdd / 2);
                break;
            case LEFT:
                where.append(wanted);
                pad(where, maxChars - wanted.length());
                break;
        }
        return where;
    }

    @SuppressWarnings("WeakerAccess")
    protected final void pad(StringBuffer to, int howMany) {
        for (int i = 0; i < howMany; i++)
            to.append(' ');
    }

    /**
     * Convenience Routine
     */
    String format(String s) {
        return format(s, new StringBuffer(), null).toString();
    }

    /**
     * ParseObject is required, but not useful here.
     */
    public Object parseObject(String source, ParsePosition pos) {
        return source;
    }
}