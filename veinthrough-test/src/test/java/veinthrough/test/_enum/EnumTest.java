package veinthrough.test._enum;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.List;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. How to write a good enum:
 *   1) value use Pair
 *   2) value use Range
 * 2. Test of print all elements in one enum
 * 3. Enum in switch clause
 * 4. Test of forValue: value -> enum
 * 5. Test of getValue: enum -> value
 * </pre>
 */
@Slf4j
public class EnumTest extends AbstractUnitTester {
    /*
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void printAllElementsTest1() {
        List<SIZE> sizes = Lists.newArrayList(
                SIZE.TOO_SMALL, SIZE.SMALL,
                SIZE.MEDIUM, SIZE.LARGE,
                SIZE.EXTRA_LARGE, SIZE.TOO_LARGE);

        for (SIZE size : sizes) {
            switch (size) {
                case TOO_SMALL:
                    System.out.println("too small" + size.getScopeString());
                    break;
                case EXTRA_LARGE:
                    System.out.println("extra large" + size.getScopeString());
                    break;
                case TOO_LARGE:
                    System.out.println("too large" + size.getScopeString());
                    break;
                default:
                    System.out.println(size.toString());
            }
        }
    }

    @Test
    public void printAllElementsTest2() {
        List<SIZE2> sizes = Lists.newArrayList(
                SIZE2.TOO_SMALL, SIZE2.SMALL,
                SIZE2.MEDIUM, SIZE2.LARGE,
                SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE);

        for (SIZE2 size : sizes) {
            System.out.println(size.toString());
        }
    }

    @Test
    public void forValueTest1() throws SIZE.InvalidSizeException {
        log.info(methodLog(" -1 is " + SIZE.forValue(-1).name()));
        log.info(methodLog("  0 is " + SIZE.forValue(0).name()));
        log.info(methodLog(" 25 is " + SIZE.forValue(25).name()));
        log.info(methodLog(" 50 is " + SIZE.forValue(50).name()));
        log.info(methodLog("100 is " + SIZE.forValue(100).name()));
    }

    @Test
    public void forValueTest2() throws SIZE2.InvalidSizeException {
        log.info(methodLog(" -1 is " + SIZE2.forValue(-1).name()));
        log.info(methodLog("  0 is " + SIZE2.forValue(0).name()));
        log.info(methodLog(" 25 is " + SIZE2.forValue(25).name()));
        log.info(methodLog(" 50 is " + SIZE2.forValue(50).name()));
        log.info(methodLog("100 is " + SIZE2.forValue(100).name()));
    }
}
