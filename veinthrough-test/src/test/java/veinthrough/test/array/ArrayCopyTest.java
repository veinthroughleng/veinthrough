package veinthrough.test.array;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.array.ArrayCopy;
import veinthrough.test.AbstractUnitTester;

import java.util.Arrays;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Convert array to string: Arrays.toString(array), can't use array.toString()
 * 2. System.arraycopy()
 * 3. Arrays.copyOf()/copyOfRange()
 * 4. veinthrough.api.array.ArrayCopy.badCopyOf(): accept Object[] as argument, return Object[]
 * @see veinthrough.api.array.ArrayCopy#badCopyOf(Object[], int)
 * 5. veinthrough.api.array.ArrayCopy.copyOf(): accept Object as argument, return Object, use reflect
 * @see veinthrough.api.array.ArrayCopy#copyOf(Object, int)
 * </pre>
 *
 */
@Slf4j
public class ArrayCopyTest extends AbstractUnitTester {
    private static final int[] intArray = {1, 2, 3};
    private static final String[] stringArray = {"Tom", "Dick", "Harry"};

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void systemArrayCopyTest() {
        String[] copy = new String[stringArray.length];
        System.arraycopy(stringArray, 0, copy, 0, stringArray.length);
        // 使用Arrays.toString(str)打印数组
        log.info(methodLog("copy", Arrays.toString(copy)));
    }

    @Test
    public void arraysArrayCopyTest() {
        String[] copy = Arrays.copyOfRange(stringArray, 0, stringArray.length);
        log.info(methodLog("copy", Arrays.toString(copy)));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void badArrayCopyOfTest() {
        // int[] argument can't be converted to object[] parameter
        // can't accept int[] as argument
//        String[] copy = (String[]) ArrayCopy.badCopyOf(intArray, 10);

        log.info(methodLog("The following call will generate an exception."));
        // Object[] can't be converted to String[]
        // 从定义开始的Object[]不能转化成其他数组
        // 除非是object[]作为中间状态, 从其他数组转换成object[]再转换成其他数组
        // 如String[] -> Object[] -> String[]
        // 因为Java数组会记住每个元素的类型
        // java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.String
        String[] copy = (String[]) ArrayCopy.badCopyOf(stringArray, 10);
        log.info(methodLog("copy", Arrays.toString(copy)));
    }

    @Test
    public void goodArrayCopyOfTest() {
        // Object can be converted to int[]
        int[] copy_int = (int[]) ArrayCopy.copyOf(intArray, 10);
        log.info(methodLog("copy", Arrays.toString(copy_int)));

        // Object can be converted to String[]
        String[] copy_string = (String[]) ArrayCopy.copyOf(stringArray, 10);
        log.info(methodLog("copy", Arrays.toString(copy_string)));
    }
}
