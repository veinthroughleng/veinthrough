package veinthrough.test.collection;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;
import veinthrough.test._class.Manager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. shuffle and sort test
 * 2. set view of map test
 * 3. serveral kinds of view tests
 * </pre>
 */
@Slf4j
public class CollectionBasicTest extends AbstractUnitTester {

    @Override
    public void test() {
    }

    @Test
    public void shuffleAndSortTest() {
        List<Integer> numbers = IntStream.range(1, 50)
                .boxed()
                .collect(Collectors.toList());
        for (int i = 1; i <= 49; i++)
            numbers.add(i);
        // shuffle
        Collections.shuffle(numbers);
        List<Integer> winningCombination = numbers.subList(0, 6);
        // sort
        Collections.sort(winningCombination);
        System.out.println(winningCombination);
    }

    @Test
    public void setViewOfMapTest() {
        // map
        Map<Integer, String> mapA = ImmutableMap.<Integer, String>builder()
                .put(1, "1")
                .put(2, "2")
                .put(3, "3")
                .build();
        // set view
        Set<Integer> setA = mapA.keySet();
        Set<Integer> setB = new HashSet<>(setA);
        Set<Integer> setC = new HashSet<>();
        // noinspection CollectionAddAllCanBeReplacedWithConstructor
        setC.addAll(setA);
        log.info(methodLog(0,
                "mapA", "" + mapA,
                "setA", "" + setA,
                "setB", "" + setB,
                "setC", "" + setC));

        // remove setA will affect mapA
        // java.lang.UnsupportedOperationException
        setA.removeAll(setB);
        log.info(methodLog(1,
                "After remove setB from setA",
                "mapA", "" + mapA,
                "setA", "" + setA,
                "setB", "" + setB,
                "setC", "" + setC));
    }

    @Test
    @SuppressWarnings("unused")
    public void viewTest() {
        Employee[] employees = {new Employee("Alice Adams", 80000D),
                new Employee("Bob Brandson", 75000D),
                new Employee("Carl Cracker", 50000D),
                new Manager("Carl Cracker", 80000D, 80000D)};
        // list view of array
        // 改变数组大小的所以方法都会导致UnsupportedOperationException
        List<Employee> employeesList = Arrays.asList(employees);

        // nCopies view
        List<String> settings = Collections.nCopies(100, "DEFAULT");

        // singleton view
        Set<Employee> worker = Collections.singleton(new Employee("Alice Adams", 80000D));

        // sub range view
        List<Employee> workers = employeesList.subList(0, 3);

        // unmodifiable view
        // Can use ImmutableList from Guava, which is not a view.
        // CollectionCreation#newList3(SIZE2...)
        List<Employee> unmodifiableWorkersList = Collections.unmodifiableList(workers);

        // synchronized view
        List<Employee> synchronizedWorkersList = Collections.synchronizedList(workers);
    }
}
