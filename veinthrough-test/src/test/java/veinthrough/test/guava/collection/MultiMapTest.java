package veinthrough.test.guava.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.collection.ListToMap;
import veinthrough.test.AbstractUnitTester;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Convert a list to a map, the same effect:
 *   (1) by multimap: Multimaps.index(list, keyFunction)
 *   (2) by ListToMap: toListedMap(list, keyFunction)
 *     @see ListToMap#toListedMap(List, Function)
 *     @see ListToMapTest#listToMapTest6()
 * </pre>
 */
@Slf4j
public class MultiMapTest extends AbstractUnitTester {
    private static final String[] animals =
            new String[]{"aardvark", "elephant", "koala", "eagle", "kangaroo"};

    @Override
    public void test() {
    }

    @Test
    public void listToMapTest() {
        ImmutableList<String> animalsList = ImmutableList.copyOf(animals);

        // 1. by multimap
        // a=[aardvark], e=[elephant, eagle], k=[koala, kangaroo]
        ImmutableListMultimap<Character, String> animalsByFirstLetter =
                Multimaps.index(animalsList,
                        str -> Objects.requireNonNull(str).charAt(0));
        log.info(methodLog(animalsByFirstLetter.toString()));

        // 2. by ListToMap
        // @see veinthrough.api.collection.ListToMap#toListedMap(List, Function)
        log.info(methodLog(
                ListToMap.toListedMap(
                        animalsList,
                        str -> str.charAt(0))
                        .toString()
        ));
    }
}
