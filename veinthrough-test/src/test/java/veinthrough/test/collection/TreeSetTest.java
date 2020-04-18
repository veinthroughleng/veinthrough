package veinthrough.test.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;

import java.util.SortedSet;
import java.util.TreeSet;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * This program demonstrates the use of a tree set.
 * Sort by Comparator is more flexible than default Comparable<T>.
 */
@Slf4j
public class TreeSetTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void treeSetTest() {
        // sort default by salary
        SortedSet<Employee> sortBySalary = new TreeSet<>();
        sortBySalary.add(new Employee("Alice Adams", 80000D));
        sortBySalary.add(new Employee("Bob Brandson", 75000D));
        sortBySalary.add(new Employee("Carl Cracker", 50000D));

        // sort by name
        SortedSet<Employee> sortByName =
                // Construct by Comparator
                new TreeSet<>((a, b) -> {
                    String nameA = a.getName();
                    String nameB = b.getName();
                    return nameA.compareTo(nameB);
                });
        sortByName.addAll(sortBySalary);

        log.info(methodLog(
                "sort default by salary", sortBySalary.toString(),
                "sort by name", sortByName.toString()));
    }
}
