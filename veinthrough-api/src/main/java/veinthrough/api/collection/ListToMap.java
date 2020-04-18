package veinthrough.api.collection;

import veinthrough.api._interface.Identifiable;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author veinthrough
 * <p>
 * Generic converting a list to a map:
 * 1. non-identifiable + override + retain first/last
 * 2. identifiable + override + retain first/last
 * 3. non-identifiable + list, the same effect:
 * (1) toListedMap(list, keyFunction)
 * (2) Multimaps.index(list, keyFunction)
 * 4. identifiable + list
 * </pre>
 */
public class ListToMap {
    // 1. non-identifiable + override + retain first/last
    public static <K, T> Map<K, T> toUniqueMap(List<T> list,
                                               Function<? super T, ? extends K> keyFunction,
                                               RETAIN_MANNER manner) {
        return list.stream()
                .collect(
                        Collectors.toMap(
                                keyFunction,
                                Function.identity(),
                                retainFunction(manner)
                        )
                );
    }

    // 2. identifiable + override + retain first/last
    public static <K, T extends Identifiable<K>> Map<K, T> toUniqueMap(List<T> list,
                                                                       RETAIN_MANNER manner) {
        return list.stream()
                .collect(
                        Collectors.toMap(
                                T::getIdentifier,
                                Function.identity(),
                                retainFunction(manner)
                        )
                );
    }

    // 3. non-identifiable + list
    // Can also use Multimap of Guava
    // The same effect:
    //   (1) toListedMap(list, keyFunction)
    //   (2) Multimaps.index(list, keyFunction)
    public static <K, T> Map<K, List<T>> toListedMap(List<T> list,
                                                     Function<? super T, ? extends K> keyFunction) {
        return list.stream()
                .collect(Collectors.groupingBy(keyFunction));
    }

    // 4. identifiable + list
    public static <K, T extends Identifiable<K>> Map<K, List<T>> toListedMap(List<T> list) {
        return list.stream()
                .collect(Collectors.groupingBy(T::getIdentifier));
    }

    private static <K> BinaryOperator<K> retainFunction(RETAIN_MANNER manner) {
        return manner == RETAIN_MANNER.RETAIN_FIRST ?
                (k1, k2) -> k1 :
                (k1, k2) -> k2;
    }

    public enum RETAIN_MANNER {
        RETAIN_FIRST,
        RETAIN_LAST
    }
}
