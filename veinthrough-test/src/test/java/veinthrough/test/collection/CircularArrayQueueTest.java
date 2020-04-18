package veinthrough.test.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.collection.CircularArrayQueue;
import veinthrough.test.AbstractUnitTester;

import java.util.Queue;

import static veinthrough.api.util.MethodLog.methodLog;


/**
 * @author veinthrough
 */
@Slf4j
public class CircularArrayQueueTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void circularArrayQueueTest() {
        Queue<String> q = new CircularArrayQueue<>(5);
        q.add("Amy");
        q.add("Bob");
        q.add("Carl");
        q.add("Deedee");
        q.add("Emile");
        q.remove();
        q.add("Fifi");
        q.remove();
        log.info(methodLog(q.toString()));
    }
}
