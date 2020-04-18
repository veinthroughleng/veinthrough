package veinthrough.test._class.reflect;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static veinthrough.api.util.MethodLog.*;

/**
 * This program shows how to invoke methods through reflection.
 *
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 * [Class]        Method getMethod(String name, Class<?>... parameterTypes)
 * [Method]        Object invoke(Object obj, Object... args):
 *                  调用static函数的第一个参数为null，否则第一个参数为调用该方法的对象
 * </pre>
 */
@Slf4j
public class MethodInvokerTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void methodInvokerTest() {
        try {
            // get method pointers to the square and sqrt methods
            Method square = MethodInvokerTest.class.getMethod("square", double.class);
            Method sqrt = Math.class.getMethod("sqrt", double.class);

            // print tables of x- and y-values
            printTable(1, 10, 1, square);
            printTable(2, 100, 10, sqrt);
        } catch (NoSuchMethodException | SecurityException e) {
            log.error(exceptionLog(e));
        }
    }

    // should be public
    // otherwise: NoSuchMethodException
    @SuppressWarnings("WeakerAccess")
    public static double square(double x) {
        return x * x;
    }

    /**
     * Prints a table with x- and y-values for a method
     *
     * @param from the lower bound for the x-values
     * @param to   the upper bound for the x-values
     * @param slice    the slice between rows in the table
     * @param f    a method with a double parameter and double return value
     */
    private static void printTable(double from, double to, int slice, Method f) {
        // print out the method as table header
        log.info(methodLog(f.toGenericString()));

        for (double x = from; x <= to; x += slice) {
            try {
                //when calling static method, the first argument is null
                double y = (Double) f.invoke(null, x);
                System.out.printf("%10.4f | %10.4f\n", x, y);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(exceptionLog(e));
            }
        }
    }
}
