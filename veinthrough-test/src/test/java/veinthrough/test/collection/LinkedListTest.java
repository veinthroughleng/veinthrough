package veinthrough.test.collection;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 * 1. Iterator/ListIterator:
 *   (1) hasNext()/next()/remove()
 *   [?] 没有add()因为数组实现add()效率不高?
 *   [?] 没有set()因为set依赖于状态, 但是remove()也依赖于状态?
 *   (2) set()/add()/hasPrevious()/previous()/nextIndex()/previousIndex()
 * 2. add()/set()/remove()
 *   (1) add(): 只依赖于迭代器的位置,
 *   Inserts the specified element into the list (optional operation)
 *   (2) set()/remove(): 依赖于迭代器的位置和状态(previous/next)
 *   Replaces the last element returned by {@link ListIterator#next} or {@link ListIterator#previous}
 *   with the specified element (optional operation)
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Merge test.
 * 2. Traverse linked list concurrently with modifying(add/remove) the list:
 *   (1) iterator is from iterator()
 *   (2) iterator is from listIterator()
 * 3. Bulk operation test.
 */
@Slf4j
public class LinkedListTest extends AbstractUnitTester {
    private static final List<String> list1 =
            Lists.newLinkedList(
                    Lists.newArrayList("Amy", "Carl", "Erica"));
    private static final List<String> list2 =
            Lists.newLinkedList(
                    Lists.newArrayList("Bob", "Doug", "Frances", "Gloria"));

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void mergeTest() {
        ListIterator<String> iter1 = list1.listIterator();
        for (String s : list2) {
            if (iter1.hasNext()) iter1.next();
            iter1.add(s);
        }
        // [Amy, Bob, Carl, Doug, Erica, Frances, Gloria]
        log.info(methodLog(list1.toString()));
    }

    // Can traverse linked list concurrently with modifying(add/remove) the list.
    // 2.(1) iterator is from iterator()
    @Test
    public void iterateAndRemoveTest1() {
        // merge
        mergeTest();

        // iterate list1 by iterator
        iterateAndRemove(list1.iterator());
        log.info(methodLog(list1.toString()));
    }

    // Can traverse linked list concurrently with modifying(add/remove) the list.
    // 2.(2) iterator is from listIterator()
    @Test
    public void iterateAndRemoveTest2() {
        // merge
        mergeTest();

        // iterate list1 by list iterator
        iterateAndRemove(list1.listIterator());
        log.info(methodLog(list1.toString()));
    }

    @Test
    public void bulkOperationTest() {
        // merge
        mergeTest();

        // remove all words in list2 from list1
        list1.removeAll(list2);
        log.info(methodLog(list1.toString()));
    }

    // remove every second word from list2
    private void iterateAndRemove(Iterator iterator) {
        while (iterator.hasNext()) {
            iterator.next(); // skip one element
            if (iterator.hasNext()) {
                iterator.next(); // skip next element
                iterator.remove(); // remove that element
            }
        }
    }
}
