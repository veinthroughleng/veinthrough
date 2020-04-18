package veinthrough.test.c_plus_plus;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>
 * This program demonstrates parameter passing in Java,
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Methods can't modify numeric parameters.
 * 2. Methods can change the state of object parameters.
 * 3. Methods can't attach new objects to object parameters, that is, can't swap object parameters.
 * </pre>
 */
@Slf4j
public class ParamTest extends AbstractUnitTester {
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    // 1. Methods can't modify primitive parameters.
    @Test
    public void primitiveTest() {
        double percent = 10;
        log.info(methodLog("Before tripleValue(): percent=" + percent));
        tripleValue(percent);
        log.info(methodLog("After tripleValue(): percent=" + percent));
    }


    // 2. Methods can change the state of object parameters.
    @Test
    public void nonPrimitiveTest() {
        Employee harry = new Employee("Harry", 50000D);
        log.info(methodLog(
                "Before tripleValue(): Employee Harry's salary=" + harry.getSalary()));
        tripleSalary(harry);
        log.info(methodLog(
                "After tripleValue(): Employee Harry's salary=" + harry.getSalary()));
    }

    // Test 3: Methods can't attach new objects to object parameters, that is, can't swap object parameters.
    @Test
    public void nonPrimitiveTest2() {
        Employee Alice = new Employee("Alice", 70000D);
        Employee Bob = new Employee("Bob", 60000D);
        log.info(methodLog("Before swap",
                "Alice", Alice.toString(),
                "Bob", Bob.toString()));
        swap(Alice, Bob);
        log.info(methodLog("After swap",
                "Alice", Alice.toString(),
                "Bob", Bob.toString()));
    }

    @SuppressWarnings("all")
    private static void tripleValue(double x) {
        x = 3 * x;
    }

    private static void tripleSalary(Employee x) {
        x.raiseSalary(200);
    }

    @SuppressWarnings("all")
    private static void swap(Employee item1, Employee item2) {
        Employee temp = item1;
        item1 = item2;
        item2 = temp;
    }
}
