package veinthrough.test.guava.collection;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.guava.SourceAndSinkTest;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 *   1. [static] HashMultiset.create()
 *   2. add(item)/ add(item, occurrences)/ addAll(Collection)
 *   3. contains(item)/ containsAll(collection)
 *   4. [static] Multisets.containsOccurrences(multiset1, multiset2)
 *   5. remove(item)/ remove(item, occurrences)/ removeAll(collection)
 *   6. elementSet()/ entrySet()
 *   7. [static] Multisets.copyHighestCountFirst(multiset)
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 *   1. count function: main function
 *   @see #wordCountTest()
 *   @see #wordCountAndSortTest()
 *   @see SourceAndSinkTest#readTest()
 *   2. sort function
 *   @see #wordCountAndSortTest()
 *   3. contains test:
 *     (1) containsAll(), 只考虑元素种类, 不考虑每个元素的count
 *     (2) containsOccurrences(), 考虑每个元素的count
 *   4. remove test
 *     (1) remove(element)/remove(element, count)
 *     (2) removeAll(), 不考虑每个元素的count, 移除每个元素的所有occurrences
 * </pre>
 */
@Slf4j
public class MultisetTest extends AbstractUnitTester {
    private static final String words = "dr|wer|dfd|dd|dfd|dda|dfd|de|dr";
    private static final String word = "abc";
    private static final Multiset<String> multiset1 = HashMultiset.create();
    private static final Multiset<String> multiset2 = HashMultiset.create();

    static {
        multiset1.add(word, 2);
        multiset2.add(word, 6);
    }

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void wordCountTest() {
        Multiset<String> wordsMultiset = HashMultiset.create();
        wordsMultiset.addAll(
                Lists.newArrayList(
                        words.split("\\|")));

        wordsMultiset.elementSet()
                .forEach(key ->
                        System.out.println(key + " count: " + wordsMultiset.count(key)));
    }

    @Test
    public void wordCountAndSortTest() {
        Multiset<String> wordsMultiset = HashMultiset.create();
        wordsMultiset.addAll(
                Lists.newArrayList(
                        words.split("\\|")));
        // 使用highestCountFirst排列元素
        // noinspection UnstableApiUsage
        Multisets.copyHighestCountFirst(wordsMultiset)
                .elementSet()
                .forEach(key ->
                        System.out.println(key + " count: " + wordsMultiset.count(key)));
    }

    @Test
    public void containsTest() {
        log.info(methodLog(
                "multiset1", multiset1.toString(),
                "multiset2", multiset2.toString()
        ));

        // return true, 因为包含了所有不重复元素
        log.info(methodLog(
                "multiset1.containsAll(multiset2): " +
                        multiset1.containsAll(multiset2)
        ));

        // return false, multiset1包含2个word，而multiset2包含6个word
        log.info(methodLog(
                "Multisets.containsOccurrences(multiset1, multiset2): " +
                        Multisets.containsOccurrences(multiset1, multiset2)
        ));

        // return true, multiset2包含6个word, multiset1包含2个word
        // multiset2包含multiset1的所有occurrences
        log.info(methodLog(
                "Multisets.containsOccurrences(multiset2, multiset1): " +
                        Multisets.containsOccurrences(multiset2, multiset1)
        ));
    }

    @Test
    public void removeTest() {
        log.info(methodLog(
                0,
                "multiset1", multiset1.toString(),
                "multiset2", multiset2.toString()
        ));

        // multiset2 现在包含3个"a"
        multiset2.remove(word, 3);
        log.info(methodLog(
                1,
                "After multiset2.remove(word, 3)",
                "multiset2", multiset2.toString()
        ));

        // multiset2移除所有"a", 虽然multiset1只有2个"a"
        multiset2.removeAll(multiset1);
        log.info(methodLog(
                2,
                "After multiset2.removeAll(multiset1)",
                "multiset2", multiset2.toString()
        ));

        // return true
        log.info(methodLog(
                "multiset2.isEmpty(): " + multiset2.isEmpty()
        ));
    }
}
