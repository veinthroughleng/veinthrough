package veinthrough.test.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 * 1. Iterator/ListIterator:
 *   (1) hasNext()/next()/remove()
 *   (2) set()/add()/hasPrevious()/previous()/nextIndex()/previousIndex()
 *   NOTE: ArrayList/LinkedList都具有Iterator()/ListIterator(), 不要把LinkedList和ListIterator混淆
 * @see TraverseTest#traverseByIterator(List)
 * 2. add()/set()/remove()
 *   (1) add(): 只依赖于迭代器的位置,
 *   Inserts the specified element into the list (optional operation)
 *   (2) set()/remove(): 依赖于迭代器的位置和状态(previous/next)
 *   Replaces the last element returned by {@link ListIterator#next} or {@link ListIterator#previous}
 *   with the specified element (optional operation)
 * 3. LinkedList/ArrayList效率问题, LinkedList擅长于修改, ArrayList擅长于random access, 效率最高
 * @see FailFastTest#mergeTest(), 使用LinkedList做修改
 * @see TraverseTest#traverseByRandomAccess(List), 使用ArrayList做random access
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Merge test.
 * 2. LinkedList当作AbstractSequentialList(双向列表)/Deque(队列)/Stack来使用
 * @see TraverseTest#traverseByRemove(LinkedList)
 * @see TraverseTest#traverseByPoll(LinkedList)
 * @see TraverseTest#traverseByPop(LinkedList)
 * </pre>
 */
@Slf4j
public class LinkedListTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    /**
     * @see FailFastTest#mergeTest()
     * <p>
     * LinkedList/ListIterator更擅长修改, 而ArrayList更擅长随机访问
     */
    @Test
    public void mergeTest() {
    }
}
