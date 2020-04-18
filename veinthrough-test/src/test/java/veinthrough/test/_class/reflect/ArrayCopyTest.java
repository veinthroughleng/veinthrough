package veinthrough.test._class.reflect;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * This program demonstrates the use of reflection for manipulating arrays.
 *
 * @author Veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. veinthrough.api.array.ArrayCopy.badCopyOf(): accept Object[] as argument, return Object[]
 * @see veinthrough.test.array.ArrayCopyTest#badArrayCopyOfTest()
 * @see veinthrough.api.array.ArrayCopy#badCopyOf(Object[], int)
 * 2. veinthrough.api.array.ArrayCopy.copyOf(): accept Object as argument, return Object, use reflect
 * @see veinthrough.test.array.ArrayCopyTest#goodArrayCopyOfTest()
 * @see veinthrough.api.array.ArrayCopy#copyOf(Object, int)
 * </pre>
 */
@Slf4j
public class ArrayCopyTest {
    @Test
    public void badArrayCopyOfTest() {
        new veinthrough.test.array.ArrayCopyTest().badArrayCopyOfTest();
    }

    @Test
    public void goodArrayCopyOfTest() {
        new veinthrough.test.array.ArrayCopyTest().goodArrayCopyOfTest();
    }
}
