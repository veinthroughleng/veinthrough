package veinthrough.test._class;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.Arrays;
import java.util.Objects;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * This program demonstrates the equals method.
 *
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. 直接用==
 * 2. equals
 * @see Employee#equals(Object)
 * @see Employee#equals2(Object)
 * @see Manager#equals(Object)
 * 3. Objects.equals: 除了null-safe, 实际上调用的还是对象的equals
 * 4. Arrays.equals
 * </pre>
 */
@Slf4j
public class EqualsTest extends AbstractUnitTester {
    private static final Employee alice1 = new Employee("Alice Adams", 75000D);
    private static final Employee alice2 = alice1;
    private static final Employee alice3 = new Employee("Alice Adams", 75000D);
    private static final Employee bob = new Employee("Bob Brandson", 50000D);
    private static final Manager carl = new Manager("Carl Cracker", 80000D, 80000D);
    private static final Manager boss = new Manager("Carl Cracker", 80000D, 80000D);

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void operatorDirectEqualTest() {
        log.info(methodLog("alice1 == alice2: " + (alice1 == alice2)));
        log.info(methodLog("alice1 == alice3: " + (alice1 == alice3)));
    }

    // null-unsafe
    @Test
    public void objectEqualsTest() {
        log.info(methodLog("alice1.equals(alice3): " + alice1.equals(alice3)));
        log.info(methodLog("alice1.equals(bob): " + alice1.equals(bob)));
        boss.setBonus(5000D);
        log.info(methodLog("carl.equals(boss): " + carl.equals(boss)));
    }

    // Objects.equals(): null-safe
    @Test
    @SuppressWarnings("all")
    public void objectsStaticEqualsTest() {
        log.info(methodLog("alice1.equals(alice3): " + Objects.equals(alice1, alice3)));
        log.info(methodLog("alice1.equals(bob): " + Objects.equals(alice1, bob)));
        log.info(methodLog("carl.equals(boss): " + Objects.equals(carl, boss)));
        Manager nullManager = null;
        log.info(methodLog("carl.equals(boss): " + Objects.equals(carl, nullManager)));
    }

    @Test
    public void arraysEqualsTest() {
        Manager[] managers = {carl, boss};
        Employee[] employees = {carl, boss};
        Employee[] workers = {carl, bob};
        log.info(methodLog("managers/employees", "" + Arrays.equals(managers, employees),
                "employees/workers", "" + Arrays.equals(employees, workers)));
    }

    @Test
    public void toStringTest() {
        log.info(methodLog("bob", bob.toString(),
                "boss", boss.toString()));
    }

    @Test
    public void hashCodeTest() {
        log.info(methodLog("Hashcode of alice1", String.valueOf(alice1.hashCode()),
                "Hashcode of alice3", String.valueOf(alice3.hashCode()),
                "Hashcode of bob", String.valueOf(bob.hashCode()),
                "Hashcode of carl", String.valueOf(carl.hashCode())));
    }
}