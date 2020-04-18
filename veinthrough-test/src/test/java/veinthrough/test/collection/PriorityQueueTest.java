package veinthrough.test.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.PriorityQueue;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * This program demonstrates the use of a priority queue.
 */
@Slf4j
public class PriorityQueueTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void priorityQueueTest() {
        PriorityQueue<GregorianCalendar> pq = new PriorityQueue<>();
        pq.add(new GregorianCalendar(1906, Calendar.DECEMBER, 9)); // G. Hopper
        pq.add(new GregorianCalendar(1815, Calendar.DECEMBER, 10)); // A. Lovelace
        pq.add(new GregorianCalendar(1903, Calendar.DECEMBER, 3)); // J. von Neumann
        pq.add(new GregorianCalendar(1910, Calendar.JUNE, 22)); // K. Zuse

        System.out.println("Iterating over elements...");
        for (GregorianCalendar date : pq)
            log.info(methodLog(
                    "" + date.get(Calendar.YEAR)));
        System.out.println("Removing elements...");
        while (!pq.isEmpty())
            log.info(methodLog(
                    "" + pq.remove().get(Calendar.YEAR)));
    }
}
