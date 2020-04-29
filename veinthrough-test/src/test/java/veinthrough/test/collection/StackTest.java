package veinthrough.test.collection;

import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

/**
 * @author veinthrough
 * Comments:
 * 1. LinkedList/Stack/Vector:
 * 也可以将LinkedList当作栈来使用, LinkedList包含pop/push接口
 * Stack<E> extends Vector<E>, 所以Stack也可以使用Enumeration
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. traverse stack:
 * @see TraverseTest#stackTest()
 * (1) as List/Vector
 * (2) pop()/push()
 * </pre>
 */
public class StackTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    /**
     * @see TraverseTest#stackTest()
     * (1) as List/Vector
     * (2) pop()/push()
     */
    @Test
    public void stackTraverseTest() {
        new TraverseTest().stackTest();
    }
}
