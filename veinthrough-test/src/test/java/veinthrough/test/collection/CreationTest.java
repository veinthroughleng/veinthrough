package veinthrough.test.collection;

import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._enum.SIZE2;

import java.util.*;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. new a list/set
 *   (1) Arrays.asList(array): only return a view of list from array
 *   (2) Lists.newArrayList(...)/ Sets.newHashSet(...)
 *   (3) ImmutableList.copyOf(array/iterable/collection)/ ImmutableSet.copyOf(array/iterable/collection)
 *   (4) ImmutableList.of(element...)/ ImmutableSet.of(element...)
 *   modify(add/remove) the array will cause UnsupportedOperationException
 * 2. new a map
 *   (1) ImmutableMap.builder()
 *   (2) keys + function  -->  map
 *     1> Maps.asMap(keysSet, function), use set as keys
 *     2> Maps.toMap(keysIterable, function), use iterable as keys
 *   (3) values + function  -->  map
 *   @see CollectionToMapTest
 * </pre>
 */
@Slf4j
public class CreationTest extends AbstractUnitTester {

    private static final SIZE2[] sizesArray = new SIZE2[]{
            SIZE2.TOO_SMALL, SIZE2.SMALL,
            SIZE2.MEDIUM, SIZE2.LARGE,
            SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE};

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void listTest() {
        log.info(methodLog(
                // 1.(1) only return a view of list from array
                // modify(add/remove) the array will cause UnsupportedOperationException
                "array view", "" + Arrays.asList(sizesArray),
                "new from array", "" + Lists.newArrayList(sizesArray),
                "copy of array", "" + ImmutableList.copyOf(sizesArray),
                "of elements", "" + ImmutableList.of(
                        SIZE2.TOO_SMALL, SIZE2.SMALL,
                        SIZE2.MEDIUM, SIZE2.LARGE,
                        SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE)));
    }

    @Test
    public void setTest() {
        log.info(methodLog(
                "new from array", "" + Sets.newHashSet(sizesArray),
                "copy of array", "" + ImmutableSet.copyOf(sizesArray),
                "of elements", "" + ImmutableSet.of(
                        SIZE2.TOO_SMALL, SIZE2.SMALL,
                        SIZE2.MEDIUM, SIZE2.LARGE,
                        SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE)));
    }

    @Test
    public void mapTest() {
        List<Integer> keysList = ImmutableList.of(1, 2, 3, 3);
        Set<Integer> keysSet = new HashSet<>(keysList);
        List<String> valuesList = ImmutableList.of("1", "2", "3");
        // noinspection unused
        List<String> duplicateValuesList = ImmutableList.of("1", "2", "3", "3");
        log.info(methodLog(
                "builder", "" + ImmutableMap.<Integer, String>builder()
                        .put(1, "1")
                        .put(2, "2")
                        .put(3, "3")
                        .build(),
                "set as keys", "" + Maps.asMap(keysSet, String::valueOf),
                "iterable as keys", "" + Maps.toMap(keysList, String::valueOf),
                // IllegalArgumentException, as duplicate key
//                "iterable as values", ""+ Maps.uniqueIndex(duplicateValuesList, Integer::valueOf),
                "iterable as values", ""+ Maps.uniqueIndex(valuesList, Integer::valueOf)));
    }
}
