package veinthrough.test.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.collection.CollectionToMap;
import veinthrough.api.collection.CollectionToMap.RETAIN_MANNER;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;
import veinthrough.test._class.Manager;
import veinthrough.test.guava.collection.MultiMapTest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static veinthrough.api.collection.CollectionToMap.RETAIN_MANNER.RETAIN_FIRST;
import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Key of Collectors.toMap()
 *   (1) use lambda String.charAt(0) as key
 *   (2) use Identifiable.getIdentifier() as key
 * 2. Value of Collectors.toMap()
 *   (1) item -> item
 *   (2) Function.identity()
 * 3. Duplicate key handling manner:
 *   (1) override, retain manner:
 *     <1> retain first
 *     <2> retain last
 *   (2) list
 * 4. Use Collectors.groupingBy: use list manner for duplicate key handling
 * 5. Use generic CollectionToMap:
 *   (1) non-identifiable + override + retain first/last
 *   @see CollectionToMap#toUniqueMap(Collection, Function, RETAIN_MANNER)
 *   (2) identifiable + override + retain first/last
 *   @see CollectionToMap#toUniqueMap(Collection, RETAIN_MANNER)
 *   (3) non-identifiable + list
 *   The same effect:
 *     <1> toListedMap(list, keyFunction)
 *     <2> Multimaps.index(list, keyFunction)
 *   @see CollectionToMap#toListedMap(Collection, Function)
 *   @see MultiMapTest#listToMapTest()
 *   (4) identifiable + list
 *   @see CollectionToMap#toListedMap(Collection)
 *   (5) use {@link Maps#uniqueIndex(Iterable, com.google.common.base.Function)}
 *   @see veinthrough.test.collection.TreeTest#treeMapTraverseTest
 *     1> Maps.uniqueIndex()/Multimaps.index(),
 *        @see CreationTest#mapTest()
 *        duplicate keys: IllegalArgumentException/to a list
 *     2> Maps.uniqueIndex()/CollectionToMap.toUniqueMap()
 *        @see CreationTest#mapTest()
 *        @see TreeTest#treeMapTraverseTest()
 *        改变顺序: 不改变顺序/CollectionToMap.toUniqueMap()中使用Stream/Collectors可能会改变元素顺序
 *        duplicate keys: IllegalArgumentException/RETAIN_MANNER
 * </pre>
 */
@Slf4j
public class CollectionToMapTest extends AbstractUnitTester {
    private static final Employee worker = new Employee("src/main/java/veinthrough", 60000D);
    private static final Manager cfo =
            new Manager("Sid Sneaky", 800000D, 60000D);
    private static final Manager ceo =
            new Manager("Gus Greedy", 1000000D, 80000D);

    @Override
    public void test() {
    }

    private List<String> getDataList() {
        return Lists.newArrayList("aardvark", "elephant", "koala", "eagle", "kangaroo");
    }

    // 1.(1), 2.(1)
    // duplicate key: error
    // java.lang.IllegalStateException: Duplicate key elephant
    @Test
    public void listToMapTest1() {
        log.info(methodLog(
                getDataList().stream()
                        .collect(
                                Collectors.toMap(
                                        str -> str.charAt(0),
                                        str -> str))
                        .toString()));
    }

    // 1.(1), 2.(2)
    // duplicate key: error
    // java.lang.IllegalStateException: Duplicate key elephant
    @Test
    public void listToMapTest2() {
        log.info(methodLog(
                getDataList().stream()
                        .collect(
                                Collectors.toMap(
                                        str -> str.charAt(0),
                                        Function.identity()))
                        .toString()));
    }

    // 1.(1), 2.(2), 3.(1).<2>
    // duplicate key: override
    @Test
    public void listToMapTest3() {
        log.info(methodLog(
                "map",
                getDataList().stream()
                        .collect(
                                Collectors.toMap(
                                        str -> str.charAt(0),
                                        Function.identity(),
                                        (key1, key2) -> key2))
                        .toString()));
    }

    // duplicate key: list
    // 1.(1), 3.(2), 3.
    @Test
    public void listToMapTest4() {
        log.info(methodLog(
                "map",
                getDataList().stream()
                        .collect(
                                Collectors.groupingBy(
                                        str -> str.charAt(0)))
                        .toString()));
    }

    // 1.(1), 2.(2), 3.(1).<2>
    // duplicate key: override
    // LinkedHashMap::new is the implementation of the map, which can't affect duplicate key manipulation
    @Test
    public void listToMapTest5() {
        log.info(methodLog(
                "map",
                getDataList().stream()
                        .collect(
                                Collectors.toMap(
                                        str -> str.charAt(0),
                                        Function.identity(),
                                        (key1, key2) -> key2,
                                        LinkedHashMap::new))
                        .toString()));
    }

    // 5.(1) non-identifiable + override + retain first/last
    @Test
    public void listToMapTest7() {
        log.info(methodLog(
                CollectionToMap.toUniqueMap(
                        getDataList(),
                        str -> str.charAt(0),
                        RETAIN_FIRST)
//                        RETAIN_LAST)
                        .toString()
        ));
    }

    // 5.(2) identifiable + override + retain first/last
    @Test
    public void listToMapTest9() {
        log.info(methodLog(
                CollectionToMap.toUniqueMap(
                        Lists.newArrayList(worker, ceo, cfo),
                        RETAIN_FIRST)
//                        RETAIN_LAST)
                        .toString()
        ));
    }

    // 5.(3) non-identifiable + list
    @Test
    public void listToMapTest6() {
        log.info(methodLog(
                CollectionToMap.toListedMap(
                        getDataList(),
                        str -> str.charAt(0))
                        .toString()
        ));
    }

    // 5.(4) identifiable + list
    @Test
    public void listToMapTest8() {
        log.info(methodLog(
                CollectionToMap.toListedMap(
                        Lists.newArrayList(worker, ceo, cfo))
                        .toString()
        ));
    }
}
