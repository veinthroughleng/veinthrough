package veinthrough.test.generic;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.generic.Pair;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;
import veinthrough.test._class.Manager;

import java.util.Arrays;
import java.util.List;

import static veinthrough.api.generic.PairAlg.minMax;
import static veinthrough.api.generic.PairAlg.minMax2;
import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. XX = <? extends XX> is OK
 * 2. <? extends XX> = XX is NOT OK
 * 3. <? super XX> = XX is OK
 * 4. XX = <? super XX> is NOT OK
 * 5. <T extends Comparable<? super T>比较 <T extends Comparable<T>
 *  由于Employee implements Comparable<Employee> 和  Manager extends Employee;
 *  所以Manager 是 Comparable<Employee>的子类，Manager可以赋值给Comparable<? super Manager>；
 *  所以Manager不是Comparable<Manager>的子类，Manger不能赋值给Comparable<Manager>。
 *  Calendar与GregorianCalendar也是同样效果。
 * </pre>
 */
@Slf4j
public class GenericTest extends AbstractUnitTester {
    private static final Manager ceo =
            new Manager("Gus Greedy", 800000D, 80000D);
    private static final Manager cfo =
            new Manager("Sid Sneaky", 600000D, 60000D);
    private static final Employee worker = new Employee("src/main/java/veinthrough", 80000D);
    private static final Manager[] managers = {ceo, cfo};

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    // 1. XX = <? extends XX> is OK
    // 2. <? extends XX> = XX is NOT OK
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused"})
    public void wildcardExtendsTest() {
        Pair<? extends Employee> ceo_cfo = new Pair<>(ceo, cfo);
        printBuddies(ceo_cfo);

        // [?] 2. <? extends XX> = XX is NOT OK ?
        // [?] newArrayList(Employee)可以, 参数解释为Employee[], 数组特殊?
        // [?] 甚至有可能Collection/Iterable都特殊?
        // [?] 但是add(Employee)不可以, 参数解释为Employee
        List<? extends Employee> extendsEmployeeList = Lists.newArrayList(worker);
        // 2. <? extends XX> = XX is NOT OK
        // 2. <? extends Employee> is unable used to write
        // Employee can't be wrote to <? extends Employee>
//        extendsEmployeeList.add(worker);

        // 1. <? extends Employee> is used to read
        // Employee can read from <? extends Employee>
        Employee cxo = ceo_cfo.getFirst();
        log.info(methodLog(cxo.toString()));
    }

    // 3. <? super XX> = XX is OK
    @Test
    @SuppressWarnings({"unchecked", "all"})
    public void wildcardSuperTest() {
        List<Manager> managersList = Arrays.asList(managers);
        Pair<? super Manager> result = new Pair<>();

        // [OrderingTest].3. natural(), 使用既有的compareTo() from Comparable<T>
        // [OrderingTest].6. onResultOf(): 对集合中元素调用 Function, 再按返回值用当前排序器排序
        // [OrderingTest].9. fluentIterable:链式操作, 链式调用产生的排序器时, 应该从后往前读;
        //               是因为每次链式调用都是用后面的方法包装了前面的排序
        // [OrderingTest].10.min()/max(): 使用生成的ordering来计算min/max
        Ordering<Manager> ordering =
                Ordering.natural().onResultOf(
                        Manager::getBonus);

        // 3. <? super Manager> is used to write
        // Pair<Manager> can be wrote to <? super Manager>
        result.setFirst(ordering.min(managersList));
        result.setSecond(ordering.max(managersList));
        printBuddies((Pair<Manager>) result);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void comparableTest() {
        // 5. <T extends Comparable<? super T>比较 <T extends Comparable<T>
        //  由于Employee implements Comparable<Employee> 和  Manager extends Employee
        //  所以Manager 是 Comparable<Employee>的子类，Manager可以赋值给Comparable<? super Manager>
        //  所以Manager不是Comparable<Manager>的子类，Manger不能赋值给Comparable<Manager>
        //  Calendar与GregorianCalendar也是同样效果

        // Manager是 Comparable<Employee>的子类
        // Manager可以赋值给Comparable<? super Manager>
        log.info(methodLog(minMax(managers).toString()));
        // [?] Manger不能赋值给Comparable<Manager>
        // [?] 使用ceo,cfo作为参数不行, 参数解释为Manager
        // [?] 但使用managers就可以, 参数解释为Manager[], 数组特殊?
        // [?] 甚至有可能Collection/Iterable都特殊?
        log.info(methodLog(minMax2(managers).toString()));
        // Manager不是Comparable<Manager>的子类
        // Manger不能赋值给Comparable<Manager>
//        log.info(methodLog(minMax2(ceo, cfo).toString()));
    }

    private void printBuddies(Pair<? extends Employee> p) {
        // 1. <? extends Employee> is used to read
        // Employee can read from <? extends Employee>
        Employee first = p.getFirst();
        Employee second = p.getSecond();
        log.info(methodLog(
                "[" + first.getName() + "," + second.getName() + "]"));
    }
}
