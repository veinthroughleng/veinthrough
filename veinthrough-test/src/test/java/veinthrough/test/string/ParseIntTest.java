package veinthrough.test.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import static veinthrough.api.util.MethodLog.methodLog;

@Slf4j
public class ParseIntTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void parseIntTest() {
        String str = "ffffff00";
        // use parseInt, will overflow
        // java.lang.NumberFormatException
        log.info(methodLog(
                String.format("%s: %#x", str, Integer.parseInt( str, 16))));
    }

    @Test
    public void parseUnsignedIntTest() {
        String str = "ffffff00";
        // use parseUnsignedInt
        log.info(methodLog(
                String.format("%s: %#x", str, Integer.parseUnsignedInt( str, 16))));
    }
}
