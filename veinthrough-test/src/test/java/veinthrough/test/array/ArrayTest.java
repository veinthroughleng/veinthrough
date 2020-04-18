package veinthrough.test.array;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.generic.Pair;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;
import veinthrough.test._class.Manager;

import java.util.Arrays;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. 使用Arrays.toString(str)打印数组
 * 2. 数组/多态: 数组的协变性, Employee[] staffs = managers; 所有数组都要牢记创建它们的元素类型，
 * 否则可能会导致调用不存在的域
 * 3. 数组/泛型: 数组都会牢记创建它们的元素类型, 但是类型擦除会使泛型失效, 所以不能实例化参数化类型的数组;
 * 所以对于泛型最好用collection如ArrayList
 * </pre>
 */
@Slf4j
public class ArrayTest extends AbstractUnitTester {
    @Override
    public void test() {

    }

    @Test
    @SuppressWarnings("UnnecessaryLocalVariable")
    public void covarianceTest() {
        Manager ceo =
                new Manager("Gus Greedy", 800000D, 80000D);
        Manager cfo =
                new Manager("Sid Sneaky", 600000D, 60000D);
        Employee worker = new Employee("veinthrough", 80000D);
        Manager[] managers = {ceo, cfo};
        // 2. 数组的协变性, 所以数组都要牢记创建它们的元素类型
        Employee[] staffs = managers;
        // 1. 使用Arrays.toString(str)打印数组
        log.info(methodLog("managers", Arrays.toString(managers),
                "staffs", Arrays.toString(staffs)));
        // 编译没问题，但是运行时java.lang.ArrayStoreException
        // 因为数组记住了创建它们的元素类型，所有抛出异常
        // 否则如果调用managers[0].getBonus(), 将会调用不存在的域
        staffs[0] = worker;
        log.info(methodLog("managers", Arrays.toString(managers),
                "staffs", Arrays.toString(staffs)));
    }

    @Test
    @SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions"})
    public void arrayGenericTest() {
        // 不能实例化参数化泛型的数组
//        Pair<String>[] table = new Pair<String>[10];
        // noinspection unchecked
        Pair<String>[] table = (Pair<String>[])new Pair<?>[10];
        Object[] objArray = table;
        // 编译没问题，数组都会牢记创建它们的元素类型, 所以运行时java.lang.ArrayStoreException
        objArray[0] = "hello";
        // 数组都会牢记创建它们的元素类型, 但是类型擦除会使泛型失效,
        // 所以不会有java.lang.ArrayStoreException,
        // 这就导致了不一致: Pair<String> -> Pair<Employee>
        // 所以不能实例化参数化类型的数组, 上面是通过强制转换才实例化了参数化泛型的数组
        objArray[1] = new Pair<Employee>();
    }
}
