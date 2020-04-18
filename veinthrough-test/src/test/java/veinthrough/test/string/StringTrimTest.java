package veinthrough.test.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * This program demonstrate how to trim blanks/0000... from a string to a number.
 */

@Slf4j
public class StringTrimTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void numberTrimTest() {
        String stream = "   00060000";

        Pattern pattern = Pattern.compile("0*",
                Pattern.CASE_INSENSITIVE);

        // trim before match
        String trimmedStream = stream.trim();
        Matcher matcher = pattern.matcher(trimmedStream);
        if (matcher.lookingAt()) {
            // the last match place
            trimmedStream = trimmedStream.substring(matcher.end());
        }
        log.info(methodLog("number string", stream,
                "trimmed number string", trimmedStream));
    }
}
