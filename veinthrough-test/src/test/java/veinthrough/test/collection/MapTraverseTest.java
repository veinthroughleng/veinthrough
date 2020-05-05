package veinthrough.test.collection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.collection.CollectionToMap;
import veinthrough.test.AbstractUnitTester;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Function;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;
import static veinthrough.api.collection.CollectionToMap.RETAIN_MANNER.RETAIN_LAST;

/**
 * @author veinthrough
 * <p>
 * This program demonstrate different types of traversing Map/HashTable
 * <p>---------------------------------------------------------
 * <pre>
 * APIs/Tests:
 * 1. Traverse by entry set: Map.entrySet()
 * 2. Traverse by stream: Map.forEach()
 * 3. Traverse by key set: Map.keySet()
 * 4. Traverse by values: map.values()
 * 5. Traverse by keys: Dictionary.keys()
 * 6. Traverse by elements: Dictionary.elements()
 * </pre>
 */
@Slf4j
public class MapTraverseTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void mapTest() {
        Map<Character, String> map = CollectionToMap.toUniqueMap(
                Lists.newArrayList(
                        "Amy", "Bob", "Carl", "Doug", "Erica", "Frances", "Gloria"),
                str -> str.charAt(0),
                RETAIN_LAST);
        ImmutableMap.<String, Function<Map<Character, String>, String>>of(
                "Traverse by entry set", MapTraverseTest::traverseByEntrySet,
                "Traverse by stream", MapTraverseTest::traverseByStream,
                "Traverse by key set", MapTraverseTest::traverseByKeySet,
                "Traverse by values", MapTraverseTest::traverseByValues)
                .forEach((taskName, task) -> {
                    try {
                        log.info(methodLog(taskName, task.apply(map)));
                    } catch (Exception e) {
                        log.error(exceptionLog(e, taskName));
                    }
                });
    }

    @Test
    public void tableTest() {
        Hashtable<Character, String> table = new Hashtable<>(
                CollectionToMap.toUniqueMap(
                        Lists.newArrayList(
                                "Amy", "Bob", "Carl", "Doug", "Erica", "Frances", "Gloria"),
                        str -> str.charAt(0),
                        RETAIN_LAST));
        ImmutableMap.<String, Function<Hashtable<Character, String>, String>>builder()
                .put("Traverse by entry set", MapTraverseTest::traverseByEntrySet)
                .put("Traverse by stream", MapTraverseTest::traverseByStream)
                .put("Traverse by key set", MapTraverseTest::traverseByKeySet)
                .put("Traverse by values", MapTraverseTest::traverseByValues)
                .put("Traverse by keys", MapTraverseTest::traverseByKeys)
                .put("Traverse by elements", MapTraverseTest::traverseByElements)
                .build()
                .forEach((taskName, task) -> {
                    try {
                        log.info(methodLog(taskName, task.apply(table)));
                    } catch (Exception e) {
                        log.error(exceptionLog(e, taskName));
                    }
                });
    }

    /**
     * Traverse by entry set: Map.entrySet()
     */
    static <K, V> String traverseByEntrySet(Map<K, V> map) {
        StringBuilder result = new StringBuilder();
        // entry
        for (Map.Entry<K, V> entry : map.entrySet()) {
            handleKeyValue(result, entry.getKey(), entry.getValue());
        }
        return stringWithBrace(result);
    }

    /**
     * Traverse by stream: Map.forEach()
     */
    static <K, V> String traverseByStream(Map<K, V> map) {
        StringBuilder result = new StringBuilder();
        // (key, value)
        map.forEach((key, value) -> handleKeyValue(result, key, value));
        return stringWithBrace(result);
    }

    /**
     * Traverse by key set: Map.keySet()
     */
    static <K, V> String traverseByKeySet(Map<K, V> map) {
        StringBuilder result = new StringBuilder();
        // key
        map.keySet().forEach(key -> handleKeyValue(result, key, map.get(key)));
        return stringWithBrace(result);
    }

    /**
     * Traverse by values: map.values()
     */
    static <K, V> String traverseByValues(Map<K, V> map) {
        StringBuilder result = new StringBuilder();
        map.values().forEach(value -> handleKeyValue(result, value));
        return stringWithBrace(result);
    }

    /**
     * Traverse by keys: Dictionary.keys()
     */
    private static <K, V> String traverseByKeys(Dictionary<K, V> table) {
        StringBuilder result = new StringBuilder();
        // Enumeration
        Enumeration<K> enu = table.keys();
        while (enu.hasMoreElements()) {
            K key = enu.nextElement();
            handleKeyValue(result, key, table.get(key));
        }
        return stringWithBrace(result);
    }

    /**
     * Traverse by elements: Dictionary.elements()
     */
    private static <K, V> String traverseByElements(Dictionary<K, V> table) {
        StringBuilder result = new StringBuilder();
        // Enumeration
        Enumeration<V> enu = table.elements();
        while (enu.hasMoreElements()) {
            V value = enu.nextElement();
            handleKeyValue(result, value);
        }
        return stringWithBrace(result);
    }

    private static <K, V> void handleKeyValue(StringBuilder stringBuilder, K key, V value) {
        stringBuilder.append(key)
                .append(":")
                .append(value)
                .append(", ");
    }

    private static <V> void handleKeyValue(StringBuilder stringBuilder, V value) {
        stringBuilder.append(value)
                .append(", ");
    }

    private static String stringWithBrace(StringBuilder stringBuilder) {
        int length = stringBuilder.length();
        return stringBuilder.delete(length - 2, length)
                .insert(0, "{")
                .append("}")
                .toString();
    }
}
