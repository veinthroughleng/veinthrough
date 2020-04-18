package veinthrough.test.env;

import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import static veinthrough.api.util.Constants.*;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * Get null dev of different OS:
 * Linux: /dev/null
 * Windows: NUL:
 * Others: jnk
 */
@Slf4j
public class NullDev extends AbstractUnitTester {

    @Override
    public void test() {
    }

    @Test
    public void nullDevTest() {
        log.info(methodLog("Null dev", getNullDev()));
    }

    private static String getNullDev() {
        String osName = System.getProperty("os.name");
        System.out.println("OS name: " + osName);
        if (new File(UNIX_NULL_DEV).exists()) {
            return UNIX_NULL_DEV;
        } else if (osName.startsWith("Windows")) {
            return WINDOWS_NULL_DEV;
        }
        return FAKE_NULL_DEV;
    }
}