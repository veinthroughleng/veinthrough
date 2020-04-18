package veinthrough.test.guava.collection;

import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._enum.SIZE2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * </pre>
 */
@Slf4j
public class CollectionCreation extends AbstractUnitTester {

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void listTest() {
        log.info(methodLog(newList1(newArray()).toString()));
        log.info(methodLog(newList2(newArray()).toString()));
    }

    @Test
    public void setTest() {
        log.info(methodLog(newSet2(newArray()).toString()));
    }

    @Test
    public void immutableListTest() {
        log.info(methodLog(newList3(newArray()).toString()));
        log.info(methodLog(newList4().toString()));
    }

    @Test
    public void immutableSetTest() {
        log.info(methodLog(newSet3(newArray()).toString()));
        log.info(methodLog(newSet4().toString()));
    }

    @Test
    public void mapTest() {
        log.info(methodLog(newMap().toString()));
    }

    private SIZE2[] newArray() {
        return new SIZE2[]{
                SIZE2.TOO_SMALL, SIZE2.SMALL,
                SIZE2.MEDIUM, SIZE2.LARGE,
                SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE};
    }

    // 1.(1) only return a view of list from array
    // modify(add/remove) the array will cause UnsupportedOperationException
    private List<SIZE2> newList1(SIZE2... sizesArray) {
        return Arrays.asList(sizesArray);
    }

    // 1.(2) list
    private List<SIZE2> newList2(SIZE2... sizesArray) {
        return Lists.newArrayList(sizesArray);
    }

    // 1.(2) set
    private Set<SIZE2> newSet2(SIZE2... sizesArray) {
        return Sets.newHashSet(sizesArray);
    }

    // 1.(3) list
    private List<SIZE2> newList3(SIZE2... sizesArray) {
        return ImmutableList.copyOf(sizesArray);
    }

    // 1.(3) set
    private Set<SIZE2> newSet3(SIZE2... sizesArray) {
        return ImmutableSet.copyOf(sizesArray);
    }

    // 1.(4) list
    private List<SIZE2> newList4() {
        return ImmutableList.of(
                SIZE2.TOO_SMALL, SIZE2.SMALL,
                SIZE2.MEDIUM, SIZE2.LARGE,
                SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE);
    }

    // 1.(4) set
    private Set<SIZE2> newSet4() {
        return ImmutableSet.of(
                SIZE2.TOO_SMALL, SIZE2.SMALL,
                SIZE2.MEDIUM, SIZE2.LARGE,
                SIZE2.EXTRA_LARGE, SIZE2.TOO_LARGE);
    }

    // 2.
    private Map<Integer, String> newMap() {
        return ImmutableMap.<Integer, String>builder()
                .put(1, "red")
                .put(2, "green")
                .put(3, "blue")
                .build();
    }
}
